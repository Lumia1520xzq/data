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
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.mall.entity.ActivityInfo;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.mall.ActivityInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import com.wf.data.service.trans.TransFragmentConvertRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
@Service
public class ChannelCostService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;
    @Autowired
    private DatawareFinalChannelCostService datawareFinalChannelCostService;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private TransFragmentConvertRecordService transFragmentConvertRecordService;
    @Autowired
    private InventoryPhyAwardsSendlogService inventoryPhyAwardsSendlogService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;

    public void toDoChannelCostAnalysis() {
        logger.info("每天成本汇总开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DESTINATION_COST_FLAG);
        if (true == flag) {
            String yesterdayDate = DateUtils.getYesterdayDate();
            channelCost(yesterdayDate);
        }

        logger.info("每天成本汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void channelCost(String searchDay) {
        Map<String, Object> mapAll = new HashMap<>();
        mapAll.put("businessDate", searchDay);
        long count = datawareFinalChannelCostService.getCountByTime(mapAll);
        if (count > 0) {
            datawareFinalChannelCostService.deleteByDate(mapAll);
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
                        // 渠道-出口类型
                        costList.addAll(dataActivityCost(map, searchDay, channelCost));
                    } else {
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);
                        DatawareFinalChannelCost channelCost = dataCost(parentMap, channelInfo, searchDay);
                        costList.add(channelCost);
                        // 渠道-出口类型
                        costList.addAll(dataActivityCost(parentMap, searchDay, channelCost));
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

    private List<DatawareFinalChannelCost> dataActivityCost(Map<String, Object> params, String businessDate, DatawareFinalChannelCost channelCost) {
        params.put("beginDate", businessDate + " 00:00:00");
        params.put("endDate", businessDate + " 23:59:59");
        // 成本
        List<Map<String, Object>> rmbAmount= inventoryPhyAwardsSendlogService.getRmbAmountByChannelAndActivity(params);
        Map<Integer, Double> cost = new HashMap<>();
        for (Map<String, Object> o : rmbAmount) {
            cost.put(Integer.valueOf((o.get("activityType").toString())), Double.valueOf(o.get("rmbAmount").toString()));
        }
        // 出口人数
        List<Map<String, Object>> userCounts = inventoryPhyAwardsSendlogService.getActivityUsersCountByChannel(params);
        Map<Integer, Long> counts = new HashMap<>();
        for (Map<String, Object> o : userCounts) {
            counts.put(Integer.valueOf((o.get("activityType").toString())), Long.valueOf(o.get("userCount").toString()));
        }
        List<DatawareFinalChannelCost> costs = Lists.newArrayList();
        DatawareFinalChannelCost acCost;
        // 成本
        Double kindCost;
        // 人数
        Long users;
        List<ActivityInfo> types = activityInfoService.listByChannelId(params);
        Integer type;
        for (ActivityInfo ac : types) {
            acCost = new DatawareFinalChannelCost();
            BeanUtils.copyProperties(channelCost, acCost);
            type = ac.getActivityType();
            acCost.setActivityType(type);
            acCost.setActivityName(ac.getName());
            kindCost = cost.get(type);
            if (null == kindCost)
                kindCost = 0.0;
            acCost.setKindCost(kindCost);
            acCost.setTotalCost(kindCost);
            users = counts.get(type);
            if (null == users)
                users = 0L;
            acCost.setActivityUsers(users);
            acCost.setCostRate(division(kindCost, acCost.getRechargeAmount()));
            acCost.setActivityRate(BigDecimalUtil.mul(division(users, acCost.getDau()), 100));
            acCost.setAvrActivityCost(division(kindCost, users));
            costs.add(acCost);
        }
        return costs;
    }

    private DatawareFinalChannelCost dataCost(Map<String, Object> params, ChannelInfo channelInfo, String businessDate) {

        DatawareFinalChannelCost channelCost = new DatawareFinalChannelCost();
        channelCost.setBusinessDate(businessDate);
        channelCost.setActivityName("");
        channelCost.setActivityType(0);
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
        if(CollectionUtils.isEmpty(activityIds)){
            channelCost.setKindCost(0.00);
            channelCost.setTotalCost(0.00);
            channelCost.setCostRate(0.00);
            channelCost.setDau(0L);
            channelCost.setActivityRate(0.00);
            channelCost.setActivityUsers(0L);
            channelCost.setAvrActivityCost(0.00);
            return channelCost;
        }
        params.put("activityIds", activityIds);
        // 根据activityIds获取成本（渠道成本）
        Double kindCost = inventoryPhyAwardsSendlogService.getRmbAmountByChannel(params);
        if (null == kindCost) {
            kindCost = 0.00;
        }

        channelCost.setKindCost(kindCost);
        channelCost.setTotalCost(kindCost);
        //成本占比=当日成本/当日充值金额
        if (channelCost.getRechargeAmount() > 0) {
            channelCost.setCostRate(division(channelCost.getTotalCost(), channelCost.getRechargeAmount()));
        } else {
            channelCost.setCostRate(0.00);
        }
        // 日活
        Long dau = datawareBuryingPointDayService.getDauByChannel(params);
        if (null == dau) dau = 0L;
        channelCost.setDau(dau);
        // 出口人数
        Long activityUsers = inventoryPhyAwardsSendlogService.getActivityUsersCount(params);
        if (null == activityUsers) activityUsers = 0L;
        channelCost.setActivityUsers(activityUsers);
        // 出口率
        channelCost.setActivityRate(BigDecimalUtil.mul(division(activityUsers, dau), 100));
        // 人均出口成本
        channelCost.setAvrActivityCost(division(kindCost, activityUsers));
        return channelCost;
    }

    private double division(Long d1, Long d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 4);
        }
        return resulet;
    }

    private double division(Double d1, Long d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 2);
        }
        return resulet;
    }

    private double division(Double d1, Double d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 4);
        }
        return resulet;
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
        // TODO
        channelCost.setDau(0L);
        channelCost.setActivityUsers(0L);
        channelCost.setAvrActivityCost(0.00);
        channelCost.setActivityRate(0.00);
        channelCost.setActivityType(0);
        channelCost.setActivityName("");
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


    @Async
    public void dataClean(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                channelCost(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    channelCost(searchDate);
                }
            }
        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

}
