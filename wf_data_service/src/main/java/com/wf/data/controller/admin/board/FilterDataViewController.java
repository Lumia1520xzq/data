package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelConversion;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelConversionService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 转化漏斗看板
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/filter/view")
public class FilterDataViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelConversionService conversionService;

    /**
     * 转化漏斗数据
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String searchDate = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            searchDate = data.getString("searchDate");
        }
        try {
            if (StringUtils.isBlank(searchDate)) {
                searchDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -1));
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        String beginDate =  DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(searchDate),30));
        String endDate = searchDate;
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
            params.put("beginDate",beginDate);
            params.put("endDate",endDate);
            return conversionService.getByChannelAndDate(params);
    }

}
