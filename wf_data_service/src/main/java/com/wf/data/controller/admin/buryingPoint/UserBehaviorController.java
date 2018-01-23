package com.wf.data.controller.admin.buryingPoint;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.controller.request.UicBehaviorReq;
import com.wf.data.dao.data.entity.UicBehaviorRecord;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.BehaviorTypeService;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.elasticsearch.EsUserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户访问路径
 *
 * @author jianjian.huang
 * @date 2017年9月28日
 */
@RestController
@RequestMapping(value = "/data/admin/userBehavior")
public class UserBehaviorController extends ExtJsController {

    @Autowired
    private EsUserBehaviorService service;
    @Autowired
    private BehaviorTypeService behaviorTypeService;
    @Autowired
    private UicUserService uicUserService;
    @Autowired
    private ChannelInfoService channelInfoService;

    @RequestMapping("/list")
    public Object listData() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Long userId = null;
        String beginDate = null;
        String endDate = null;
        Long parentEventId = null;
        Long eventId = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userId = data.getLong("userId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            parentEventId = data.getLong("parentEventId");
            eventId = data.getLong("eventId");
        }

        String sStart = json.getString("start");
        String sLength = json.getString("limit");
        Integer start = 0;
        Integer length = 0;
        if (sStart != null) {
            start = Integer.parseInt(sStart);
        }
        if (sLength != null) {
            length = Integer.parseInt(sLength);
        }

        UicBehaviorReq req = new UicBehaviorReq();
        req.setParentId(parentId);
        req.setChannelId(channelId);
        req.setUserId(userId);
        req.setBeginDate(beginDate);
        req.setEndDate(endDate);
        req.setParentEventId(parentEventId);
        req.setEventId(eventId);

        Page<UicBehaviorRecord> page = service.findPage(start, length, req);
        List<UicBehaviorRecord> pageData = page.getData();
        for (UicBehaviorRecord temp : pageData) {
            //1、用户昵称
            Long userIdTemp = temp.getUserId();
            UicUser user = uicUserService.getByUserId(userIdTemp);
            if (user != null) {
                temp.setNickname(user.getNickname());
            }
            //2、渠道名称
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("channelId", temp.getChannelId());
            String channelName = channelInfoService.findChannelNameById(params);
            temp.setChannelName(channelName);
            //3、事件名称
            Long eventIdTemp = temp.getBehaviorEventId();
            String eventName = behaviorTypeService.findEventNameById(eventIdTemp);
            temp.setEventName(eventName);
        }
        return dataGrid(page);
    }

}
