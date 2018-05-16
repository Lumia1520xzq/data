package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.constants.CostMonitorConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalActivityCost;
import com.wf.data.dao.mall.entity.ActivityInfo;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareFinalActivityCostService;
import com.wf.data.service.mall.ActivityInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityCostService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataDictService dataDictService;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private InventoryPhyAwardsSendlogService inventoryPhyAwardsSendlogService;
    @Autowired
    private DatawareFinalActivityCostService datawareFinalActivityCostService;

    public void toDoActivityCostAnalysis() {
        logger.info("每天出口成本汇总开始:traceId={}", TraceIdUtils.getTraceId());
        activityCost(DateUtils.getYesterdayDate());
        logger.info("每天出口成本汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void activityCost(String searchDay) {
        Map<String, Object> mapAll = new HashMap<>();
        mapAll.put("businessDate", searchDay);
        long count = datawareFinalActivityCostService.getCountByTime(mapAll);
        if (count > 0) {
            datawareFinalActivityCostService.deleteByDate(mapAll);
        }
        // (value:类型，label:类型名)
        List<DataDict> activitys = dataDictService.findListByType(CostMonitorConstants.ACTIVITY_TYPE);
        List<DatawareFinalActivityCost> costList = Lists.newArrayList();
        try {
            //查询全部
            DatawareFinalActivityCost total = dataCost(mapAll, null, searchDay, null);
            costList.add(total);
            Map<String, Object> map;
            DatawareFinalActivityCost activityCost;
            for (DataDict activity : activitys) {
                map = new HashMap<>();
                map.put("businessDate", searchDay);
                activityCost = dataCost(map, activity, searchDay, total);
                costList.add(activityCost);
                costList.addAll(dataChannelCost(map, activity, searchDay, activityCost, total));
            }
            saveCost(costList);
        } catch (Exception e) {
            logger.error("添加出口成本汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private List<DatawareFinalActivityCost> dataChannelCost(Map<String, Object> params, DataDict activity, String businessDate, DatawareFinalActivityCost activityCost, DatawareFinalActivityCost total) {
        params.put("beginDate", businessDate + " 00:00:00");
        params.put("endDate", businessDate + " 23:59:59");
        params.put("activityType", activity.getValue());
        // 成本
        List<Map<String, Object>> rmbAmount= inventoryPhyAwardsSendlogService.getRmbAmountsByActivity(params);
        Map<Long, Double> cost = new HashMap<>();
        for (Map<String, Object> o : rmbAmount) {
            cost.put(Long.valueOf((o.get("parentId").toString())), Double.valueOf(o.get("rmbAmount").toString()));
        }
        // 出口人数
        List<Map<String, Object>> userCounts = inventoryPhyAwardsSendlogService.getChannelUsersCountByActivity(params);
        Map<Long, Long> counts = new HashMap<>();
        for (Map<String, Object> o : userCounts) {
            counts.put(Long.valueOf((o.get("parentId").toString())), Long.valueOf(o.get("userCount").toString()));
        }
        List<DatawareFinalActivityCost> costs = Lists.newArrayList();
        DatawareFinalActivityCost chCost;
        // 成本
        Double kindCost;
        // 人数
        Long users;
        List<ActivityInfo> channels = activityInfoService.listByActivityType(params);
        Long channel;
        for (ActivityInfo ch : channels) {
            chCost = new DatawareFinalActivityCost();
            BeanUtils.copyProperties(activityCost, chCost);
            channel = ch.getChannelId();
            chCost.setParentId(channel);
            chCost.setChannelName(ch.getName());
            kindCost = cost.get(channel);
            if (null == kindCost)
                kindCost = 0.0;
            chCost.setKindCost(kindCost);
            chCost.setTotalCost(kindCost);
            users = counts.get(channel);
            if (null == users)
                users = 0L;
            chCost.setActivityUsers(users);
            chCost.setCostRate(BigDecimalUtil.mul(division(kindCost, activityCost.getKindCost()), 100));
            if (null != total) {
                // 成本份额占比
                chCost.setCostRate(BigDecimalUtil.mul(division(kindCost, total.getKindCost()), 100));
                // 出口人数份额占比
                chCost.setActivityUserRate(BigDecimalUtil.mul(division(users, total.getActivityUsers()), 100));
            } else {
                chCost.setCostRate(0.00);
                chCost.setActivityUserRate(0.00);
            }
            // 人均出口成本
            chCost.setAvrActivityCost(division(kindCost, users));
            costs.add(chCost);
        }
        return costs;
    }

    private DatawareFinalActivityCost dataCost(Map<String, Object> params, DataDict activity, String businessDate, DatawareFinalActivityCost total) {
        DatawareFinalActivityCost cost = new DatawareFinalActivityCost();
        cost.setBusinessDate(businessDate);
        cost.setParentId(0L);
        cost.setChannelName("");
        if (null == activity) {
            cost.setActivityType(0);
            cost.setActivityName("全部");
            cost.setCostRate(0.00);
            cost.setActivityUserRate(0.00);
            cost.setAvrActivityCost(0.00);
        } else {
            cost.setActivityType(activity.getValue());
            cost.setActivityName(activity.getLabel());
            params.put("activityType", activity.getValue());
        }
        params.put("beginDate", businessDate + " 00:00:00");
        params.put("endDate", businessDate + " 23:59:59");
        // 成本
        Double kindCost = inventoryPhyAwardsSendlogService.getRmbAmountByActivity(params);
        if (null == kindCost) {
            kindCost = 0.00;
        }
        cost.setKindCost(kindCost);
        cost.setTotalCost(kindCost);
        // 出口人数
        Long activityUsers = inventoryPhyAwardsSendlogService.getActivityUsersCount(params);
        if (null == activityUsers) activityUsers = 0L;
        cost.setActivityUsers(activityUsers);
        if (null != total) {
            // 成本份额占比
            cost.setCostRate(BigDecimalUtil.mul(division(kindCost, total.getKindCost()), 100));
            // 出口人数份额占比
            cost.setActivityUserRate(BigDecimalUtil.mul(division(activityUsers, total.getActivityUsers()), 100));
        } else {
            cost.setCostRate(0.00);
            cost.setActivityUserRate(0.00);
        }
        // 人均出口成本
        cost.setAvrActivityCost(division(kindCost, activityUsers));
        return cost;
    }

    private double division(Long d1, Long d2) {
        double result = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            result = BigDecimalUtil.div(d1, d2, 4);
        }
        return result;
    }

    private double division(Double d1, Long d2) {
        double result = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            result = BigDecimalUtil.div(d1, d2, 2);
        }
        return result;
    }

    private double division(Double d1, Double d2) {
        double result = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            result = BigDecimalUtil.div(d1, d2, 4);
        }
        return result;
    }

    private void saveCost(List<DatawareFinalActivityCost> costList) {
        try {
            if (CollectionUtils.isNotEmpty(costList)) {
                datawareFinalActivityCostService.batchSave(costList);
            }
        } catch (Exception e) {
            logger.error("添加出口成本汇总记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(costList.toString()));
        }
    }

    @Async
    public void dataClean(String startTime, String endTime) {
        try {
            if (startTime.equals(endTime)) {
                activityCost(endTime);
            } else {
                for (String searchDate : DateUtils.getDateList(startTime, endTime)) {
                    activityCost(searchDate);
                }
            }
        } catch (Exception e) {
            logger.error("出口成本失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("出口成本老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

}
