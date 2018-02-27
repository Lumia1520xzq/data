package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
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
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
@Service
public class ChannelInfoAllService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private DatawareFinalChannelInfoAllService datawareFinalChannelInfoAllService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    public void toDoChannelInfoAllAnalysis() {
        logger.info("每天数据汇总开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DESTINATION_COLLECTING_FLAG);
        if (true == flag) {
            String yesterdayDate = DateUtils.getYesterdayDate();
            channelInfo(yesterdayDate);
        }

        logger.info("每天数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void channelInfo(String searchDay) {
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        try {
            Map<String, Object> countParams = new HashMap<>();
            countParams.put("businessDate", searchDay);
            long count = datawareFinalChannelInfoAllService.getCountByTime(countParams);
            if (count > 0) {
                datawareFinalChannelInfoAllService.deleteByDate(countParams);
            }
            Map<String, Object> mapAll = new HashMap<>();
            mapAll.put("businessDate", searchDay);
            dataKettle(mapAll, null, searchDay, 1);

            List<String> channels = Arrays.asList(channelIdList.split(","));

            List<Long> childChannelList = Lists.newArrayList();
            List<Long> parentChannelList = Lists.newArrayList();


            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        childChannelList.add(channel);
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("channelId", channel);
                        dataKettle(map, channelInfo, searchDay, 0);
                    } else {
                        parentChannelList.add(channel);
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);
                        dataKettle(parentMap, channelInfo, searchDay, 0);
                    }
                }

            }

            //汇总其他渠道
            parentChannelList.add(1L);
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", searchDay);
            params.put("childChannelList", childChannelList);
            params.put("parentChannelList", parentChannelList);
            dataKettle(params, null, searchDay, 0);


        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void dataKettle(Map<String, Object> params, ChannelInfo channelInfo, String businessDate, Integer flag) {

        DatawareFinalChannelInfoAll infoAll = new DatawareFinalChannelInfoAll();
        infoAll.setBusinessDate(businessDate);
        if (null == channelInfo) {
            if (flag == 0) {
                infoAll.setChannelId(0L);
                infoAll.setParentId(0L);
                infoAll.setChannelName("其他");
            } else {
                infoAll.setChannelId(1L);
                infoAll.setParentId(1L);
                infoAll.setChannelName("全部");
            }
        } else {
            infoAll.setChannelName(channelInfo.getName());
            infoAll.setChannelId(channelInfo.getId());
            if (null == channelInfo.getParentId()) {
                infoAll.setParentId(channelInfo.getId());
            } else {
                infoAll.setParentId(channelInfo.getParentId());
            }
        }


        //日活
        Long dau = datawareBuryingPointDayService.getDauByChannel(params);
        if (null == dau) dau = 0L;
        infoAll.setDau(dau);
        //充值
        DatawareFinalChannelInfoAll convertInfo = datawareConvertDayService.getConvertByDate(params);
        if (null != convertInfo) {
            if (null == convertInfo.getRechargeCount()) {
                infoAll.setRechargeCount(0L);
            } else {
                infoAll.setRechargeCount(convertInfo.getRechargeCount());
            }
            if (null == convertInfo.getRechargeAmount()) {
                infoAll.setRechargeAmount(0.00);
            } else {
                infoAll.setRechargeAmount(convertInfo.getRechargeAmount());
            }
        }

        //新增用户数
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(params);
        long newUserCount = newUserList.size();
        infoAll.setNewUsers(newUserCount);
        //投注人数list
        List<Long> beetingUserIdList = datawareBettingLogDayService.getBettingUserIdByDate(params);
        Collection interColl = CollectionUtils.intersection(newUserList, beetingUserIdList);
        //当日投注新增用户数
        List<Long> bettingNewUsers = (List<Long>) interColl;
        if (null != bettingNewUsers) {
            infoAll.setUserBettingCount(Long.valueOf(bettingNewUsers.size()));
        } else {
            infoAll.setUserBettingCount(0L);
        }
        //投注
        DatawareFinalChannelInfoAll bettingInfo = datawareBettingLogDayService.getBettingByDate(params);
        if (null != bettingInfo) {
            if (null == bettingInfo.getBettingAmount()) {
                infoAll.setBettingAmount(0.00);
            } else {
                infoAll.setBettingAmount(bettingInfo.getBettingAmount());
            }
            if (null == bettingInfo.getUserCount()) {
                infoAll.setUserCount(0L);
            } else {
                infoAll.setUserCount(bettingInfo.getUserCount());
            }
            if (null == bettingInfo.getBettingCount()) {
                infoAll.setBettingCount(0L);
            } else {
                infoAll.setBettingCount(bettingInfo.getBettingCount());
            }
            if (null == bettingInfo.getResultAmount()) {
                infoAll.setResultAmount(0.00);
            } else {
                infoAll.setResultAmount(bettingInfo.getResultAmount());
            }
        }

        //返奖率=返奖流水/投注流水
        double resultRate = 0.00;
        if (0 != infoAll.getBettingAmount()) {
            resultRate = BigDecimalUtil.div(infoAll.getResultAmount() * 100, infoAll.getBettingAmount(), 2);
        }
        infoAll.setResultRate(resultRate);
        //投注转化率=投注人数/DAU
        double bettingRate = 0.00;
        //DAU付费转化率=当日付费人数/当日DAU
        double dauPayRate = 0.00;
        //ARPU=当日充值金额/当日DAU
        double payArpu = 0.00;
        if (0 != dau) {
            bettingRate = BigDecimalUtil.div(infoAll.getUserCount() * 100, dau, 2);
            dauPayRate = BigDecimalUtil.div(infoAll.getRechargeCount() * 100, dau, 2);
            payArpu = BigDecimalUtil.div(infoAll.getRechargeAmount(), dau, 2);
        }
        infoAll.setBettingRate(bettingRate);
        infoAll.setDauPayRate(dauPayRate);
        infoAll.setPayArpu(payArpu);
        //投注付费转化率=当日充值人数/当日投注人数
        double bettingPayRate = 0.00;
        if (0 != infoAll.getUserCount()) {
            bettingPayRate = BigDecimalUtil.div(infoAll.getRechargeCount() * 100, infoAll.getUserCount(), 2);
        }
        infoAll.setBettingPayRate(bettingPayRate);
        //新用户投注转化率=当日投注新增用户数/当日新增用户数
        double userBettingRate = 0.00;
        if (0 != infoAll.getNewUsers()) {
            userBettingRate = BigDecimalUtil.div(infoAll.getUserBettingCount() * 100, infoAll.getNewUsers(), 2);
        }
        infoAll.setUserBettingRate(userBettingRate);
        //ARPPU当日充值金额/当日充值人数
        double payArppu = 0.00;
        if (0 != infoAll.getRechargeCount()) {
            payArppu = BigDecimalUtil.div(infoAll.getRechargeAmount(), infoAll.getRechargeCount(), 2);
        }
        infoAll.setPayArppu(payArppu);


        Double hisRecharge = datawareConvertDayService.getHistoryConvertByDate(params);
        if (null != hisRecharge) {
            infoAll.setHisRecharge(hisRecharge);
        } else {
            infoAll.setHisRecharge(0.00);
        }
        Long hisRegistered = datawareUserInfoService.getHistoryUserByDate(params);
        if (null != hisRegistered) {
            infoAll.setHisRegistered(hisRegistered);
        } else {
            infoAll.setHisRegistered(0L);
        }
        if (infoAll.getHisRegistered() > 0) {
            infoAll.setUserLtv(BigDecimalUtil.div(infoAll.getHisRecharge(), infoAll.getHisRegistered(), 2));
        } else {
            infoAll.setUserLtv(0.00);
        }


        //首日付费率 = 注册用户充值人数/注册人数（newUserList）
        //充值人数
        List<Long> rechargeUserIdList = datawareConvertDayService.getUserIdByDate(params);
        if (CollectionUtils.isNotEmpty(rechargeUserIdList) && CollectionUtils.isNotEmpty(newUserList)) {
            Collection rechargeInterColl = CollectionUtils.intersection(newUserList, rechargeUserIdList);
            //新用户充值人数
            List<Long> rechargeList = (List<Long>) rechargeInterColl;
            infoAll.setFirstRechargeRate(BigDecimalUtil.div(rechargeList.size() * 100, newUserList.size(), 2));
        } else {
            infoAll.setFirstRechargeRate(0.00);
        }

        try {
            datawareFinalChannelInfoAllService.save(infoAll);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(infoAll.toString()));
        }
        //更新7日付费率
        updateWeekRechargeRate(params, businessDate,infoAll.getParentId());

    }

    private void updateWeekRechargeRate( Map<String, Object> params, String businessDate,Long parentId) {
        Map<String,Object> map = new HashMap<>();
        String weekBeforeDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(businessDate), -6));
        map.put("businessDate", weekBeforeDate);
        map.put("parentId", parentId);
        DatawareFinalChannelInfoAll infoAll = datawareFinalChannelInfoAllService.findByDate(map);
        if (infoAll == null){
            logger.error("添加渠道汇总记录失败: traceId={}, data={}", TraceIdUtils.getTraceId(),null);
            return;
        }
        //新增用户数
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(params);

        params.put("beginDate", weekBeforeDate);
        params.put("endDate", businessDate);
        List<Long> rechargeUserIdList = datawareConvertDayService.getUserIdList(params);
        if (CollectionUtils.isNotEmpty(rechargeUserIdList) && CollectionUtils.isNotEmpty(newUserList)) {
            Collection rechargeInterColl = CollectionUtils.intersection(newUserList, rechargeUserIdList);
            //7日新用户充值人数
            List<Long> rechargeList = (List<Long>) rechargeInterColl;
            infoAll.setWeekRechargeRate(BigDecimalUtil.div(rechargeList.size() * 100, newUserList.size(), 2));
        } else {
            infoAll.setWeekRechargeRate(0.00);
        }
        try {
            datawareFinalChannelInfoAllService.save(infoAll);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(infoAll.toString()));
        }
    }

    @Async
    public void dataClean(String startTime, String endTime) {

        try {

            if (startTime.equals(endTime)) {
                channelInfo(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    channelInfo(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }


    @Async
    public void historyLtv(String startTime, String endTime) {

        try {

            if (startTime.equals(endTime)) {
                forChannel(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    forChannel(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void forChannel(String searchDay) {
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        try {
            Map<String, Object> countParams = new HashMap<>();
            countParams.put("businessDate", searchDay);
            countParams.put("parentId", 1);
            DatawareFinalChannelInfoAll info = datawareFinalChannelInfoAllService.getInfoByChannel(countParams);
            if (null != info) {
                Map<String, Object> mapAll = new HashMap<>();
                mapAll.put("businessDate", searchDay);
                updateLtv(mapAll, info);

            }

            List<String> channels = Arrays.asList(channelIdList.split(","));

            List<Long> childChannelList = Lists.newArrayList();
            List<Long> parentChannelList = Lists.newArrayList();


            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        childChannelList.add(channel);
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("channelId", channel);

                        DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(map);
                        if (null != channelInfoAll) {
                            updateLtv(map, channelInfoAll);
                        }


                    } else {
                        parentChannelList.add(channel);
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);

                        DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(parentMap);
                        if (null != channelInfoAll) {
                            updateLtv(parentMap, channelInfoAll);
                        }
                    }
                }

            }


            Map<String, Object> otherMap = new HashMap<>();
            otherMap.put("businessDate", searchDay);
            otherMap.put("parentId", 0);
            DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(otherMap);
            if (null != channelInfoAll) {
                //汇总其他渠道
                parentChannelList.add(1L);
                Map<String, Object> params = new HashMap<>();
                params.put("businessDate", searchDay);
                params.put("childChannelList", childChannelList);
                params.put("parentChannelList", parentChannelList);
                updateLtv(params, channelInfoAll);
            }

        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void updateLtv(Map<String, Object> map, DatawareFinalChannelInfoAll info) {
        Double hisRecharge = datawareConvertDayService.getHistoryConvertByDate(map);
        if (null != hisRecharge) {
            info.setHisRecharge(hisRecharge);
        } else {
            info.setHisRecharge(0.00);
        }
        Long hisRegistered = datawareUserInfoService.getHistoryUserByDate(map);
        if (null != hisRegistered) {
            info.setHisRegistered(hisRegistered);
        } else {
            info.setHisRegistered(0L);
        }
        if (info.getHisRegistered() > 0) {
            info.setUserLtv(BigDecimalUtil.div(info.getHisRecharge(), info.getHisRegistered(), 2));
        } else {
            info.setUserLtv(0.00);
        }

        try {
            datawareFinalChannelInfoAllService.save(info);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }
    }


    @Async
    public void historyRechargeRate(String startTime, String endTime) {

        try {

            if (startTime.equals(endTime)) {
                forChannelRate(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    forChannelRate(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void forChannelRate(String searchDay) {
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        try {
            Map<String, Object> countParams = new HashMap<>();
            countParams.put("businessDate", searchDay);
            countParams.put("parentId", 1);
            DatawareFinalChannelInfoAll info = datawareFinalChannelInfoAllService.getInfoByChannel(countParams);
            if (null != info) {
                Map<String, Object> mapAll = new HashMap<>();
                mapAll.put("businessDate", searchDay);
                updateRate(mapAll, info,searchDay);

            }

            List<String> channels = Arrays.asList(channelIdList.split(","));

            List<Long> childChannelList = Lists.newArrayList();
            List<Long> parentChannelList = Lists.newArrayList();


            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        childChannelList.add(channel);
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("channelId", channel);

                        DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(map);
                        if (null != channelInfoAll) {
                            updateRate(map, channelInfoAll,searchDay);
                        }


                    } else {
                        parentChannelList.add(channel);
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);

                        DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(parentMap);
                        if (null != channelInfoAll) {
                            updateRate(parentMap, channelInfoAll,searchDay);
                        }
                    }
                }

            }


            Map<String, Object> otherMap = new HashMap<>();
            otherMap.put("businessDate", searchDay);
            otherMap.put("parentId", 0);
            DatawareFinalChannelInfoAll channelInfoAll = datawareFinalChannelInfoAllService.getInfoByChannel(otherMap);
            if (null != channelInfoAll) {
                //汇总其他渠道
                parentChannelList.add(1L);
                Map<String, Object> params = new HashMap<>();
                params.put("businessDate", searchDay);
                params.put("childChannelList", childChannelList);
                params.put("parentChannelList", parentChannelList);
                updateRate(params, channelInfoAll,searchDay);
            }

        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void updateRate(Map<String, Object> map, DatawareFinalChannelInfoAll infoAll,String searchDay) {
        //新增用户数
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(map);
        //首日付费率 = 注册用户充值人数/注册人数（newUserList）
        //充值人数
        List<Long> rechargeUserIdList = datawareConvertDayService.getUserIdByDate(map);
        if (CollectionUtils.isNotEmpty(rechargeUserIdList) && CollectionUtils.isNotEmpty(newUserList)) {
            Collection rechargeInterColl = CollectionUtils.intersection(newUserList, rechargeUserIdList);
            //新用户充值人数
            List<Long> rechargeList = (List<Long>) rechargeInterColl;
            infoAll.setFirstRechargeRate(BigDecimalUtil.div(rechargeList.size() * 100, newUserList.size(), 2));
        } else {
            infoAll.setFirstRechargeRate(0.00);
        }


        Date endDate = DateUtils.getNextDate(DateUtils.parseDate(searchDay), 6);
        String weekAfterDate = DateUtils.formatDate(endDate);
        if (endDate.getTime() < DateUtils.parseDate(DateUtils.formatCurrentDateYMD()).getTime()) {
            map.put("beginDate", searchDay);
            map.put("endDate", weekAfterDate);
            List<Long> weekRechargeUserIdList = datawareConvertDayService.getUserIdList(map);
            if (CollectionUtils.isNotEmpty(weekRechargeUserIdList) && CollectionUtils.isNotEmpty(newUserList)) {
                Collection rechargeInterColl = CollectionUtils.intersection(newUserList, weekRechargeUserIdList);
                //7日新用户充值人数
                List<Long> rechargeList = (List<Long>) rechargeInterColl;
                infoAll.setWeekRechargeRate(BigDecimalUtil.div(rechargeList.size() * 100, newUserList.size(), 2));
            } else {
                infoAll.setWeekRechargeRate(0.00);
            }
        }

        try {
            datawareFinalChannelInfoAllService.save(infoAll);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(infoAll.toString()));
        }
    }

}
