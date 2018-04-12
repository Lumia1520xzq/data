package com.wf.data.service.data;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.service.CrudService;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.MapUtils;
import com.wf.data.common.constants.UserRechargeTypeConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.DatawareFinalRechargeTagAnalysisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.TransConvertService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRechargeTagAnalysisService extends CrudService<DatawareFinalRechargeTagAnalysisDao, DatawareFinalRechargeTagAnalysis> {
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;
    @Autowired
    private TransConvertService transConvertService;

    /**
     * job调用入口
     */
    public void toDoAnalysis() {
        logger.info("用户分层分析开始:traceId={}", TraceIdUtils.getTraceId());
        //昨天
        String yesterdayDate = DateUtils.getYesterdayDate();
        //前天
        String twoDayBefore = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -2));

        try {
            //汇总全部渠道数据
            dataKettle(null, yesterdayDate, twoDayBefore);
            daysRetention(null, yesterdayDate, 1);
            daysRetention(null, yesterdayDate, 6);
            userLost(null, yesterdayDate);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                //汇总各个主渠道数据
                dataKettle(item, yesterdayDate, twoDayBefore);
                daysRetention(item, yesterdayDate, 1);
                daysRetention(item, yesterdayDate, 6);
                userLost(item, yesterdayDate);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("用户分层分析结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void dataKettle(ChannelInfo channelInfo, String yesterdayDate, String twoDayBefore) {

        DatawareFinalRechargeTagAnalysis info = setChannelInfo(channelInfo, yesterdayDate);
        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getId());
            params.put("parentId", channelInfo.getId());
        } else {
            userParams.remove("parentId");
            params.remove("parentId");
        }
        userParams.put("businessDate", yesterdayDate);
        //获取昨天的所有活跃
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);

        //获取昨天的所有充值相关信息
        DatawareFinalChannelInfoAll infoAll = datawareConvertDayService.getRechargeByDate(userParams);
        //获取昨天新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(userParams);
        //计算新用户的活跃相关信息
        info = getDauInfo(info, registerdList, dauUserList, userParams);
        info = getBettingInfo(info, registerdList, userParams);
        info = getRechargeInfo(info, registerdList, infoAll.getRechargeAmount(), userParams);
        info.setUserTag(UserRechargeTypeConstants.NEW_USER_TYPE_0);
        save(info);


        //充值用户分层
        params.put("businessDate", twoDayBefore);

        for (int i = 1; i <= 6; i++) {
            DatawareFinalRechargeTagAnalysis userRechargeType = setChannelInfo(channelInfo, yesterdayDate);
            List<Long> oldUserList = userRechargeType(params, i);
            userRechargeType = getDauInfo(userRechargeType, oldUserList, dauUserList, userParams);
            userRechargeType = getBettingInfo(userRechargeType, oldUserList, userParams);
            userRechargeType = getRechargeInfo(userRechargeType, oldUserList, infoAll.getBettingAmount(), userParams);
            userRechargeType.setUserTag(i);
            save(userRechargeType);
        }
    }

    /**
     * 用户流失
     *
     * @param channelInfo
     * @param yesterdayDate
     */
    private void userLost(ChannelInfo channelInfo, String yesterdayDate) {
        Map<String, Object> params = new HashMap<>();
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }
        String businessDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(yesterdayDate), -7));
        String rechargeDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(yesterdayDate), -8));
        String beginDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(yesterdayDate), -6));
        params.put("businessDate", businessDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();
        Map<String, Object> rechargeParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getId());
            retentionParams.put("parentId", channelInfo.getId());
            rechargeParams.put("parentId", channelInfo.getId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
            rechargeParams.remove("parentId");
        }
        userParams.put("beginDate", beginDate);
        userParams.put("endDate", yesterdayDate);

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdByDates(userParams);


        retentionParams.put("businessDate", businessDate);
        //新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);


        List<Long> tagDauUserList = datawareBuryingPointDayService.getUserIdListByChannel(retentionParams);

        Collection registerdInterColl = CollectionUtils.intersection(registerdList, tagDauUserList);
        //活跃的标签用户
        List<Long> registerdDauList = (List<Long>) registerdInterColl;

        if (CollectionUtils.isEmpty(registerdDauList)) {
            tagAnalysis.setWeekLostRate(0.00);
        } else {
            Collection interColl = CollectionUtils.subtract(registerdDauList, dauUserList);
            //标签用户且活跃的用户
            List<Long> newUserDauList = (List<Long>) interColl;
            if (CollectionUtils.isEmpty(newUserDauList)) {
                tagAnalysis.setWeekLostRate(0.00);
            } else {
                tagAnalysis.setWeekLostRate(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
            }
        }

        save(tagAnalysis);

        rechargeParams.put("businessDate", rechargeDate);
        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(rechargeParams, i);
            Collection oldInterColl = CollectionUtils.intersection(oldUserList, tagDauUserList);
            //活跃的标签用户
            List<Long> oldDauList = (List<Long>) oldInterColl;
            if (CollectionUtils.isEmpty(oldDauList)) {
                tagDto.setWeekLostRate(0.00);
            } else {
                Collection userInterColl = CollectionUtils.subtract(oldDauList, dauUserList);
                //标签用户且活跃的用户
                List<Long> userDauList = (List<Long>) userInterColl;
                if (CollectionUtils.isEmpty(userDauList)) {
                    tagDto.setWeekLostRate(0.00);
                } else {
                    tagDto.setWeekLostRate(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                }
            }
            save(tagDto);
        }


    }

    /**
     * 更新用户留存
     *
     * @param channelInfo
     * @param yesterdayDate
     * @param day
     */
    private void daysRetention(ChannelInfo channelInfo, String yesterdayDate, int day) {

        Map<String, Object> params = new HashMap<>();
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }
        String businessDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(yesterdayDate), -day));
        String rechargeDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(businessDate), -1));
        params.put("businessDate", businessDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();
        Map<String, Object> rechargeParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getId());
            retentionParams.put("parentId", channelInfo.getId());
            rechargeParams.put("parentId", channelInfo.getId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
            rechargeParams.remove("parentId");
        }
        userParams.put("businessDate", yesterdayDate);
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);

        //新注册用户
        retentionParams.put("businessDate", businessDate);
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);

        List<Long> tagDauUserList = datawareBuryingPointDayService.getUserIdListByChannel(retentionParams);

        Collection registerdInterColl = CollectionUtils.intersection(registerdList, tagDauUserList);
        //活跃的标签用户
        List<Long> registerdDauList = (List<Long>) registerdInterColl;

        Collection interColl = CollectionUtils.intersection(registerdDauList, dauUserList);

        List<Long> newUserDauList = (List<Long>) interColl;
        if (CollectionUtils.isEmpty(newUserDauList)) {
            if (day == 1) {
                tagAnalysis.setDayRetention(0.00);
            } else {
                tagAnalysis.setWeekRetention(0.00);
            }
        } else {
            if (CollectionUtils.isNotEmpty(registerdDauList)) {
                if (day == 1) {
                    tagAnalysis.setDayRetention(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
                } else {
                    tagAnalysis.setWeekRetention(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
                }
            }
        }
        save(tagAnalysis);

        rechargeParams.put("businessDate", rechargeDate);
        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(rechargeParams, i);
            Collection oldInterColl = CollectionUtils.intersection(oldUserList, tagDauUserList);
            //活跃的标签用户
            List<Long> oldDauList = (List<Long>) oldInterColl;

            Collection userInterColl = CollectionUtils.intersection(oldDauList, dauUserList);
            //标签用户且活跃的用户
            List<Long> userDauList = (List<Long>) userInterColl;
            if (CollectionUtils.isEmpty(userDauList)) {
                if (day == 1) {
                    tagDto.setDayRetention(0.00);
                } else {
                    tagDto.setWeekRetention(0.00);
                }
            } else {
                if (CollectionUtils.isNotEmpty(oldDauList)) {
                    if (day == 1) {
                        tagDto.setDayRetention(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                    } else {
                        tagDto.setWeekRetention(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                    }
                }
            }
            save(tagDto);
        }

    }


    /**
     * 获取充值信息
     *
     * @param info
     * @param tagUserList
     * @param params
     * @return
     */
    private DatawareFinalRechargeTagAnalysis getRechargeInfo(DatawareFinalRechargeTagAnalysis info, List<Long> tagUserList, Double totalAmount, Map<String, Object> params) {
        if (CollectionUtils.isEmpty(tagUserList)) {
            return info;
        }
        Double rechargeAmount = 0.00;
        Long userCount = 0L;
        Long rechargeCount = 0L;
        Long createOrder = 0L;
        int i = 0;
        /*Collection interColl = CollectionUtils.intersection(tagUserList, dauUserList);
        //标签用户且活跃的用户
        List<Long> userDauList = (List<Long>) interColl;*/
        while (i < tagUserList.size()) {
            int var10002 = i;
            i += 1000;
            DatawareFinalChannelInfoAll channelInfo = doGetRechargeInfo(tagUserList, params, var10002, i >= tagUserList.size() ? tagUserList.size() : i);
            createOrder += doCreateOrderUserId(tagUserList, params, var10002, i >= tagUserList.size() ? tagUserList.size() : i);
            rechargeAmount += channelInfo.getRechargeAmount();
            userCount += channelInfo.getUserCount();
            rechargeCount += channelInfo.getRechargeCount();
        }

        info.setThirdAmount(rechargeAmount);
        info.setRechargeCount(rechargeCount);
        info.setRechargeUserCount(userCount);
        if (totalAmount == null) totalAmount = 0.00;
        if (0 != totalAmount) {
            info.setRechargeRate(BigDecimalUtil.div(info.getThirdAmount() * 100, totalAmount, 2));
        }
        if (0 != info.getDau()) {
            info.setDauRechargeRate(BigDecimalUtil.div(info.getRechargeUserCount() * 100, info.getDau(), 2));
        }
        if (0 != info.getBettingUserCount()) {
            info.setBettingRechargeRate(BigDecimalUtil.div(info.getRechargeUserCount() * 100, info.getBettingUserCount(), 2));
        }

        if (0 != info.getRechargeUserCount()) {
            info.setDarppuRate(BigDecimalUtil.div(info.getThirdAmount(), info.getRechargeUserCount(), 2));
        }
        if (0 != info.getRechargeUserCount()) {
            info.setAveragePayCount(BigDecimalUtil.div(info.getRechargeCount(), info.getRechargeUserCount(), 2));
        }
        if (0 != info.getRechargeCount()) {
            info.setAveragePayAmount(BigDecimalUtil.div(info.getThirdAmount(), info.getRechargeCount(), 2));
        }
        if (0 != info.getDau()) {
            info.setPointPayRate(BigDecimalUtil.div(createOrder * 100, info.getDau(), 2));
        }
        if (0 != createOrder) {
            info.setPaySuccessRate(BigDecimalUtil.div(info.getRechargeUserCount() * 100, createOrder, 2));
        }

        return info;
    }

    private Long doCreateOrderUserId(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        params.remove("userList");
        List<Long> userList = entitys.subList(startIndex, endIndex);
        params.put("userList", userList);
        return transConvertService.getCreateOrderUserId(params);

    }

    private DatawareFinalChannelInfoAll doGetRechargeInfo(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        params.remove("userList");
        List<Long> userList = entitys.subList(startIndex, endIndex);
        params.put("userList", userList);
        return datawareConvertDayService.getRechargeByDate(params);

    }

    /**
     * 获取投注信息
     *
     * @param info
     * @param tagUserList
     * @param params
     */
    private DatawareFinalRechargeTagAnalysis getBettingInfo(DatawareFinalRechargeTagAnalysis info, List<Long> tagUserList, Map<String, Object> params) {

        if (CollectionUtils.isEmpty(tagUserList)) {
            return info;
        }
        int i = 0;

        Double bettingAmount = 0.00;
        Double resultAmount = 0.00;
        Double diffAmount;
        Double resultRate = 0.00;
        Long bettingUserCount = 0L;
        Long bettingCount = 0L;
        Double bettingRate = 0.00;
        Double bettingArpu = 0.00;
        Double averageBettingCount = 0.00;
        Double averageBettingAsp = 0.00;
        /*Collection interColl = CollectionUtils.intersection(tagUserList, dauUserList);
        //标签用户且活跃的用户
        List<Long> userDauList = (List<Long>) interColl;*/


        while (i < tagUserList.size()) {
            int var10002 = i;
            i += 1000;
            DatawareFinalChannelInfoAll infoAll = doGetBettingInfo(tagUserList, params, var10002, i >= tagUserList.size() ? tagUserList.size() : i);

            bettingAmount += infoAll.getBettingAmount();
            resultAmount += infoAll.getResultAmount();
            bettingUserCount += infoAll.getUserCount();
            bettingCount += infoAll.getBettingCount();

        }
        info.setBettingAmount(bettingAmount);
        info.setResultAmount(resultAmount);
        diffAmount = info.getBettingAmount() - info.getResultAmount();
        info.setDiffAmount(diffAmount);
        if (0 != info.getBettingAmount()) {
            resultRate = BigDecimalUtil.div(info.getResultAmount() * 100, info.getBettingAmount(), 2);
        }
        info.setResultRate(resultRate);
        info.setBettingUserCount(bettingUserCount);
        info.setBettingCount(bettingCount);
        if (0 != tagUserList.size()) {
            bettingRate = BigDecimalUtil.div(info.getBettingUserCount() * 100, tagUserList.size(), 2);
        }
        info.setBettingRate(bettingRate);
        if (0 != info.getBettingUserCount()) {
            bettingArpu = BigDecimalUtil.div(info.getBettingAmount(), info.getBettingUserCount(), 2);
        }
        info.setBettingArpu(bettingArpu);

        if (0 != info.getBettingUserCount()) {
            averageBettingCount = BigDecimalUtil.div(info.getBettingCount(), info.getBettingUserCount(), 0);
        }
        info.setAverageBettingCount(averageBettingCount);
        if (0 != info.getBettingCount()) {
            averageBettingAsp = BigDecimalUtil.div(info.getBettingAmount(), info.getBettingCount(), 0);
        }
        info.setAverageBettingAsp(averageBettingAsp);
        return info;
    }


    private DatawareFinalChannelInfoAll doGetBettingInfo(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        params.remove("userList");
        List<Long> userList = entitys.subList(startIndex, endIndex);
        params.put("userList", userList);
        return datawareBettingLogDayService.getBettingByDate(params);

    }


    /**
     * 获取日活信息
     *
     * @param info
     * @param tagUserList
     * @param dauUserList
     * @param params
     */
    private DatawareFinalRechargeTagAnalysis getDauInfo(DatawareFinalRechargeTagAnalysis info, List<Long> tagUserList, List<Long> dauUserList, Map<String, Object> params) {

        if (CollectionUtils.isNotEmpty(tagUserList)) {
            if (CollectionUtils.isNotEmpty(dauUserList)) {
                Collection interColl = CollectionUtils.intersection(tagUserList, dauUserList);
                //标签用户且活跃的用户
                List<Long> newUserDauList = (List<Long>) interColl;
                if (CollectionUtils.isNotEmpty(newUserDauList)) {
                    info.setDau(Long.valueOf(newUserDauList.size()));
                }
                info.setDauRate(BigDecimalUtil.div(info.getDau() * 100, dauUserList.size(), 2));
                info.setTotalUserCount(Long.valueOf(tagUserList.size()));
                info.setTotalUserRate(BigDecimalUtil.div(info.getDau() * 100, info.getTotalUserCount(), 2));
                Long totalLoginCount = loginCount(newUserDauList, params);
                if (totalLoginCount == null) totalLoginCount = 0L;
                if (0 != info.getDau()) {
                    info.setLoginCount(BigDecimalUtil.div(Double.valueOf(totalLoginCount), info.getDau(), 2));
                }
            }
        }
        return info;
    }

    /**
     * 获取分层老用户
     *
     * @param rechargeType
     * @param map
     */
    private List<Long> userRechargeType(Map<String, Object> map, int rechargeType) {
        Map<String, Object> params = map;
        List<Long> oldUserList = Lists.newArrayList();
        if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_1) {
            //累充0老用户

            oldUserList = datawareUserInfoService.getNonRechargeUserId(params);
        } else if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_2) {
            //累充1-100老用户
            params.put("havingBegin", 1);
            params.put("havingEnd", 100);
            oldUserList = datawareConvertDayService.getTotalAmountUserId(params);
        } else if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_3) {
            //累充100-1000老用户
            params.put("havingBegin", 100);
            params.put("havingEnd", 1000);
            oldUserList = datawareConvertDayService.getTotalAmountUserId(params);
        } else if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_4) {
            //累充1000-10000老用户
            params.put("havingBegin", 1000);
            params.put("havingEnd", 10000);
            oldUserList = datawareConvertDayService.getTotalAmountUserId(params);
        } else if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_5) {
            //累充10000-100000老用户
            params.put("havingBegin", 10000);
            params.put("havingEnd", 100000);
            oldUserList = datawareConvertDayService.getTotalAmountUserId(params);
        } else if (rechargeType == UserRechargeTypeConstants.RECHARGE_TYPE_6) {
            //累充10万以上老用户
            params.put("havingBegin", 100000);
            params.remove("havingEnd");
            oldUserList = datawareConvertDayService.getTotalAmountUserId(params);
        }
        return oldUserList;
    }

    /**
     * 设置渠道
     *
     * @param channelInfo
     * @param yesterdayDate
     * @return
     */
    private DatawareFinalRechargeTagAnalysis setChannelInfo(ChannelInfo channelInfo, String yesterdayDate) {
        DatawareFinalRechargeTagAnalysis info = new DatawareFinalRechargeTagAnalysis().toInit();
        info.setBusinessDate(yesterdayDate);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }

        return info;
    }

    /**
     * 人均登陆次数
     * 一千个用户循环一次
     *
     * @param entitys
     * @param params
     * @return
     */
    private Long loginCount(List<Long> entitys, Map<String, Object> params) {
        Long loginCount = 0L;
        int i = 0;
        while (i < entitys.size()) {
            int var10002 = i;
            i += 1000;
            loginCount += doGetLoginCount(entitys, params, var10002, i >= entitys.size() ? entitys.size() : i);
        }
        return loginCount;

    }

    private Long doGetLoginCount(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        params.remove("userList");
        List<Long> userList = entitys.subList(startIndex, endIndex);
        params.put("userList", userList);
        return datawareBuryingPointDayService.getUserPointCount(params);

    }


    public DatawareFinalRechargeTagAnalysis getTagAnalysisDate(Map<String, Object> map) {
        return dao.getTagAnalysisDate(map);
    }

    public List<DatawareFinalRechargeTagAnalysis> getListByTagAndDate(Map<String,Object> map){
        return dao.getListByTagAndDate(map);
    }

    public List<String> getDateList(Map<String,Object> map){
        return dao.getDateList(map);
    }

    @Async
    public void historyEntranceAnalysis(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                dao.deleteByDate(MapUtils.toMap("businessDate", endTime));
                String twoDayBefore = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(endTime), -1));
                //汇总全部渠道数据
                dataKettle(null, endTime, twoDayBefore);
                historyDaysRetention(null, endTime, 1);
                historyDaysRetention(null, endTime, 6);
                historyUserLost(null, endTime);
                List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
                for (ChannelInfo item : channelInfoList) {
                    //汇总各个主渠道数据
                    dataKettle(item, endTime, twoDayBefore);
                    historyDaysRetention(item, endTime, 1);
                    historyDaysRetention(item, endTime, 6);
                    historyUserLost(item, endTime);
                }
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    dao.deleteByDate(MapUtils.toMap("businessDate", searchDate));
                    String twoDayBefore = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), -1));
                    //汇总全部渠道数据
                    dataKettle(null, searchDate, twoDayBefore);
                    historyDaysRetention(null, searchDate, 1);
                    historyDaysRetention(null, searchDate, 6);
                    historyUserLost(null, searchDate);
                    List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
                    for (ChannelInfo item : channelInfoList) {
                        //汇总各个主渠道数据
                        dataKettle(item, searchDate, twoDayBefore);
                        historyDaysRetention(item, searchDate, 1);
                        historyDaysRetention(item, searchDate, 6);
                        historyUserLost(item, searchDate);
                    }

                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("用户分层分析老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }


    /**
     * 用户流失
     *
     * @param channelInfo
     * @param searchDate
     */
    private void historyUserLost(ChannelInfo channelInfo, String searchDate) {
        Map<String, Object> params = new HashMap<>();
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }
        String endDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), 6));
        String beginDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), 1));
        String rechargeDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), -1));
        params.put("businessDate", searchDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();
        Map<String, Object> rechargeParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getId());
            retentionParams.put("parentId", channelInfo.getId());
            rechargeParams.put("parentId", channelInfo.getId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
            rechargeParams.remove("parentId");
        }
        userParams.put("beginDate", beginDate);
        userParams.put("endDate", endDate);

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdByDates(userParams);


        retentionParams.put("businessDate", searchDate);
        //新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);


        List<Long> tagDauUserList = datawareBuryingPointDayService.getUserIdListByChannel(retentionParams);

        Collection registerdInterColl = CollectionUtils.intersection(registerdList, tagDauUserList);
        //活跃的标签用户
        List<Long> registerdDauList = (List<Long>) registerdInterColl;

        if (CollectionUtils.isEmpty(registerdDauList)) {
            tagAnalysis.setWeekLostRate(0.00);
        } else {
            Collection interColl = CollectionUtils.subtract(registerdDauList, dauUserList);
            //标签用户且活跃的用户
            List<Long> newUserDauList = (List<Long>) interColl;
            if (CollectionUtils.isEmpty(newUserDauList)) {
                tagAnalysis.setWeekLostRate(0.00);
            } else {
                tagAnalysis.setWeekLostRate(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
            }
        }

        save(tagAnalysis);

        rechargeParams.put("businessDate", rechargeDate);
        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(rechargeParams, i);
            Collection oldInterColl = CollectionUtils.intersection(oldUserList, tagDauUserList);
            //活跃的标签用户
            List<Long> oldDauList = (List<Long>) oldInterColl;
            if (CollectionUtils.isEmpty(oldDauList)) {
                tagDto.setWeekLostRate(0.00);
            } else {
                Collection userInterColl = CollectionUtils.subtract(oldDauList, dauUserList);
                //标签用户且活跃的用户
                List<Long> userDauList = (List<Long>) userInterColl;
                if (CollectionUtils.isEmpty(userDauList)) {
                    tagDto.setWeekLostRate(0.00);
                } else {
                    tagDto.setWeekLostRate(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                }
            }
            save(tagDto);
        }


    }

    /**
     * 更新用户留存
     *
     * @param channelInfo
     * @param searchDate
     * @param day
     */
    private void historyDaysRetention(ChannelInfo channelInfo, String searchDate, int day) {

        Map<String, Object> params = new HashMap<>();
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }
        String businessDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), day));
        String beforeBusinessDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate), -1));
        params.put("businessDate", searchDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();
        Map<String, Object> beforeParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getId());
            retentionParams.put("parentId", channelInfo.getId());
            beforeParams.put("parentId", channelInfo.getId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
            beforeParams.remove("parentId");
        }
        userParams.put("businessDate", businessDate);
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);

        //新注册用户
        retentionParams.put("businessDate", searchDate);
        beforeParams.put("businessDate", beforeBusinessDate);
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);

        List<Long> tagDauUserList = datawareBuryingPointDayService.getUserIdListByChannel(retentionParams);

        Collection registerdInterColl = CollectionUtils.intersection(registerdList, tagDauUserList);
        //活跃的标签用户
        List<Long> registerdDauList = (List<Long>) registerdInterColl;

        Collection interColl = CollectionUtils.intersection(registerdDauList, dauUserList);

        List<Long> newUserDauList = (List<Long>) interColl;
        if (CollectionUtils.isEmpty(newUserDauList)) {
            if (day == 1) {
                tagAnalysis.setDayRetention(0.00);
            } else {
                tagAnalysis.setWeekRetention(0.00);
            }
        } else {
            if (CollectionUtils.isNotEmpty(registerdDauList)) {
                if (day == 1) {
                    tagAnalysis.setDayRetention(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
                } else {
                    tagAnalysis.setWeekRetention(BigDecimalUtil.div(newUserDauList.size() * 100, registerdDauList.size(), 2));
                }
            }
        }
        save(tagAnalysis);

        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(beforeParams, i);
            Collection oldInterColl = CollectionUtils.intersection(oldUserList, tagDauUserList);
            //活跃的标签用户
            List<Long> oldDauList = (List<Long>) oldInterColl;

            Collection userInterColl = CollectionUtils.intersection(oldDauList, dauUserList);
            //标签用户且活跃的用户
            List<Long> userDauList = (List<Long>) userInterColl;
            if (CollectionUtils.isEmpty(userDauList)) {
                if (day == 1) {
                    tagDto.setDayRetention(0.00);
                } else {
                    tagDto.setWeekRetention(0.00);
                }
            } else {
                if (CollectionUtils.isNotEmpty(oldDauList)) {
                    if (day == 1) {
                        tagDto.setDayRetention(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                    } else {
                        tagDto.setWeekRetention(BigDecimalUtil.div(userDauList.size() * 100, oldDauList.size(), 2));
                    }
                }
            }
            save(tagDto);
        }

    }

}