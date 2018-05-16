package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.CostMonitorConstants;
import com.wf.data.common.constants.GameDataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalActivityCost;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareFinalActivityCostService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/data/game/cost")
public class CostMonitorController extends ExtJsController {

    @Autowired
    private DatawareFinalChannelCostService datawareFinalChannelCostService;

    @Autowired
    private DatawareFinalActivityCostService datawareFinalActivityCostService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private ChannelInfoService channelInfoService;

    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        // 标签页ID
        String tabId = null;
        // 开始时间
        String startTime = null;
        // 结束时间
        String endTime = null;
        //  主渠道
        List<Long> parentIds = Lists.newArrayList();
        // 出口类型
        List<Integer> activityTypes = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            tabId = data.getString("tabId");
            JSONArray arr = data.getJSONArray(CostMonitorConstants.ACTIVITYTYPE);
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            activityTypes = JSONObject.parseArray(js, Integer.class);
            JSONArray parentIdArr = data.getJSONArray(GameDataConstants.PARENTID);
            String parentIdJs = JSONObject.toJSONString(parentIdArr, SerializerFeature.WriteClassName);
            parentIds = JSONObject.parseArray(parentIdJs, Long.class);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        if (parentIds.size() > 1 && activityTypes.size() > 1) {
            return error("[主渠道]和[出口类型]不能同时多选！");
        }
        Map<String, Object> resultMap;
        // 渠道监控的场合
        if ("channelMonitor".equals(tabId)) {
            // 没有选择主渠道ID的场合
            if (parentIds.isEmpty() && activityTypes.isEmpty()) {
                Map<String, Object> params = new HashMap<>(1);
                // （T-1）日
                params.put("businessDate", DateUtils.getYesterdayDate());
                // 成本TOP5
                List<DatawareFinalChannelCost> channels = datawareFinalChannelCostService.getCostTop5Channel(params);
                for (DatawareFinalChannelCost channel : channels) {
                    parentIds.add(channel.getParentId());
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put(GameDataConstants.BEGINDATE, startTime);
            map.put(GameDataConstants.ENDDATE, endTime);
            if (!parentIds.isEmpty() && activityTypes.isEmpty()) {
                activityTypes.add(0);
            }
            // 渠道监控数据
            resultMap = getChannelMonitorChartsData(parentIds, activityTypes, map);
            map.put("parentIds", parentIds);
            resultMap.put("dateList", datawareFinalChannelCostService.findDateList(map));
        } else if ("activityMonitor".equals(tabId)) {// 出口监控的场合
            if (activityTypes.isEmpty() && parentIds.isEmpty()) {
                Map<String, Object> params = new HashMap<>(1);
                // （T-1）日
                params.put("businessDate", DateUtils.getYesterdayDate());
                List<DatawareFinalActivityCost> top5Activity = datawareFinalActivityCostService.getCostTop5Activity(params);
                for (DatawareFinalActivityCost activityCost : top5Activity) {
                    activityTypes.add(activityCost.getActivityType());
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put(GameDataConstants.BEGINDATE, startTime);
            map.put(GameDataConstants.ENDDATE, endTime);
            if (!activityTypes.isEmpty() && parentIds.isEmpty()) {
                parentIds.add(0L);
            }
            // 渠道监控数据
            resultMap = getActivityMonitorChartsData(parentIds, activityTypes, map);
            map.put("activityTypes", activityTypes);
            resultMap.put("dateList", datawareFinalActivityCostService.findDateList(map));
        } else {
            resultMap = new HashMap<>();
        }
        return resultMap;
    }

    private Map<String, Object> getActivityMonitorChartsData(List<Long> parentIds, List<Integer> activityTypes, Map<String, Object> map) {
        // 图例
        List<String> legends = Lists.newArrayList();
        // 出口成本
        List<Object> costs = Lists.newArrayList();
        // 成本份额占比
        List<Object> costRates = Lists.newArrayList();
        // 出口人数
        List<Object> activityUsers = Lists.newArrayList();
        // 出口人数份额占比
        List<Object> activityRates = Lists.newArrayList();
        // 人均出口成本
        List<Object> avrActivityCosts = Lists.newArrayList();
        List<DatawareFinalActivityCost> costInfo;
        if (activityTypes.isEmpty() && parentIds.size() >= 1) {
            // 欢乐套圈
            activityTypes.add(3);
        }
        for (Integer activityType : activityTypes) {
            map.put(CostMonitorConstants.ACTIVITYTYPE, activityType);
            for (Long parentId : parentIds) {
                map.put("parentId", parentId);
                costInfo = datawareFinalActivityCostService.getActivityCostInfo(map);
                legends.add(this.getLegend(activityType, parentId));
                costs.add(getCosts(costInfo));
                costRates.add(getCostRates(costInfo));
                activityUsers.add(getActivityUsers(costInfo));
                activityRates.add(getActivityRates(costInfo));
                avrActivityCosts.add(getActivityCosts(costInfo));
            }
        }
        map.remove("parentId");
        map.remove(CostMonitorConstants.ACTIVITYTYPE);
        Map<String, Object> seriesMap = new HashMap<>();
        seriesMap.put(CostMonitorConstants.ACTIVITYCOST, costs);
        seriesMap.put(CostMonitorConstants.ACTIVITYCOSTRATE, costRates);
        seriesMap.put(CostMonitorConstants.PERACTIVITYUSERS, activityUsers);
        seriesMap.put(CostMonitorConstants.ACTIVITYUSERSRATE, activityRates);
        seriesMap.put(CostMonitorConstants.ACTIVITYAVRCOST, avrActivityCosts);

        Map<String, Object> dataMap = new HashMap<>();
        // 图例
        dataMap.put(GameDataConstants.LEGENDS, legends);
        dataMap.put(GameDataConstants.SERIES, seriesMap);

        List<String> titles = Lists.newArrayList();
        titles.add(CostMonitorConstants.ACTIVITYCOST);
        titles.add(CostMonitorConstants.ACTIVITYCOSTRATE);
        titles.add(CostMonitorConstants.PERACTIVITYUSERS);
        titles.add(CostMonitorConstants.ACTIVITYUSERSRATE);
        titles.add(CostMonitorConstants.ACTIVITYAVRCOST);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(GameDataConstants.TITLES, titles);
        resultMap.put(GameDataConstants.CHARTSDATA, dataMap);
        return resultMap;
    }

    private List<Object> getCosts(List<DatawareFinalActivityCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalActivityCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getKindCost()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getCostRates(List<DatawareFinalActivityCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalActivityCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getCostRate()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getActivityUsers(List<DatawareFinalActivityCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalActivityCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getActivityUsers()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getActivityRates(List<DatawareFinalActivityCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalActivityCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getActivityUserRate()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getActivityCosts(List<DatawareFinalActivityCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalActivityCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getAvrActivityCost()));
            rs.add(lString);
        }
        return rs;
    }

    /**
     * 渠道监控数据
     * @param parentIds
     * @param activityTypes
     * @param map
     * @return
     */
    private Map<String, Object> getChannelMonitorChartsData(List<Long> parentIds, List<Integer> activityTypes, Map<String, Object> map) {
        // 图例
        List<String> legends = Lists.newArrayList();
        // 成本
        List<Object> costs = Lists.newArrayList();
        // 成本占比
        List<Object> costRates = Lists.newArrayList();
        List<Object> activityUsers = Lists.newArrayList();
        List<Object> activityRates = Lists.newArrayList();
        List<Object> avrActivityCosts = Lists.newArrayList();
        List<DatawareFinalChannelCost> costInfo;
        if (activityTypes.size() >= 1 && parentIds.isEmpty()) {
            // 奖多多渠道
            parentIds.add(100001L);
        }
        for (Long parentId : parentIds) {
            map.put("parentId", parentId);
            for (Integer activityType : activityTypes) {
                map.put(CostMonitorConstants.ACTIVITYTYPE, activityType);
                costInfo = datawareFinalChannelCostService.getCostInfo(map);
                legends.add(this.getLegend(activityType, parentId));
                costs.add(getChannelCosts(costInfo));
                costRates.add(getChannelCostRates(costInfo));
                activityUsers.add(getChannelActivityUsers(costInfo));
                activityRates.add(getChannelActivityRates(costInfo));
                avrActivityCosts.add(getChannelActivityCosts(costInfo));
            }
        }
        map.remove("parentId");
        map.remove(CostMonitorConstants.ACTIVITYTYPE);

        Map<String, Object> seriesMap = new HashMap<>();
        seriesMap.put(CostMonitorConstants.COST, costs);
        seriesMap.put(CostMonitorConstants.COSTRATE, costRates);
        seriesMap.put(CostMonitorConstants.ACTIVITYUSERS, activityUsers);
        seriesMap.put(CostMonitorConstants.ACTIVITYRATE, activityRates);
        seriesMap.put(CostMonitorConstants.AVRACTIVITYCOST, avrActivityCosts);

        Map<String, Object> dataMap = new HashMap<>();
        // 图例
        dataMap.put(GameDataConstants.LEGENDS, legends);
        dataMap.put(GameDataConstants.SERIES, seriesMap);

        List<String> titles = Lists.newArrayList();
        titles.add(CostMonitorConstants.COST);
        titles.add(CostMonitorConstants.COSTRATE);
        titles.add(CostMonitorConstants.ACTIVITYUSERS);
        titles.add(CostMonitorConstants.ACTIVITYRATE);
        titles.add(CostMonitorConstants.AVRACTIVITYCOST);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(GameDataConstants.TITLES, titles);
        resultMap.put(GameDataConstants.CHARTSDATA, dataMap);
        return resultMap;
    }

    private List<Object> getChannelCosts(List<DatawareFinalChannelCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalChannelCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getKindCost()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getChannelCostRates(List<DatawareFinalChannelCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalChannelCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getCostRate()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getChannelActivityUsers(List<DatawareFinalChannelCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalChannelCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getActivityUsers()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getChannelActivityRates(List<DatawareFinalChannelCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalChannelCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getActivityRate()));
            rs.add(lString);
        }
        return rs;
    }

    private List<Object> getChannelActivityCosts(List<DatawareFinalChannelCost> costs) {
        List<String> lString;
        List<Object> rs = Lists.newArrayList();
        for (DatawareFinalChannelCost cost : costs) {
            lString = Lists.newArrayList();
            lString.add(String.valueOf(cost.getAvrActivityCost()));
            rs.add(lString);
        }
        return rs;
    }

    /**
     * 根据出口类型和渠道获取图例
     *
     * @param activityType
     * @param parentId
     * @return
     */
    private String getLegend(int activityType, Long parentId) {
        DataDict dictDto = dataDictService.getDictByValue(CostMonitorConstants.ACTIVITY_TYPE, activityType);
        if (null == dictDto) {
            dictDto = new DataDict();
            dictDto.setLabel("全部");
        }
        ChannelInfo channelInfo = channelInfoService.get(parentId);
        if (null == channelInfo) {
            channelInfo = new ChannelInfo();
            channelInfo.setName("全部");
        }
        return channelInfo.getName() + " - " + dictDto.getLabel();
    }
}

