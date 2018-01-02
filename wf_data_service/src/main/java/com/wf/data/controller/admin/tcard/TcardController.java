package com.wf.data.controller.admin.tcard;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.TcardUserBettingLogService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 乐盈三张
 *
 * @author lcs
 */
@RestController
@RequestMapping("/data/admin/tcard")
public class TcardController extends ExtJsController {

    @Autowired
    private TcardUserBettingLogService tcardUserBettingLogService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    /**
     * 根据类型获取列表
     *
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        String startTime = null;
        String endTime = null;

        List<TcardDto> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        List<String> datelist = Lists.newArrayList();
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");

        }

        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endTime = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(startTime, endTime);
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
                datelist.add(startTime);
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                endTime = startTime;
                datelist.add(startTime);
            } else {
                datelist = DateUtils.getDateList(startTime, endTime);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        Map<String, Object> params = new HashMap<>();
        for (String searchDate : datelist) {
            params.put("searchDate", searchDate);
            if (null != channelId) {
                params.put("channelId", channelId);
            }
            if (null != parentId && null == channelId) {
                params.put("parentId", parentId);
            }
            TcardDto dto = new TcardDto();

            list.add(dto);
        }


        return list;
    }

    /**
     * 三张场次分析
     *
     * @return
     */
    @RequestMapping("/getAnalysisList")
    public Object getAnalysisList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        String startTime = null;
        String endTime = null;
        List<TcardDto> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        List<String> datelist = Lists.newArrayList();
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endTime = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(startTime, endTime);
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
                datelist.add(startTime);
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                datelist.add(startTime);
            } else {
                datelist = DateUtils.getDateList(startTime, endTime);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>();
        for (String searchDate : datelist) {
            params.put("searchDate", searchDate);
            if (null != channelId) {
                params.put("channelId", channelId);
            }
            if (null != parentId && null == channelId) {
                params.put("parentId", parentId);
            }

            TcardDto dto = new TcardDto();

            list.add(dto);
        }
        return list;
    }

}
