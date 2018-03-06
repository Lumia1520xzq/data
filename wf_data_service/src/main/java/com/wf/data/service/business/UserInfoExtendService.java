package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import com.wf.data.service.TransAccountService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareUserInfoExtendBaseService;
import com.wf.data.service.data.DatawareUserInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/3/5
 */

@Service
public class UserInfoExtendService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //每次查询数量
    private static final long PAGE_SIZE = 50000;

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

    /**
     * 重置dataware_user_info_extend_base
     */
    public void resetUserInfoBase() {
        String yesterday = DateUtils.getYesterdayDate();
        try {

            //删除原有数据
            long allUserBaseCount = userInfoExtendBaseService.getAllCount();
            if (allUserBaseCount > 0){
                userInfoExtendBaseService.deleteAll();
            }

            /* 分页查询dataware_user_info数据**/
            long userCount = userInfoService.getCountByTime(new HashMap<>());
            int pageCount = (int) Math.ceil(1.0 * userCount / PAGE_SIZE);//算出总共需要多少页
            for (int i = 1; i <= pageCount; i++) {
                long minIndex = (i - 1) * PAGE_SIZE;
                long maxIndex = PAGE_SIZE;
                Map<String, Object> params = new HashMap<>();
                params.put("minIndex", minIndex);
                params.put("maxIndex", maxIndex);
                List<DatawareUserInfo> userInfos = userInfoService.getBaseUserInfoLimit(params);
                if (CollectionUtils.isNotEmpty(userInfos)) {
                    for (DatawareUserInfo userInfo : userInfos) {
                        DatawareUserInfoExtendBase userInfoExtendBase = new DatawareUserInfoExtendBase();

                        Map<String,Object> userParam = new HashMap<>();
                        userParam.put("userId",userInfo.getUserId());
                        //剩余金叶子数
                        Double useAmount = transAccountService.getUseAmountByUserId(userParam);
                        //出口成本
                        Double costAmount = phyAwardsSendlogService.getRmbAmountByUserId(userParam);
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
                        if (userInfo.getRegisteredDate().equals(yesterday)) {
                            userInfoExtendBase.setNewUserFlag(0);
                        } else {
                            userInfoExtendBase.setNewUserFlag(1);
                        }

                        userInfoExtendBaseService.save(userInfoExtendBase);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("用户维度基本信息表重置结束:traceId={}", TraceIdUtils.getTraceId());
    }
}
