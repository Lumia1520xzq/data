package com.wf.data.service.data;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.service.CrudService;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.constants.UserRechargeTypeConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.DatawareFinalRechargeTagAnalysisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.TransConvertService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRechargeTagAnalysisService extends CrudService<DatawareFinalRechargeTagAnalysisDao, DatawareFinalRechargeTagAnalysis> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
        Map<String, Object> recparams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getParentId());
            params.put("parentId", channelInfo.getParentId());
        } else {
            userParams.remove("parentId");
            params.remove("parentId");
        }
        userParams.put("businessDate", yesterdayDate);

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);

        recparams.put("businessDate", yesterdayDate);
        DatawareFinalChannelInfoAll infoAll = datawareConvertDayService.getRechargeByDate(recparams);
        //新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(userParams);
        info = getDauInfo(info, registerdList, dauUserList, userParams);
        info = getBettingInfo(info, registerdList, userParams);
        info = getRechargeInfo(info, registerdList, infoAll.getBettingAmount(), userParams);
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
        String beginDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(yesterdayDate), -6));
        params.put("businessDate", yesterdayDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getParentId());
            retentionParams.put("parentId", channelInfo.getParentId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
        }
        userParams.put("beginDate", beginDate);
        userParams.put("endDate", yesterdayDate);

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdByDates(userParams);

        retentionParams.put("businessDate", businessDate);
        //新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);
        if (CollectionUtils.isEmpty(registerdList)) {
            tagAnalysis.setWeekLostRate(0.00);
        } else {
            Collection interColl = CollectionUtils.subtract(registerdList, dauUserList);
            //标签用户且活跃的用户
            List<Long> newUserDauList = (List<Long>) interColl;
            if (CollectionUtils.isEmpty(newUserDauList)) {
                tagAnalysis.setWeekLostRate(0.00);
            } else {
                tagAnalysis.setWeekLostRate(BigDecimalUtil.div(newUserDauList.size() * 100, registerdList.size(), 2));
            }
        }

        save(tagAnalysis);

        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(retentionParams, i);
            if (CollectionUtils.isEmpty(oldUserList)) {
                tagDto.setWeekLostRate(0.00);
            } else {
                Collection userInterColl = CollectionUtils.subtract(oldUserList, dauUserList);
                //标签用户且活跃的用户
                List<Long> userDauList = (List<Long>) userInterColl;
                if (CollectionUtils.isEmpty(userDauList)) {
                    tagDto.setWeekLostRate(0.00);
                } else {
                    tagDto.setWeekLostRate(BigDecimalUtil.div(userDauList.size() * 100, oldUserList.size(), 2));
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
        params.put("businessDate", businessDate);
        params.put("userTag", UserRechargeTypeConstants.NEW_USER_TYPE_0);

        DatawareFinalRechargeTagAnalysis tagAnalysis = getTagAnalysisDate(params);
        if (null == tagAnalysis) return;

        Map<String, Object> userParams = new HashMap<>();
        Map<String, Object> retentionParams = new HashMap<>();

        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getParentId());
            retentionParams.put("parentId", channelInfo.getParentId());
        } else {
            userParams.remove("parentId");
            retentionParams.remove("parentId");
        }
        userParams.put("businessDate", yesterdayDate);
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);

        //新注册用户
        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(retentionParams);

        Collection interColl = CollectionUtils.intersection(registerdList, dauUserList);
        //标签用户且活跃的用户
        List<Long> newUserDauList = (List<Long>) interColl;
        if (CollectionUtils.isEmpty(newUserDauList)) {
            tagAnalysis.setDayRetention(0.00);
        } else {
            if (0 != tagAnalysis.getDau()) {
                if (day == 1) {
                    tagAnalysis.setDayRetention(BigDecimalUtil.div(newUserDauList.size() * 100, tagAnalysis.getDau(), 2));
                } else {
                    tagAnalysis.setWeekRetention(BigDecimalUtil.div(newUserDauList.size() * 100, tagAnalysis.getDau(), 2));
                }
            }
        }
        save(tagAnalysis);

        for (int i = 1; i <= 6; i++) {
            params.remove("userTag");
            params.put("userTag", i);
            DatawareFinalRechargeTagAnalysis tagDto = getTagAnalysisDate(params);
            List<Long> oldUserList = userRechargeType(params, i);
            Collection userInterColl = CollectionUtils.intersection(oldUserList, dauUserList);
            //标签用户且活跃的用户
            List<Long> userDauList = (List<Long>) userInterColl;
            if (CollectionUtils.isEmpty(userDauList)) {
                tagAnalysis.setDayRetention(0.00);
            } else {
                if (0 != tagAnalysis.getDau()) {
                    if (day == 1) {
                        tagAnalysis.setDayRetention(BigDecimalUtil.div(userDauList.size() * 100, tagAnalysis.getDau(), 2));
                    } else {
                        tagAnalysis.setWeekRetention(BigDecimalUtil.div(userDauList.size() * 100, tagAnalysis.getDau(), 2));
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
        Map<String, Object> map = params;
        List<Long> userList = entitys.subList(startIndex, endIndex);
        map.put("userList", userList);
        return transConvertService.getCreateOrderUserId(map);

    }

    private DatawareFinalChannelInfoAll doGetRechargeInfo(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        Map<String, Object> map = params;
        List<Long> userList = entitys.subList(startIndex, endIndex);
        map.put("userList", userList);
        return datawareConvertDayService.getRechargeByDate(map);

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
        Double diffAmount = 0.00;
        Double resultRate = 0.00;
        Long bettingUserCount = 0L;
        Long bettingCount = 0L;
        Double bettingRate = 0.00;
        Double bettingArpu = 0.00;
        Double averageBettingCount = 0.00;
        Double averageBettingAsp = 0.00;


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

        if (0 != tagUserList.size()) {
            averageBettingCount = BigDecimalUtil.div(info.getBettingCount(), tagUserList.size(), 0);
        }
        info.setAverageBettingCount(averageBettingCount);
        if (0 != info.getBettingCount()) {
            averageBettingAsp = BigDecimalUtil.div(info.getBettingAmount(), info.getBettingCount(), 0);
        }
        info.setAverageBettingAsp(averageBettingAsp);
        return info;
    }


    private DatawareFinalChannelInfoAll doGetBettingInfo(List<Long> entitys, Map<String, Object> map, int startIndex, int endIndex) {
        Map<String, Object> params = map;
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
                info = getDauData(info, tagUserList, dauUserList);
                Long totalLoginCount = loginCount(tagUserList, params);
                if (totalLoginCount == null) totalLoginCount = 0L;
                info.setLoginCount(BigDecimalUtil.div(Double.valueOf(totalLoginCount), Double.valueOf(tagUserList.size()), 2));
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
     * 活跃数据
     *
     * @param dto
     * @param userList
     * @param dauUserList
     * @return
     */
    private DatawareFinalRechargeTagAnalysis getDauData(DatawareFinalRechargeTagAnalysis dto, List<Long> userList, List<Long> dauUserList) {

        Collection interColl = CollectionUtils.intersection(userList, dauUserList);
        //标签用户且活跃的用户
        List<Long> newUserDauList = (List<Long>) interColl;
        if (null != userList) {
            dto.setDau(Long.valueOf(newUserDauList.size()));
        }
        dto.setDauRate(BigDecimalUtil.div(dto.getDau() * 100, dauUserList.size(), 2));
        dto.setTotalUserCount(Long.valueOf(userList.size()));
        dto.setTotalUserRate(BigDecimalUtil.div(dto.getDau() * 100, dto.getTotalUserCount(), 2));
        return dto;
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
        Map<String, Object> map = params;
        List<Long> userList = entitys.subList(startIndex, endIndex);
        map.put("userList", userList);
        return datawareBuryingPointDayService.getUserPointCount(map);

    }


    public DatawareFinalRechargeTagAnalysis getTagAnalysisDate(Map<String, Object> map) {
        return dao.getTagAnalysisDate(map);
    }

}