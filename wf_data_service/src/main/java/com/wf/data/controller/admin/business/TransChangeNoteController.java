package com.wf.data.controller.admin.business;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareThirdBettingRecord;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.elasticsearch.EsTransChangeNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JoeH
 * 2018/01/19
 */
@RestController
@RequestMapping(value = "/data/admin/business/transChangeNote")
public class TransChangeNoteController extends ExtJsController {

    @Autowired
    private UicUserService uicUserService;
    @Autowired
    private EsTransChangeNoteService esTransChangeNoteService;
    @Autowired
    private ChannelInfoService channelInfoService;

    /**
     * 查询合计
     */
    @RequestMapping("/sumData")
    public Object sumData(@RequestBody TransChangeNote note) {
        return esTransChangeNoteService.getAmount(note);
    }

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Long userId = null;
        Integer businessType = null;
        String beginDate = null;
        String endDate = null;
        Double changeMoneyLow = null;
        Double changeMoneyHigh = null;
        Integer changeType = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userId = data.getLong("userId");
            businessType = data.getInteger("businessType");
            beginDate = data.getString("startTime");
            endDate = data.getString("endTime");
            changeMoneyLow = data.getDouble("changeMoneyLow");
            changeMoneyHigh = data.getDouble("changeMoneyHigh");
            changeType = data.getInteger("changeType");
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
        TransChangeNote note = new TransChangeNote();
        note.setParentId(parentId);
        note.setChannelId(channelId);
        note.setUserId(userId);
        note.setBusinessType(businessType);
        note.setBeginDate(beginDate);
        note.setEndDate(endDate);
        note.setChangeMoneyLow(changeMoneyLow);
        note.setChangeMoneyHigh(changeMoneyHigh);
        note.setChangeType(changeType);
        Page<TransChangeNote> page = esTransChangeNoteService.findPage(start,length,note);
        List<TransChangeNote> pageData = page.getData();
        for (TransChangeNote temp : pageData) {
            if(temp.getChannelId() != null){
                ChannelInfo info =	channelInfoService.get(temp.getChannelId());
                if(info != null) {
                    temp.setChannelName(info.getName() + "(" + info.getId() + ")");
                }
            }
            Long id = temp.getUserId();
            if(null != id){
                UicUser user = uicUserService.get(id);
                if (null != user) {
                    temp.setUserName(user.getNickname());
                }
            }
        }
        return dataGrid(page);
    }
}
