package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.elasticsearch.EsTransChangeNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shihui
 * @date 2018/1/24
 */

@RestController
@RequestMapping("data/admin/channel/financetrans")
public class ChannelFinanceTransController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private UicUserService uicUserService;

    @Autowired
    private EsTransChangeNoteService esTransChangeNoteService;

    @RequestMapping("list")
    public Object listData() {

        JSONObject json = getRequestJson();

        Long userId = null;
        Long parentId = null;
        Long channelId = null;
        Integer changeType = null;
        Double changeMoneyLow = null;
        Double changeMoneyHigh = null;
        Integer businessType = null;
        String beginDate = null;
        String endDate = null;

        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            userId = data.getLong("userId");
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            changeType = data.getInteger("changeType");
            changeMoneyLow = data.getDouble("changeMoneyLow");
            changeMoneyHigh = data.getDouble("changeMoneyHigh");
            businessType = data.getInteger("businessType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
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

        TransChangeNote paramBean = new TransChangeNote();
        paramBean.setUserId(userId);
        paramBean.setParentId(parentId);
        paramBean.setChannelId(channelId);
        paramBean.setChangeType(changeType);
        paramBean.setChangeMoneyLow(changeMoneyLow);
        paramBean.setChangeMoneyHigh(changeMoneyHigh);
        paramBean.setBusinessType(businessType);
        paramBean.setBeginDate(beginDate);
        paramBean.setEndDate(endDate);


        Page<TransChangeNote> page = esTransChangeNoteService.findPage(start, length, paramBean);
        List<TransChangeNote> pageData = page.getData();
        for (TransChangeNote temp : pageData) {
            if (temp.getChannelId() != null) {
                ChannelInfo info = channelInfoService.get(temp.getChannelId());
                if (info != null) {
                    temp.setChannelName(info.getName() + "(" + info.getId() + ")");
                }
            }
            UicUser user = uicUserService.getByUserId(temp.getUserId());
            if (user != null) {
                temp.setUserName(user.getNickname());
                temp.setThirdId(user.getThirdId());
            }
        }
        return dataGrid(page);
    }

    /**
     * 查询合计
     */
    @RequestMapping("/sumData")
    public Object sumData(@RequestBody TransChangeNote note) {
        return esTransChangeNoteService.getAmount(note);
    }


}
