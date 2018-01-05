package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 整体数据概览
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/board/view")
public class WholeDataViewController extends ExtJsController {

    @Autowired
    private DatawareFinalChannelInfoAllService datawareFinalChannelInfoAllService;

    /**
     * 整体数据概览
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endTime = DateUtils.getYesterdayDate();
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                endTime = startTime;
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>();
        if(null == parentId){
            parentId = 1L;
        }
            params.put("parentId", parentId);
            params.put("beginDate",startTime);
            params.put("endDate",endTime);
        List<DatawareFinalChannelInfoAll>  list =  datawareFinalChannelInfoAllService.getListByChannelAndDate(params);
        Map<String,Object> map = new HashMap<>(4);
        map.put("list",list);
        return map;
    }


}
