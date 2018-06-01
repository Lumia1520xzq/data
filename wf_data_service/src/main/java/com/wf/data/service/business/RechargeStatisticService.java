package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeStatistic;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareFinalRechargeStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RechargeStatisticService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private DatawareFinalRechargeStatisticService datawareFinalRechargeStatisticService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;

    public void toDoRechargeStatisticAnalysis() {
        logger.info("每天新增充值数据汇总开始:traceId={}", TraceIdUtils.getTraceId());
        rechargeInfo(DateUtils.getYesterdayDate());
        logger.info("每天新增充值数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void rechargeInfo(String searchDay) {
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        List<DatawareFinalRechargeStatistic> charges = Lists.newArrayList();
        try {
            Map<String, Object> countParams = new HashMap<>();
            countParams.put("businessDate", searchDay);
            long count = datawareFinalRechargeStatisticService.getCountByTime(countParams);
            if (count > 0) {
                datawareFinalRechargeStatisticService.deleteByDate(countParams);
            }
            Map<String, Object> mapAll = new HashMap<>();
            mapAll.put("businessDate", searchDay);
            charges.add(dataKettle(mapAll, null, searchDay, 1));

            List<String> channels = Arrays.asList(channelIdList.split(","));

            List<Long> childChannelList = Lists.newArrayList();
            List<Long> parentChannelList = Lists.newArrayList();
            Long channel;
            ChannelInfo channelInfo;
            Map<String, Object> map;
            for (String channelStr : channels) {
                channel = Long.valueOf(channelStr);
                channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        childChannelList.add(channel);
                        map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("channelId", channel);
                        charges.add(dataKettle(map, channelInfo, searchDay, 0));
                    } else {
                        parentChannelList.add(channel);
                        map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("parentId", channel);
                        charges.add(dataKettle(map, channelInfo, searchDay, 0));
                    }
                }
            }

            //汇总其他渠道
            parentChannelList.add(1L);
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", searchDay);
            params.put("childChannelList", childChannelList);
            params.put("parentChannelList", parentChannelList);
            charges.add(dataKettle(params, null, searchDay, 0));
            datawareFinalRechargeStatisticService.batchSave(charges);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private DatawareFinalRechargeStatistic dataKettle(Map<String, Object> params, ChannelInfo channelInfo, String businessDate, Integer flag) {
        DatawareFinalRechargeStatistic infoAll = new DatawareFinalRechargeStatistic();
        infoAll.setBusinessDate(businessDate);
        if (null == channelInfo) {
            if (flag == 0) {
                infoAll.setChannelId(0L);
                infoAll.setParentId(0L);
            } else {
                infoAll.setChannelId(1L);
                infoAll.setParentId(1L);
            }
        } else {
            infoAll.setChannelId(channelInfo.getId());
            if (null == channelInfo.getParentId()) {
                infoAll.setParentId(channelInfo.getId());
            } else {
                infoAll.setParentId(channelInfo.getParentId());
            }
        }
        // 新增充值人数
        Long newRechargeCount = datawareConvertDayService.getNewRechargeCount(params);
        if (newRechargeCount == null) {
            newRechargeCount = 0L;
        }
        infoAll.setNewRechargeCount(newRechargeCount);
        // 新增付费转化周期
        Double newPayCovCycle = datawareConvertDayService.getNewPayCovCycle(params);
        if (newPayCovCycle == null) {
            newPayCovCycle = 0.0;
        }
        infoAll.setNewPayCovCycle(newPayCovCycle);
        // 复购率
        Double rechargeRepRate = datawareConvertDayService.getRechargeRepRate(params);
        if (rechargeRepRate == null) {
            rechargeRepRate = 0.0;
        }
        infoAll.setRechargeRepRate(BigDecimalUtil.mul(rechargeRepRate, 100));
        return infoAll;
    }

    @Async
    public void dataClean(String startTime, String endTime) {
        try {
            if (startTime.equals(endTime)) {
                rechargeInfo(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    rechargeInfo(searchDate);
                }
            }
        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }
}
