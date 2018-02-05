package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelConversion;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * dataware_final_channel_conversion清洗
 * 转化分析相关
 *
 * @author liucs
 * @date 2018年01月15日
 */
@Service
public class ChannelConversionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelConversionService channelConversionService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    public void toDoConversionAnalysis() {
        logger.info("每日转化分析相关清洗开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_CONVERSION_FLAG);
        if (true == flag) {
            String yesterdayDate = DateUtils.getYesterdayDate();
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", yesterdayDate);
            long count = channelConversionService.getCountByTime(params);
            if (count > 0) {
                channelConversionService.deleteByDate(params);
            }
            conversionAnalysis(yesterdayDate);
        }


        logger.info("每日转化分析相关清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void conversionAnalysis(String searchDate) {
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        try {
            Map<String, Object> mapAll = new HashMap<>();
            mapAll.put("businessDate", searchDate);
            dataKettle(mapAll, null, searchDate);

            List<String> channels = Arrays.asList(channelIdList.split(","));

            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDate);
                        map.put("channelId", channel);
                        dataKettle(map, channelInfo, searchDate);
                    } else {
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDate);
                        parentMap.put("parentId", channel);
                        dataKettle(parentMap, channelInfo, searchDate);
                    }
                }

            }

        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }

    private void dataKettle(Map<String, Object> params, ChannelInfo channelInfo, String businessDate) {
        DatawareFinalChannelConversion info = new DatawareFinalChannelConversion();
        info.setBusinessDate(businessDate);
        if (null == channelInfo) {
            info.setChannelId(1L);
            info.setParentId(1L);
            info.setChannelName("全部");
        } else {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            if (null == channelInfo.getParentId()) {
                info.setParentId(channelInfo.getId());
            } else {
                info.setParentId(channelInfo.getParentId());
            }
        }
        //新增用户数
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(params);
        long newUserCount = 0L;
        if (CollectionUtils.isNotEmpty(newUserList)) {
            newUserCount = newUserList.size();
        } else {
            newUserList = new ArrayList<>();
        }
        info.setRegisteredCount(newUserCount);

        //日活
        long dau = 0L;
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(params);
        if (CollectionUtils.isNotEmpty(dauUserList)) {
            dau = dauUserList.size();
        } else {
            dauUserList = new ArrayList<>();
        }
        info.setDauCount(dau);

        Collection interColl = CollectionUtils.intersection(newUserList, dauUserList);
        //新用户日活
        List<Long> dauRegisteredList = (List<Long>) interColl;
        if (CollectionUtils.isNotEmpty(dauRegisteredList)) {
            info.setDauRegistered(Long.valueOf(dauRegisteredList.size()));
        } else {
            info.setDauRegistered(0L);
            dauRegisteredList = new ArrayList<>();
        }
        //老用户日活
        List<Integer> dauOlderList = (List<Integer>) CollectionUtils.disjunction(dauUserList, dauRegisteredList);

        if (CollectionUtils.isNotEmpty(dauOlderList)) {
            info.setDauOlder(Long.valueOf(dauOlderList.size()));
        } else {
            info.setDauOlder(0L);
            dauOlderList = new ArrayList<>();
        }
        //总进入游戏日活（去除gametype=0）
        params.put("gameFlag", 1);
        List<Long> gameDauList = datawareBuryingPointDayService.getUserIdListByChannel(params);
        if (CollectionUtils.isNotEmpty(gameDauList)) {
            info.setGamedauCount(Long.valueOf(gameDauList.size()));
        } else {
            info.setGamedauCount(0L);
            gameDauList = new ArrayList<>();
        }

        Collection gameDauInterColl = CollectionUtils.intersection(newUserList, gameDauList);
        //新用户进入游戏日活
        List<Long> gameDauRegisteredList = (List<Long>) gameDauInterColl;
        if (CollectionUtils.isNotEmpty(gameDauRegisteredList)) {
            info.setGamedauRegistered(Long.valueOf(gameDauRegisteredList.size()));
        } else {
            info.setGamedauRegistered(0L);
            gameDauRegisteredList = new ArrayList<>();
        }

        //老用户进入游戏日活
        List<Integer> gameDauOlderList = (List) CollectionUtils.disjunction(gameDauList, gameDauRegisteredList);
        if (CollectionUtils.isNotEmpty(gameDauOlderList)) {
            info.setGamedauOlder(Long.valueOf(gameDauOlderList.size()));
        } else {
            info.setGamedauOlder(0L);
            gameDauOlderList = new ArrayList<>();
        }

        //投注人数list
        List<Long> beetingUserIdList = datawareBettingLogDayService.getBettingUserIdByDate(params);
        if (CollectionUtils.isNotEmpty(beetingUserIdList)) {
            info.setBettingCount(Long.valueOf(beetingUserIdList.size()));
        } else {
            info.setBettingCount(0L);
            beetingUserIdList = new ArrayList<>();
        }


        Collection beetingInterColl = CollectionUtils.intersection(newUserList, beetingUserIdList);
        //新用户投注人数
        List<Long> bettingNewUsers = (List<Long>) beetingInterColl;
        if (null != bettingNewUsers) {
            info.setBettingRegistered(Long.valueOf(bettingNewUsers.size()));
        } else {
            info.setBettingRegistered(0L);
        }

        //老用户投注人数
        List<Integer> bettingOlderList = (List) CollectionUtils.disjunction(beetingUserIdList, bettingNewUsers);
        if (CollectionUtils.isNotEmpty(bettingOlderList)) {
            info.setBettingOlder(Long.valueOf(bettingOlderList.size()));
        } else {
            info.setBettingOlder(0L);
            bettingOlderList = new ArrayList<>();
        }

        //充值人数
        List<Long> rechargeUserIdList = datawareConvertDayService.getUserIdByDate(params);
        if (CollectionUtils.isNotEmpty(rechargeUserIdList)) {
            info.setRechargeCount(Long.valueOf(rechargeUserIdList.size()));
        } else {
            info.setRechargeCount(0L);
            rechargeUserIdList = new ArrayList<>();
        }

        Collection rechargeInterColl = CollectionUtils.intersection(newUserList, rechargeUserIdList);
        //新用户充值人数
        List<Long> rechargeList = (List<Long>) rechargeInterColl;
        if (null != rechargeList) {
            info.setRechargeRegistered(Long.valueOf(rechargeList.size()));
        } else {
            info.setRechargeRegistered(0L);
        }

        //老用户充值人数
        List<Integer> rechargeOlderList = (List) CollectionUtils.disjunction(rechargeUserIdList, rechargeList);
        if (CollectionUtils.isNotEmpty(rechargeOlderList)) {
            info.setRechargeOlder(Long.valueOf(rechargeOlderList.size()));
        } else {
            info.setRechargeOlder(0L);
            rechargeOlderList = new ArrayList<>();
        }
        //充值人数/当日DAU
        double payRate = 0.00;
        if (info.getDauCount() > 0) {
            payRate = BigDecimalUtil.div(info.getRechargeCount() * 100, info.getDauCount(), 2);
        }
        info.setPayRate(payRate);

        //新用户充值人数/新用户DAU
        double payRegisteredRate = 0.00;
        if (info.getDauRegistered() > 0) {
            payRegisteredRate = BigDecimalUtil.div(info.getRechargeRegistered() * 100, info.getDauRegistered(), 2);
        }
        info.setPayRegisteredRate(payRegisteredRate);

        //老用户充值人数/老用户DAU
        double payOlderRate = 0.00;
        if (info.getDauOlder() > 0) {
            payOlderRate = BigDecimalUtil.div(info.getRechargeOlder() * 100, info.getDauOlder(), 2);
        }
        info.setPayOlderRate(payOlderRate);

        //新用户DAU/注册人数
        double dauRegisteredRate = 0.00;
        if (info.getRegisteredCount() > 0) {
            dauRegisteredRate = BigDecimalUtil.div(info.getDauRegistered() * 100, info.getRegisteredCount(), 2);
        }
        info.setDauRegisteredRate(dauRegisteredRate);

        //总进入游戏人数/总DAU
        double gamedauRate = 0.00;
        if (info.getDauCount() > 0) {
            gamedauRate = BigDecimalUtil.div(info.getGamedauCount() * 100, info.getDauCount(), 2);
        }
        info.setGamedauRate(gamedauRate);

        //新用户进入游戏人数/新用户DAU
        double gamedauRegisteredRate = 0.00;
        if (info.getDauRegistered() > 0) {
            gamedauRegisteredRate = BigDecimalUtil.div(info.getGamedauRegistered() * 100, info.getDauRegistered(), 2);
        }
        info.setGamedauRegisteredRate(gamedauRegisteredRate);

        //老用户进入游戏人数/老用户DAU
        double gamedauOlderRate = 0.00;
        if (info.getDauOlder() > 0) {
            gamedauOlderRate = BigDecimalUtil.div(info.getGamedauOlder() * 100, info.getDauOlder(), 2);
        }
        info.setGamedauOlderRate(gamedauOlderRate);
        //总投注/总进入游戏人数
        double bettingRate = 0.00;
        if (info.getGamedauCount() > 0) {
            bettingRate = BigDecimalUtil.div(info.getBettingCount() * 100, info.getGamedauCount(), 2);
        }
        info.setBettingRate(bettingRate);

        //新用户投注/新用户进入游戏人数
        double bettingRegisteredRate = 0.00;
        if (info.getGamedauRegistered() > 0) {
            bettingRegisteredRate = BigDecimalUtil.div(info.getBettingRegistered() * 100, info.getGamedauRegistered(), 2);
        }
        info.setBettingRegisteredRate(bettingRegisteredRate);
        //老用户投注/老用户进入游戏人数
        double bettingOlderRate = 0.00;
        if (info.getGamedauOlder() > 0) {
            bettingOlderRate = BigDecimalUtil.div(info.getBettingOlder() * 100, info.getGamedauOlder(), 2);
        }
        info.setBettingOlderRate(bettingOlderRate);

        //总充值人数/总投注人数
        double rechargeRate = 0.00;
        if (info.getBettingCount() > 0) {
            rechargeRate = BigDecimalUtil.div(info.getRechargeCount() * 100, info.getBettingCount(), 2);
        }
        info.setRechargeRate(rechargeRate);

        //新用户充值人数/新用户投注人数
        double rechargeRegisteredRate = 0.00;
        if (info.getBettingRegistered() > 0) {
            rechargeRegisteredRate = BigDecimalUtil.div(info.getRechargeRegistered() * 100, info.getBettingRegistered(), 2);
        }
        info.setRechargeRegisteredRate(rechargeRegisteredRate);
        //老用户充值人数/老用户投注人数
        double rechargeOlderRate = 0.00;
        if (info.getBettingOlder() > 0) {
            rechargeOlderRate = BigDecimalUtil.div(info.getRechargeOlder() * 100, info.getBettingOlder(), 2);
        }
        info.setRechargeOlderRate(rechargeOlderRate);

        //新用户占比(注册用户/总DAU)
        double registeredRate = 0.00;
        if (info.getDauCount() > 0) {
            registeredRate = BigDecimalUtil.div(info.getRegisteredCount() * 100, info.getDauCount(), 2);
        }
        info.setRegisteredRate(registeredRate);


        try {
            channelConversionService.save(info);
        } catch (Exception e) {
            logger.error("dataware_final_channel_conversion添加汇总记录失败: traceId={},data={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(info.toString()), LogExceptionStackTrace.erroStackTrace(e));

        }


    }


    @Async
    public void dataClean(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                Map<String, Object> params = new HashMap<>();
                params.put("businessDate", endTime);
                long count = channelConversionService.getCountByTime(params);
                if (count > 0) {
                    channelConversionService.deleteByDate(params);
                }
                conversionAnalysis(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("businessDate", endTime);
                    long count = channelConversionService.getCountByTime(params);
                    if (count > 0) {
                        channelConversionService.deleteByDate(params);
                    }
                    conversionAnalysis(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }
}
