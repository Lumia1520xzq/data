package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wf.core.web.base.ExtJsController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lcs
 * 2018/04/02
 */
@RestController
@RequestMapping(value = "/game/data")
public class GameDataController extends ExtJsController {


    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        String tabId = null;
        List<Long> parentId = null;
        List<Long> gameType = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            tabId = data.getString("tabId");

            JSONArray arr = data.getJSONArray("gameType");
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            gameType = JSONObject.parseArray(js, Long.class);

            JSONArray parentIdArr = data.getJSONArray("parentId");
            String parentIdJs = JSONObject.toJSONString(parentIdArr, SerializerFeature.WriteClassName);
            parentId = JSONObject.parseArray(parentIdJs, Long.class);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        System.out.println(tabId);
        System.out.println(gameType.toString());
        System.out.println(parentId.toString());
        System.out.println(startTime);
        System.out.println(endTime);
        return data;
    }


}

