package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransAccountService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareUserInfoExtendBaseService;
import com.wf.data.service.data.DatawareUserInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author shihui
 * @date 2018/3/5
 */

@Service
public class UserInfoExtendService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //每次查询数量
    private static final long PAGE_SIZE = 50000;

    private static final String YESTERDAY = DateUtils.getYesterdayDate();

    @Autowired
    private DatawareUserInfoService userInfoService;
    @Autowired
    private DatawareUserInfoExtendBaseService userInfoExtendBaseService;
    @Autowired
    private InventoryPhyAwardsSendlogService phyAwardsSendlogService;
    @Autowired
    private DatawareBuryingPointDayService buryingPointDayService;
    @Autowired
    private TransAccountService transAccountService;
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;

    /**
     * 重置dataware_user_info_extend_base
     */
    public void resetUserInfoBase() {
        try {

            //删除原有数据
            long allUserBaseCount = userInfoExtendBaseService.getAllCount();
            if (allUserBaseCount > 0) {
                userInfoExtendBaseService.deleteAll();
            }

            /* 分页查询dataware_user_info数据**/
            HashMap<String,Object> alluserInfoParam = new HashMap<>();
            alluserInfoParam.put("yesterdayParam",YESTERDAY);
            long userCount = userInfoService.getCountByTime(alluserInfoParam);
            int pageCount = (int) Math.ceil(1.0 * userCount / PAGE_SIZE);//算出总共需要多少页
            for (int i = 1; i <= pageCount; i++) {
                long minIndex = (i - 1) * PAGE_SIZE;
                long maxIndex = PAGE_SIZE;
                Map<String, Object> params = new HashMap<>();
                params.put("minIndex", minIndex);
                params.put("maxIndex", maxIndex);
                params.put("endDate",YESTERDAY);
                List<DatawareUserInfo> userInfos = userInfoService.getBaseUserInfoLimit(params);
                if (CollectionUtils.isNotEmpty(userInfos)) {
                    for (DatawareUserInfo userInfo : userInfos) {
                        saveInfo(userInfo);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("重置用户维度基本信息失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("用户维度基本信息表重置结束:traceId={}", TraceIdUtils.getTraceId());
    }

    /**
     * 定时清洗用户基本信息
     */
    public void toDoAnalysis() {
        /* 更改用户分类和用户状态*/
        //1.重置用户分类和用户状态
        resetUserGroup();
        //2.获取非正常用户
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }
        //3.更改dataware_user_info_extend_base中的user_group
        if (CollectionUtils.isNotEmpty(uicGroupList)) {
            for (Long userId : uicGroupList) {
                DatawareUserInfoExtendBase userInfoExtendBase = userInfoExtendBaseService.getByUserId(userId);
                if (null != userInfoExtendBase){
                    userInfoExtendBase.setUserGroup(1);
                    userInfoExtendBaseService.save(userInfoExtendBase);
                }
            }
        }

    /* 获取活跃用户，只更改活跃用户的信息*/
        //1.获取昨天所有活跃用户
        Map<String, Object> bpParam = new HashMap<>();
        bpParam.put("businessDate", DateUtils.getYesterdayDate());
        List<Long> activeUserIds = buryingPointDayService.getUserIdListByChannel(bpParam);

        //2.修改活跃用户信息
        if (CollectionUtils.isNotEmpty(activeUserIds)) {
            for (Long activeUserId : activeUserIds) {
                DatawareUserInfoExtendBase userInfoExtendBase = userInfoExtendBaseService.get(activeUserId);
                if (userInfoExtendBase == null) {//新增用户
                    //获取新用户基本信息
                    DatawareUserInfo userInfo = userInfoService.get(activeUserId);
                    if (userInfo != null) {
                        saveInfo(userInfo);
                    }
                } else {//老用户
                    Map<String, Object> userParam = new HashMap<>();
                    userParam.put("userId", activeUserId);

                    //剩余金叶子数
                    Double useAmount = judgeNull(transAccountService.getUseAmountByUserId(userParam));
                    //出口成本
                    Double costAmount = judgeNull(phyAwardsSendlogService.getRmbAmountByUserId(userParam));
                    //最后一次活跃时间
                    String lastActiveDate = buryingPointDayService.getLastActiveDate(userParam);
                    //活跃天数
                    int activeDates = buryingPointDayService.getActiveDatesByUser(userParam);

                    userInfoExtendBase.setNoUseGoldAmount(useAmount);
                    userInfoExtendBase.setCostAmount(costAmount);
                    userInfoExtendBase.setLastActiveDate(lastActiveDate);
                    userInfoExtendBase.setActiveDates(activeDates);
                    userInfoExtendBaseService.save(userInfoExtendBase);
                }
            }
        }

    }

    /**
     * 按月份重置用户分类和用户状态
     */
    private void resetUserGroup() {
        /* 按月循环更新*/
        try {
            //获取第一个用户注册时间
            String earliestRegisteDate = userInfoExtendBaseService.getEarliestRegisteDate();
            Date beginDate = new SimpleDateFormat("yyyy-MM").parse(earliestRegisteDate);//定义开始日期
            Date endDate = new SimpleDateFormat("yyyy-MM").parse(DateUtils.getYesterdayDate());//定义结束日期
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(beginDate);//设置日期起始时间
            while(dd.getTime().before(endDate)){//判断是否到结束日期
                String monthStr = DateUtils.formatDate(dd.getTime(),DateUtils.MONTH_PATTERN);
                userInfoExtendBaseService.updateUserGroupAndNewFlag(monthStr);
                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
        }catch (Exception e){
            logger.error("重置用户分类和用户状态失败: traceId={}", TraceIdUtils.getTraceId());
        }


    }

    /**
     * 根据用户清洗表保存dataware_user_info_extend_base
     * @param userInfo
     */
    private void saveInfo(DatawareUserInfo userInfo) {
        DatawareUserInfoExtendBase userInfoExtendBase = new DatawareUserInfoExtendBase();

        Map<String, Object> userParam = new HashMap<>();
        userParam.put("userId", userInfo.getUserId());
        //剩余金叶子数
        Double useAmount = judgeNull(transAccountService.getUseAmountByUserId(userParam));
        //出口成本
        Double costAmount = judgeNull(phyAwardsSendlogService.getRmbAmountByUserId(userParam));
        //首次活跃时间
        String firstActiveDate = buryingPointDayService.getFirstActiveDate(userParam);
        //最后一次活跃时间
        String lastActiveDate = buryingPointDayService.getLastActiveDate(userParam);
        //活跃天数
        int activeDates = buryingPointDayService.getActiveDatesByUser(userParam);

        userInfoExtendBase.setUserId(userInfo.getUserId());
        userInfoExtendBase.setChannelId(userInfo.getChannelId());
        userInfoExtendBase.setRegisteredTime(userInfo.getRegisteredTime());
        userInfoExtendBase.setUserGroup(userInfo.getUserGroup());//是否正常用户
        userInfoExtendBase.setNoUseGoldAmount(useAmount);
        userInfoExtendBase.setCostAmount(costAmount);
        userInfoExtendBase.setFirstActiveDate(firstActiveDate);
        userInfoExtendBase.setLastActiveDate(lastActiveDate);
        userInfoExtendBase.setActiveDates(activeDates);
        //首日新用户；1：不是，0：是；
        if (userInfo.getRegisteredDate().equals(YESTERDAY)) {
            userInfoExtendBase.setNewUserFlag(0);
        } else {
            userInfoExtendBase.setNewUserFlag(1);
        }
        userInfoExtendBaseService.save(userInfoExtendBase);
    }

    /**
     * 判断null
     *
     * @param d1
     * @return
     */
    private Double judgeNull(Double d1) {
        if (d1 == null) {
            return 0.0;
        } else {
            return d1;
        }
    }
}
