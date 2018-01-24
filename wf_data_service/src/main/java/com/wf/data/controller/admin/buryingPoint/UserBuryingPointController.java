package com.wf.data.controller.admin.buryingPoint;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.controller.request.BehaviorRecordReq;
import com.wf.data.dao.data.entity.BehaviorRecordResp;
import com.wf.data.service.elasticsearch.EsUicBehaviorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shihui
 * @date 2018/1/23
 */

@RestController
@RequestMapping("/data/admin/userBuryingPoint")
public class UserBuryingPointController extends ExtJsController {

    @Autowired
    private EsUicBehaviorRecordService behaviorRecordService;

    @RequestMapping("/list")
    public Object listData() {
        JSONObject json = getRequestJson();

        Long parentId = null;
        Long channelId = null;
        Long parentEventId = null;
        Long eventId = null;
        String beginDate = null;
        String endDate = null;
        Integer userType = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            parentEventId = data.getLong("parentEventId");
            eventId = data.getLong("eventId");
            userType = data.getInteger("userType");
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

        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.getYesterdayDate();
            endDate = DateUtils.getYesterdayDate();
        }

        //设置用户类型默认为所有用户
        if (userType == null) {
            userType = 1;
        }

        BehaviorRecordReq req = new BehaviorRecordReq();
        req.setParentId(parentId);
        req.setChannelId(channelId);
        req.setParentEventId(parentEventId);
        req.setEventId(eventId);
        req.setBeginDate(beginDate);
        req.setEndDate(endDate);
        req.setUserType(userType);

        Page<BehaviorRecordResp> page = new Page<BehaviorRecordResp>();
        List<BehaviorRecordResp> behaviorList = new ArrayList<BehaviorRecordResp>();

        if (req.getUserType() == 1) {
            behaviorList = behaviorRecordService.getBehaviorList(req);
        } else if (req.getUserType() == 2) {
            behaviorList = behaviorRecordService.getNewBehaviorList(req);
        }

        int total = behaviorList.size();
        int fromIndex = start;
        int toIndex = (start / length + 1) * length;
        if (fromIndex > total)
            return null;
        if (toIndex > total)
            toIndex = total;
        List<BehaviorRecordResp> list = behaviorList.subList(fromIndex, toIndex);
        page.setData(list);
        page.setiTotalRecords(list.size());
        page.setCount(behaviorList.size());

        return dataGrid(page);
    }
}
