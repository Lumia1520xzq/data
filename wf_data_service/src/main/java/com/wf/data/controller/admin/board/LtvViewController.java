package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.prism.ps.PPStoPSWDisplacementMapPeer;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dto.MonthlyDataDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelConversionService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.CharsetEncoder;
import java.util.*;

/**
 * 用户ltv分析
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/ltv/view")
public class LtvViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelInfoAllService channelInfoAllService;

    /**
     * 用户ltv分析
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        String startTime = null;
        String endTime = null;
        String channelStr = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
            channelStr = data.getString("channels");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -30));
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
        params.put("beginDate",startTime);
        params.put("endDate",endTime);
        List<Long> channels;
        if(null == channelStr){
            channels = Arrays.asList(new Long[]{1L,100001L,100006L,100015L,100016L});
        }else {
            channels = JSONObject.parseArray(channelStr,long.class);
        }
        Map<String,Object> map = new HashMap<>();
        //遍历channels
        for(Long channel:channels){
        params.put("parentId", channel);
        List<DatawareFinalChannelInfoAll> infoAllList = channelInfoAllService.getListByChannelAndDate(params);
        map.put(""+channel,infoAllList);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }

}
