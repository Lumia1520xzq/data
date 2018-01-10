package com.wf.data.task.dataware;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.mall.ActivityInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import com.wf.data.service.trans.TransFragmentConvertRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
public class ChannelCostJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);
    private final DatawareConvertDayService datawareConvertDayService = SpringContextHolder.getBean(DatawareConvertDayService.class);
    private final DatawareFinalChannelCostService datawareFinalChannelCostService = SpringContextHolder.getBean(DatawareFinalChannelCostService.class);
    private final ActivityInfoService activityInfoService = SpringContextHolder.getBean(ActivityInfoService.class);
    private final TransFragmentConvertRecordService transFragmentConvertRecordService = SpringContextHolder.getBean(TransFragmentConvertRecordService.class);
    private final InventoryPhyAwardsSendlogService inventoryPhyAwardsSendlogService = SpringContextHolder.getBean(InventoryPhyAwardsSendlogService.class);


    public void execute() {
        logger.info("每天成本汇总开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DESTINATION_COST_FLAG);
        if (false == flag) {
            historyChannelCost();
        } else {
            String yesterdayDate = DateUtils.getYesterdayDate();
            channelCost(yesterdayDate);
        }

        logger.info("每天成本汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void historyChannelCost() {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COST_DATE);

        if (StringUtils.isBlank(date)) {
            logger.error("清洗时间未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }

        String[] dates = date.split(",");
        if (StringUtils.isBlank(dates[0])) {
            logger.error("清洗开始时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }
        if (StringUtils.isBlank(dates[1])) {
            logger.error("清洗结束时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }
        String startDay = dates[0].trim();
        String endDay = dates[1].trim();
        try {

            if (startDay.equals(endDay)) {
                channelCost(startDay);
            } else {
                List<String> datelist = DateUtils.getDateList(startDay, endDay);
                for (String searchDate : datelist) {
                    channelCost(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }


    private void channelCost(String searchDay) {
        Map<String, Object> mapAll = new HashMap<>();
        mapAll.put("businessDate", searchDay);
        long count = datawareFinalChannelCostService.getCountByTime(mapAll);
        if (count > 0) {
            logger.info("cost数据已存在: traceId={},date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(searchDay));
            return;
        }

        List<DatawareFinalChannelCost> costList = Lists.newArrayList();

        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        List<String> channels = Arrays.asList(channelIdList.split(","));
        try {
            //查询全部
            DatawareFinalChannelCost cost = dataCost(mapAll, null, searchDay);

            //查询各个渠道
            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("parentId", channelInfo.getParentId());
                        map.put("channelId", channel);
                        DatawareFinalChannelCost channelCost = dataCost(map, channelInfo, searchDay);
                        costList.add(channelCost);
                    } else {
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);
                        DatawareFinalChannelCost channelCost = dataCost(parentMap, channelInfo, searchDay);
                        costList.add(channelCost);
                    }
                }

            }

            costList.add(getOtherCost(cost, costList));
            costList.add(cost);
            saveCost(costList);
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


    private DatawareFinalChannelCost dataCost(Map<String, Object> params, ChannelInfo channelInfo, String businessDate) {

        DatawareFinalChannelCost channelCost = new DatawareFinalChannelCost();
        channelCost.setBusinessDate(businessDate);
        if (null == channelInfo) {
            channelCost.setChannelId(1L);
            channelCost.setParentId(1L);
            channelCost.setChannelName("全部");
        } else {
            channelCost.setChannelName(channelInfo.getName());
            channelCost.setChannelId(channelInfo.getId());
            if (null == channelInfo.getParentId()) {
                channelCost.setParentId(channelInfo.getId());
            } else {
                channelCost.setParentId(channelInfo.getParentId());
            }
        }

        //充值
        DatawareFinalChannelInfoAll convertInfo = datawareConvertDayService.getConvertByDate(params);
        if (null != convertInfo) {
            if (null == convertInfo.getRechargeAmount()) {
                channelCost.setRechargeAmount(0.00);
            } else {
                channelCost.setRechargeAmount(convertInfo.getRechargeAmount());
            }
        }

        //碎片
        String beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(businessDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
        String endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(businessDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        Double fragmentCost = transFragmentConvertRecordService.getFragmentCostByDate(params);
        if (null == fragmentCost) {
            fragmentCost = 0.00;
        }
        channelCost.setFragmentCost(fragmentCost);


        List<Long> activityIds = activityInfoService.getListByChannelId(params);
        params.put("activityIds", activityIds);
        Double kindCost = inventoryPhyAwardsSendlogService.getRmbAmountByChannel(params);
        if(null == kindCost){
            kindCost = 0.00;
        }

        channelCost.setKindCost(kindCost);
        channelCost.setTotalCost(BigDecimalUtil.add(kindCost, fragmentCost));
        //成本占比=当日成本/当日充值金额
        if (channelCost.getRechargeAmount() > 0) {
            channelCost.setCostRate(BigDecimalUtil.div(channelCost.getTotalCost(), channelCost.getRechargeAmount(), 2));
        } else {
            channelCost.setCostRate(0.00);
        }

        return channelCost;

    }

    /**
     * 计算其他渠道数据
     *
     * @param totalchannelCost
     * @param channelCostList
     * @return
     */
    private DatawareFinalChannelCost getOtherCost(DatawareFinalChannelCost totalchannelCost, List<DatawareFinalChannelCost> channelCostList) {
        DatawareFinalChannelCost channelCost = new DatawareFinalChannelCost();
        double rechargeAmount = 0.00;
        double fragmentCost = 0.00;
        double kindCost = 0.00;
        double totalCost = 0.00;

        for (DatawareFinalChannelCost item : channelCostList) {
            rechargeAmount += item.getRechargeAmount();
            fragmentCost += item.getFragmentCost();
            kindCost += item.getKindCost();
            totalCost += item.getTotalCost();
        }

        channelCost.setRechargeAmount(BigDecimalUtil.sub(totalchannelCost.getRechargeAmount(), rechargeAmount));
        channelCost.setFragmentCost(BigDecimalUtil.sub(totalchannelCost.getFragmentCost(), fragmentCost));
        channelCost.setKindCost(BigDecimalUtil.sub(totalchannelCost.getKindCost(), kindCost));
        channelCost.setTotalCost(BigDecimalUtil.sub(totalchannelCost.getTotalCost(), totalCost));
        if (channelCost.getRechargeAmount() > 0) {
            channelCost.setCostRate(BigDecimalUtil.div(channelCost.getTotalCost() * 100, channelCost.getRechargeAmount(), 2));
        } else {
            channelCost.setCostRate(0.00);
        }
        channelCost.setChannelName("其他");
        channelCost.setChannelId(0L);
        channelCost.setParentId(0L);
        channelCost.setBusinessDate(totalchannelCost.getBusinessDate());
        return channelCost;
    }

    private void saveCost(List<DatawareFinalChannelCost> costList) {

        try {
            if (CollectionUtils.isNotEmpty(costList)) {
                datawareFinalChannelCostService.batchSave(costList);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(costList.toString()));
        }
    }


}
