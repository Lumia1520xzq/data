package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.*;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransAccountService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.data.*;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import com.wf.data.service.wars.WarsRoomService;
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
    @Autowired
    private DatawareBettingLogDayService bettingLogDayService;
    @Autowired
    private WarsRoomService warsRoomService;
    @Autowired
    private DatawareConvertDayService convertDayService;
    @Autowired
    private DatawareUserInfoExtendStatisticsService userInfoExtendStatisticsService;

    /**
     * 重置dataware_user_info_extend_base
     */
    public void resetUserInfoBase() {
        try {
            //删除原有数据
            long allUserBaseCount = userInfoExtendBaseService.getAllCount();
            if (allUserBaseCount > 0) {
                userInfoExtendBaseService.deleteAll();
                userInfoExtendStatisticsService.deleteAll();
            }

            /* 分页查询dataware_user_info数据**/
            HashMap<String, Object> alluserInfoParam = new HashMap<>();
            alluserInfoParam.put("yesterdayParam", YESTERDAY);
            long userCount = userInfoService.getCountByTime(alluserInfoParam);
            int pageCount = (int) Math.ceil(1.0 * userCount / PAGE_SIZE);//算出总共需要多少页
            for (int i = 1; i <= pageCount; i++) {
                long minIndex = (i - 1) * PAGE_SIZE;
                long maxIndex = PAGE_SIZE;
                Map<String, Object> params = new HashMap<>();
                params.put("minIndex", minIndex);
                params.put("maxIndex", maxIndex);
                params.put("endDate", YESTERDAY);
                List<DatawareUserInfo> userInfos = userInfoService.getBaseUserInfoLimit(params);
                if (CollectionUtils.isNotEmpty(userInfos)) {
                    for (DatawareUserInfo userInfo : userInfos) {
                        saveBaseInfo(userInfo);
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
                if (null != userInfoExtendBase) {
                    userInfoExtendBase.setUserGroup(1);
                    userInfoExtendBaseService.save(userInfoExtendBase);
                }
            }
        }

    /* 获取活跃用户，只更改活跃用户的信息*/
        //1.获取昨天所有活跃用户
        Map<String, Object> bpParam = new HashMap<>();
        bpParam.put("businessDate", YESTERDAY);
        List<Long> activeUserIds = buryingPointDayService.getUserIdListByChannel(bpParam);

        //2.修改活跃用户信息
        if (CollectionUtils.isNotEmpty(activeUserIds)) {
            for (Long activeUserId : activeUserIds) {
                DatawareUserInfoExtendBase userInfoExtendBase = userInfoExtendBaseService.getByUserId(activeUserId);
                if (userInfoExtendBase == null) {//新增用户
                    //获取新用户基本信息
                    DatawareUserInfo userInfo = userInfoService.get(activeUserId);
                    if (userInfo != null) {
                        saveBaseInfo(userInfo);
                    }
                } else {//老用户
                    updateUserExtendInfo(userInfoExtendBase);
                }
            }
        }

    }

    /**
     * 修改活跃用户维度信息
     * @param userInfoExtendBase
     */
    private void updateUserExtendInfo(DatawareUserInfoExtendBase userInfoExtendBase) {
        Map<String, Object> userParam = new HashMap<>();
        Long userId = userInfoExtendBase.getUserId();
        userParam.put("userId", userId);

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

        //保存用户投注，充值数据
        saveStatisticsInfo(userId, useAmount, costAmount);
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
            while (dd.getTime().before(endDate)) {//判断是否到结束日期
                String monthStr = DateUtils.formatDate(dd.getTime(), DateUtils.MONTH_PATTERN);
                userInfoExtendBaseService.updateUserGroupAndNewFlag(monthStr);
                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
        } catch (Exception e) {
            logger.error("重置用户分类和用户状态失败: traceId={}", TraceIdUtils.getTraceId());
        }

    }

    /**
     * 根据用户清洗表保存dataware_user_info_extend_base
     *
     * @param userInfo
     */
    private void saveBaseInfo(DatawareUserInfo userInfo) {
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

        //保存用户投注，充值数据
        saveStatisticsInfo(userInfo.getUserId(), useAmount, costAmount);
    }

    /**
     * 保存用户投注，充值数据
     */
    private void saveStatisticsInfo(Long userId, Double useAmount, Double costAmount) {
        DatawareUserInfoExtendStatistics userInfoExtendStatistics = new DatawareUserInfoExtendStatistics();
        Map<String, Object> baseParam = new HashMap<>();
        baseParam.put("userId", userId);
        baseParam.put("endDate", YESTERDAY);

        Double totalBettingAmount = 0D;
        Double totalResultAmount = 0D;
        Double totalDiffAmount = 0D;
        Long totalBettingCount;
        Double averageBettingAmount;
        Double profitAmount;
        Double totalWarsProfit;
        Double totalWarsBetting;
        Integer rechargeType;
        String firstRechargeTime;
        String lastRechargeTime;
        Double totalRechargeAmount = 0D;
        Long totalRechargeCount = 0L;
        Double averageRechargeAmount;
        //获取用户投注信息
        DatawareBettingLogDay bettingLogDay = bettingLogDayService.getInfoByUser(baseParam);
        //获取用户充值信息
        DatawareConvertDay convertDay = convertDayService.getInfoByUser(baseParam);

        /*用户投注信息*/
        if (null != bettingLogDay) {
            //累计投注金额
            totalBettingAmount = judgeNull(bettingLogDay.getBettingAmount());
            //返奖金额
            totalResultAmount = judgeNull(bettingLogDay.getResultAmount());
            //累计流水差
            totalDiffAmount = BigDecimalUtil.sub(totalBettingAmount, totalResultAmount);
        }
        //投注笔数
        totalBettingCount = bettingLogDayService.getBettingCountByUser(baseParam);
        //平均每笔投注金额(累计投注金额/累计投注笔数)
        averageBettingAmount = cal(totalBettingAmount, totalBettingCount);
        //累计物资争夺盈利
        totalWarsProfit = warsRoomService.getTotalWarsProfitByUser(baseParam);
        //累计物资争夺支持
        totalWarsBetting = warsRoomService.getTotalWarsBettingByUser(baseParam);

        /*用户充值信息*/
        if (convertDay != null) {
            //累计充值金额
            totalRechargeAmount = convertDay.getThirdAmount();
            //累计充值次数
            totalRechargeCount = Long.parseLong(convertDay.getRechargeCount().toString());
        }
        //用户分层
        rechargeType = getUserRechargeType(totalRechargeAmount);
        //首次充值时间
        firstRechargeTime = convertDayService.getFirstRechargeTime(baseParam);
        //最后一次充值时间
        lastRechargeTime = convertDayService.getLastRechargeTime(baseParam);
        //单次充值金额
        averageRechargeAmount = cal(totalRechargeAmount, totalRechargeCount);
        //用户盈亏状态
        profitAmount = getProfitAmount(useAmount, costAmount, totalWarsProfit, totalRechargeAmount, totalWarsBetting);

        userInfoExtendStatistics.setUserId(userId);
        userInfoExtendStatistics.setTotalBettingAmount(totalBettingAmount);
        userInfoExtendStatistics.setTotalResultAmount(totalResultAmount);
        userInfoExtendStatistics.setTotalDiffAmount(totalDiffAmount);
        userInfoExtendStatistics.setTotalBettingCount(totalBettingCount);
        userInfoExtendStatistics.setAverageBettingAmount(averageBettingAmount);
        userInfoExtendStatistics.setProfitAmount(profitAmount);
        userInfoExtendStatistics.setTotalWarsProfit(totalWarsProfit);
        userInfoExtendStatistics.setTotalWarsBetting(totalWarsBetting);
        userInfoExtendStatistics.setFirstRechargeTime(firstRechargeTime);
        userInfoExtendStatistics.setLastRechargeTime(lastRechargeTime);
        userInfoExtendStatistics.setTotalRechargeAmount(totalRechargeAmount);
        userInfoExtendStatistics.setTotalRechargeCount(totalRechargeCount);
        userInfoExtendStatistics.setAverageRechargeAmount(averageRechargeAmount);
        userInfoExtendStatistics.setRechargeType(rechargeType);
        userInfoExtendStatisticsService.save(userInfoExtendStatistics);

    }

    /**
     * 计算用户盈亏
     * (用户剩余金叶子数/1000+出口成本+物资争夺盈利/1000-用户充值金额-物资争夺支持/1000，单位为元)
     *
     * @param useAmount
     * @param costAmount
     * @param totalWarsProfit
     * @param totalRechargeAmount
     * @param totalWarsBetting
     * @return
     */
    private Double getProfitAmount(Double useAmount, Double costAmount, Double totalWarsProfit, Double totalRechargeAmount, Double totalWarsBetting) {
        double useAmountRmb = BigDecimalUtil.div(useAmount, 1000);
        double totalWarsProfitRmb = BigDecimalUtil.div(totalWarsProfit, 1000);
        double totalWarsBettingRmb = BigDecimalUtil.div(totalWarsBetting, 1000);
        double cost = BigDecimalUtil.add(BigDecimalUtil.add(useAmountRmb, costAmount), totalWarsProfitRmb);
        double profit = BigDecimalUtil.add(costAmount, totalWarsBettingRmb);
        return BigDecimalUtil.sub(cost, profit);
    }

    /**
     * 用户分层
     * (累充0/累充1-100/累充100-1000/累充1000-10000/累充10000-100000/累充10万以上，区间为左闭右开；按累计充值金额)
     *
     * @param totalRechargeAmount
     * @return
     */
    private Integer getUserRechargeType(Double totalRechargeAmount) {
        int rechargeType = 0;
        if (totalRechargeAmount >= 1 && totalRechargeAmount < 100) {
            rechargeType = 1;
        } else if (totalRechargeAmount >= 100 && totalRechargeAmount < 1000) {
            rechargeType = 2;
        } else if (totalRechargeAmount >= 1000 && totalRechargeAmount < 10000) {
            rechargeType = 3;
        } else if (totalRechargeAmount >= 10000 && totalRechargeAmount < 10000) {
            rechargeType = 4;
        } else {
            rechargeType = 5;
        }
        return rechargeType;
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

    private Double cal(Double d1, Long d2) {
        if (d1 == null || d1 == 0D || d2 == null || d2 == 0D) {
            return 0D;
        } else {
            return BigDecimalUtil.div(d1, d2);
        }
    }
}
