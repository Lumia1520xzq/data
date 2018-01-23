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
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 月度指标监控
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/month/view")
public class MonthlyDataViewController extends ExtJsController {

    @Autowired
    private DatawareFinalChannelInfoAllService datawareFinalChannelInfoAllService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelCostService datawareFinalChannelCostService;

    /**
     * 月度指标监控
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
                //三个月前的月份
                startTime = getMonth();
                //这个月的月份
                endTime = DateUtils.formatDate(DateUtils.getPrevDate(new Date(),1),DateUtils.MONTH_PATTERN);
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

        return null;
    }


    private  String cal(Long last,Long notlast){
        if(0 == notlast){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }
    private  String cal(Double last,Double notlast){
        if(0 == notlast){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }

    private static String getMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getPrevDate(new Date(),1));
        cal.add(Calendar.MONTH,-2);
        Date date = cal.getTime();
        return DateUtils.formatDate(date,DateUtils.MONTH_PATTERN);
    }

}
