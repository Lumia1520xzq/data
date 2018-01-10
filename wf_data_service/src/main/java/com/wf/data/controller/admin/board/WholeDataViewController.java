package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
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
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelRetentionService datawareFinalChannelRetentionService;
    @Autowired
    private DatawareFinalChannelCostService datawareFinalChannelCostService;

    /**
     * 整体数据概览
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
        Map<String, Object> params = new HashMap<>(3);
        if(null == parentId){
            parentId = 1L;
        }
        ChannelInfo channelInfo = channelInfoService.get(parentId);
        if (null != channelInfo) {
            if (null != channelInfo.getParentId()) {
                params.put("channelId", parentId);
            } else {
                params.put("parentId", parentId);
            }
        }else {
            params.put("parentId", parentId);
        }
            params.put("beginDate",startTime);
            params.put("endDate",endTime);
            List<DatawareFinalChannelInfoAll> allList = datawareFinalChannelInfoAllService.getListByChannelAndDate(params);
            for(DatawareFinalChannelInfoAll info:allList) {
                String date = info.getBusinessDate();
                params.put("date",date);
                DatawareFinalChannelRetention retention = datawareFinalChannelRetentionService.findByDate(params);
                if(null != retention) {
                    Double usersDayRetention = retention.getUsersDayRetention();
                    Double dayRetention = retention.getDayRetention();
                    Double usersRate = retention.getUsersRate();
                    info.setUsersDayRetention(usersDayRetention);
                    info.setDayRetention(dayRetention);
                    info.setUsersRate(usersRate);
                }else{
                    info.setUsersDayRetention(0.0);
                    info.setDayRetention(0.0);
                    info.setUsersRate(0.0);
                }
                DatawareFinalChannelCost cost = datawareFinalChannelCostService.findByDate(params);
                if(null != cost){
                    Double totalCost = cost.getTotalCost();
                    Double costRate = cost.getCostRate();
                    info.setTotalCost(totalCost);
                    info.setCostRate(costRate);
                }
                else{
                    info.setTotalCost(0.0);
                    info.setCostRate(0.0);
                }
            }
            return  allList;
    }


}
