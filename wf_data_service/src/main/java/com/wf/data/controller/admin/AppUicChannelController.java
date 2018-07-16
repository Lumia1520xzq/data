package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.appuic.entity.AppUicChannelInfo;
import com.wf.data.service.appuic.AppUicChannelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据接口
 *
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/appuic/channel")
public class AppUicChannelController extends ExtJsController {

    @Autowired
    private AppUicChannelInfoService appUicChannelInfoService;

    /**
     * 获取所有的渠道
     *
     * @return
     */
    @RequestMapping("/getAllChannels")
    public Object getAllChannels() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        AppUicChannelInfo dto = new AppUicChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }

        List<AppUicChannelInfo> list = appUicChannelInfoService.findList(dto, 1000);

        for (AppUicChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    /**
     * 获取主渠道
     *
     * @return
     */
    @RequestMapping("/getParentChannels")
    public Object getParentChannels() {
        JSONObject json = getRequestJson();
        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        AppUicChannelInfo dto = new AppUicChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        dto.setEnable(1);
        dto.setMainChannel(1L);
        List<AppUicChannelInfo> list = appUicChannelInfoService.findList(dto, 1000);

        for (AppUicChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    /**
     * 获取子渠道
     *
     * @return
     */
    @RequestMapping("/getChildChannels")
    public Object getChildChannels() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("parentId");
        }
        AppUicChannelInfo dto = new AppUicChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setParentId(Long.parseLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        if (data != null) {
            String channelName = data.getString("name");
            if(StringUtils.isNotBlank(channelName)){
                dto.setName(channelName);
            }
        }
        dto.setEnable(1);
        List<AppUicChannelInfo> list = appUicChannelInfoService.findList(dto, 1000);
        if (null != dto.getParentId()) {
            AppUicChannelInfo info = appUicChannelInfoService.get(dto.getParentId());
            list.add(0, info);
        }
        for (AppUicChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }

        return list;
    }


    /**
     * 获取子渠道
     *
     * @return
     */
    @RequestMapping("/listChildChannel")
    public Object listChildChannel() {
        JSONObject json = getRequestJson();

        List<Long> parentId = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            String js = JSONObject.toJSONString(data.getJSONArray("parentId"),
                    SerializerFeature.WriteClassName);
            parentId = JSONObject.parseArray(js, Long.class);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("parentIds", parentId);
        List<AppUicChannelInfo> list = appUicChannelInfoService.listSubChannel(params);
        if (null != parentId && !parentId.isEmpty()) {
            for (Long pId : parentId) {
                AppUicChannelInfo info = appUicChannelInfoService.get(pId);
                list.add(0, info);
            }
        }
        for (AppUicChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }

        return list;
    }



}
