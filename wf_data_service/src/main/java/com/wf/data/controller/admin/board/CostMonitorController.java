package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.CostMonitorConstants;
import com.wf.data.common.constants.GameDataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalActivityCost;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dto.UserDetailsDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareFinalActivityCostService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private InventoryPhyAwardsSendlogService inventoryPhyAwardsSendlogService;

    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    @Autowired
    private UicUserService uicUserService;

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
        // 用户ID
        Long userId = null;
        Long start = null;
        Long limit = null;
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
            userId = data.getLong("userId");
            start = json.getLongValue("start");
            limit = json.getLongValue("limit");
        }
        Map<String, Object> resultMap;
        // 渠道监控的场合
        if (CostMonitorConstants.CHANNELMONITOR.equals(tabId)) {
            if (parentIds.size() > 1 && activityTypes.size() > 1) {
                return error("[主渠道]和[出口类型]不能同时多选！");
            }
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
        } else if (CostMonitorConstants.ACTIVITYMONITOR.equals(tabId)) {// 出口监控的场合
            if (parentIds.size() > 1 && activityTypes.size() > 1) {
                return error("[主渠道]和[出口类型]不能同时多选！");
            }
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
        } else if (CostMonitorConstants.USERDETAILS.equals(tabId)) {// 用户明细的场合
            // 如果用户ID为空的场合
            if (userId ==null) {
                Map<String, Object> map = new HashMap<>(2);
                map.put(GameDataConstants.BEGINDATE, endTime + " 00:00:00");
                map.put(GameDataConstants.ENDDATE, endTime + " 23:59:59");
                // 获取指定日期实物成本最大的用户
                userId = inventoryPhyAwardsSendlogService.getMaxCostUserId(map);
            }
            Page<UserDetailsDto> p = new Page();
            p.setStart(start);
            p.setLength(limit);
            p.setData(getUserDetails(userId, startTime, endTime));
            return dataGrid(p);
        } else {
            resultMap = new HashMap<>();
        }
        return resultMap;
    }

    private List<UserDetailsDto> getUserDetails(Long userId, String startTime, String endTime) {
        if (userId == null)
            return Lists.newArrayList();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put(GameDataConstants.BEGINDATE, startTime + " 00:00:00");
        params.put(GameDataConstants.ENDDATE, endTime + " 23:59:59");
        // 投注、盈利、游戏等信息
        List<UserDetailsDto> cmUserDetails = datawareBettingLogDayService.getCMUserDetails(params);
        if (null == cmUserDetails)
            return Lists.newArrayList();
        // 用户信息
        UicUser uicUser = uicUserService.get(userId);
        // 成本信息
        List<Map<String, Object>> userCosts = inventoryPhyAwardsSendlogService.getUserCostPerDay(params);
        Map<String, Double> dayCostMap = new HashMap<>();
        if (null != userCosts) {
            for (Map<String, Object> userCost : userCosts) {
                dayCostMap.put(userCost.get("sendDate").toString(), Double.valueOf(userCost.get("rmbAmount").toString()));
            }
        }
        Double kindCost;
        // 补充成本信息
        for (UserDetailsDto cmUserDetail : cmUserDetails) {
            // 用户昵称
            cmUserDetail.setUserName(uicUser.getNickname());
            kindCost = dayCostMap.get(cmUserDetail.getBusinessDate());
            if (kindCost == null) {
                kindCost = 0.0;
            }
            // 成本
            cmUserDetail.setKindCost(kindCost);
            // 成本占比(成本/充值金额)
            cmUserDetail.setCostRate(BigDecimalUtil.mul(division(kindCost, cmUserDetail.getRechargeAmount()), 100));
            // 返奖率（返奖金额/投注金额）
            cmUserDetail.setReturnRate(BigDecimalUtil.mul(division(cmUserDetail.getResultAmount(), cmUserDetail.getBettingAmount()), 100));
            // S3
            cmUserDetail.setNoUseGoldAmount(division(cmUserDetail.getNoUseGoldAmount(), 1000l));
        }
        return cmUserDetails;
    }

    private double division(Double d1, Double d2) {
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

    /**
     * 导出数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String tabId, @RequestParam String userId,
                           @RequestParam String startTime, @RequestParam String endTime, HttpServletResponse response) {
        // 用户明细的场合
        if (CostMonitorConstants.USERDETAILS.equals(tabId)) {
            List<UserDetailsDto> list = getUserDetails(Long.valueOf(userId), startTime, endTime);
            if (!list.isEmpty()) {
                ChannelInfo cInfo = channelInfoService.get(list.get(0).getChannelId());
                for (UserDetailsDto dto : list) {
                    dto.setChannelName(cInfo.getName() + "(" + cInfo.getId() + ")");
                }
            }
            try {
                String fileName = "用户明细" + com.wf.data.common.utils.DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
                new ExportExcel("成本监控--用户明细表", UserDetailsDto.class).setDataList(list).write(response, fileName).dispose();
            } catch (Exception e) {
                logger.error("导出失败：" + e.getMessage());
            }
        }
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
            lString.add(String.valueOf(BigDecimalUtil.round(BigDecimalUtil.mul(cost.getCostRate(), 100), 2)));
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

