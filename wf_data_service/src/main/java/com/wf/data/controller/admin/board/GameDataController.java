package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareFinalGameInfoService;
import jodd.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcs
 * 2018/04/02
 */
@RestController
@RequestMapping(value = "/data/game/data")
public class GameDataController extends ExtJsController {

    @Autowired
    private DatawareFinalGameInfoService datawareFinalGameInfoService;
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
        String tabId = null;
        List<Long> parentIds = null;
        List<Integer> gameTypes = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            tabId = data.getString("tabId");

            JSONArray arr = data.getJSONArray("gameType");
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            gameTypes = JSONObject.parseArray(js, Integer.class);

            JSONArray parentIdArr = data.getJSONArray("parentId");
            String parentIdJs = JSONObject.toJSONString(parentIdArr, SerializerFeature.WriteClassName);
            parentIds = JSONObject.parseArray(parentIdJs, Long.class);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        if (CollectionUtils.isEmpty(parentIds)) {
            //默认显示全渠道
            parentIds.add(1L);
        }
        if (CollectionUtils.isEmpty(gameTypes)) {
            //默认显示捕鱼游戏
            gameTypes.add(10);
        }
        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDate(startTime).getTime() >= DateUtils.parseDate(endTime).getTime()) {
            return error("开始时间大于等于结束时间");
        }
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", startTime);
        map.put("endDate", endTime);
        List<String> datelist = DateUtils.getDateList(startTime, endTime);


        List<String> titles = Lists.newArrayList();
        resultMap.put("dateList", datelist);
        if ("allUsers".equals(tabId)) {
            titles.add("DAU");
            titles.add("投注人数");
            titles.add("投注转化率");
            titles.add("投注流水");
            titles.add("流水差");
            titles.add("返奖率");
            titles.add("投注笔数");
            titles.add("投注ARPU");
            titles.add("投注ASP");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("newUsers".equals(tabId)) {
            titles.add("新增用户数");
            titles.add("新增投注用户数");
            titles.add("新增投注转化率");
            titles.add("投注流水");
            titles.add("流水差");
            titles.add("返奖率");
            titles.add("投注笔数");
            titles.add("投注ARPU");
            titles.add("投注ASP");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("retention".equals(tabId)) {
            titles.add("新增次留");
            titles.add("新增三留");
            titles.add("新增七留");
            titles.add("全量次留");
            titles.add("全量三留");
            titles.add("全量七留");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("other".equals(tabId)) {
            titles.add("导入率");
            titles.add("累计用户数");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        }


        return resultMap;
    }

    private Map<String, Object> getChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tabId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> legends = Lists.newArrayList();
        List<Object> series = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put("gameType", gameType);
                map.put("parentId", parentId);
                DataDict dictDto = dataDictService.getDictByValue("game_type", gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if(null == channelDto){
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                series.add(getLinesData(list, tabId));

            }

        }
        resultMap.put("legends", legends);
        resultMap.put("series", series);
        return resultMap;
    }

    private List<Object> getLinesData(List<DatawareFinalGameInfo> list, String tabId) {
        List<Object> resultList = Lists.newArrayList();
        List<Object> dauList = Lists.newArrayList();
        /*List<Object> userCountList = Lists.newArrayList();
        List<Object> conversionList = Lists.newArrayList();
        List<Object> bettingAmountList = Lists.newArrayList();
        List<Object> returnAmountList = Lists.newArrayList();
        List<Object> diffAmountList = Lists.newArrayList();
        List<Object> returnRateList = Lists.newArrayList();
        List<Object> bettingCountList = Lists.newArrayList();
        List<Object> bettingArpuList = Lists.newArrayList();
        List<Object> bettingAspList = Lists.newArrayList();
        List<Object> oneDayRetentionList = Lists.newArrayList();
        List<Object> threeDayRetentionList = Lists.newArrayList();
        List<Object> sevenDayRetentionList = Lists.newArrayList();
        List<Object> newUserCountList = Lists.newArrayList();
        List<Object> newUserBettingUserCountList = Lists.newArrayList();
        List<Object> newUserBettingConversionList = Lists.newArrayList();
        List<Object> newUserReturnAmountList = Lists.newArrayList();
        List<Object> newUserBettingAmountList = Lists.newArrayList();
        List<Object> newUserDiffAmountList = Lists.newArrayList();
        List<Object> newUserReturnRateList = Lists.newArrayList();
        List<Object> newUserBettingCountList = Lists.newArrayList();
        List<Object> newUserBettingArpuList = Lists.newArrayList();
        List<Object> newUserBettingAspList = Lists.newArrayList();
        List<Object> newUserOneDayRetentionList = Lists.newArrayList();
        List<Object> newUserThreeDayRetentionList = Lists.newArrayList();
        List<Object> newUserSevenDayRetentionList = Lists.newArrayList();
        List<Object> importRate = Lists.newArrayList();*/

        for (DatawareFinalGameInfo info : list) {
            if ("allUsers".equals(tabId)) {
                dauList.add(info.getDau());
            } else if ("newUsers".equals(tabId)) {
            } else if ("retention".equals(tabId)) {
            } else if ("other".equals(tabId)) {
            }
        }
        resultList.add(dauList);
        return resultList;
    }


}

