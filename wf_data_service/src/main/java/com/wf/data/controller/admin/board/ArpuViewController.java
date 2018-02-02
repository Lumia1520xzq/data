package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.persistence.Page;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredArpu;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.data.DatawareFinalRegisteredArpuService;
import com.wf.data.service.elasticsearch.EsTransChangeNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JoeH
 * 2018/02/02
 */
@RestController
@RequestMapping(value = "/data/arpu/view")
public class ArpuViewController extends ExtJsController {


    @Autowired
    private DatawareFinalRegisteredArpuService arpuService;

    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String beginDate = null;
        String endDate = null;
        List<DatawareFinalRegisteredArpu> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        List<String> datelist = Lists.newArrayList();
        if (data != null) {
            parentId = data.getLong("parentId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endDate = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                datelist.add(beginDate);
            } else {
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>();
        if (parentId == null){
            params.put("parentId", 1);
        }else{
            params.put("parentId", parentId);
        }
        for (String searchDate : datelist) {
            params.put("businessDate", searchDate);
            DatawareFinalRegisteredArpu arpu = arpuService.getArpuByDate(params);
            if (null != arpu){
                list.add(arpu);
            }
        }
        return list;
    }
}
