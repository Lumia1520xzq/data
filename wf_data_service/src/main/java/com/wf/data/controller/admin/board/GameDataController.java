package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
@RequestMapping(value = "/game/data")
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

        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", startTime);
        map.put("endDate", endTime);

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

        List<String> datelist = DateUtils.getDateList(startTime, endTime);


        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put("gameType", gameType);
                map.put("parentId", parentId);
                DataDict dictDto = dataDictService.getDictByValue("game_type", gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);

                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                for (DatawareFinalGameInfo info : list){

                }

            }

        }
        return data;
    }


}

