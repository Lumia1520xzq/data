package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.controller.response.FilterDataViewExcelResponse;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelConversion;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelConversionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String beginDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(searchDate), 30));
        String endDate = searchDate;
        Map<String, Object> params = new HashMap<>(3);
        if (null == parentId) {
            parentId = 1L;
        }
        ChannelInfo channelInfo = channelInfoService.get(parentId);
        if (null != channelInfo) {
            if (null != channelInfo.getParentId()) {
                params.put("channelId", parentId);
            } else {
                params.put("parentId", parentId);
            }
        } else {
            params.put("parentId", parentId);
        }
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return conversionService.getByChannelAndDate(params);
    }

    /**
     * 数据下载
     */
    @RequestMapping("/export")
    public void export(@RequestParam String parentId, @RequestParam String searchDate, HttpServletResponse response) {
        List<FilterDataViewExcelResponse> responses = new ArrayList<>();
        try {

            /* 整理参数*/
            Long parentIdParam = null;
            String beginDateParam = null;
            String endDateParam = null;
            try {
                if (!judgeParamIsBank(parentId)) {
                    parentIdParam = Long.parseLong(parentId);
                }
                if (judgeParamIsBank(searchDate)) {
                    beginDateParam = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -14));
                    endDateParam = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -1));
                } else {
                    endDateParam = formatGTMDate(searchDate);
                    beginDateParam = DateUtils.getPrevDate(endDateParam, 13);
                }
            } catch (Exception e) {
                logger.error("导出核心指标总览数据时查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId());
            }

            Map<String, Object> params = new HashMap<>(3);
            if (null == parentIdParam) {
                parentIdParam = 1L;
            }
            ChannelInfo channelInfo = channelInfoService.get(parentIdParam);
            if (null != channelInfo) {
                if (null != channelInfo.getParentId()) {
                    params.put("channelId", parentIdParam);
                } else {
                    params.put("parentId", parentIdParam);
                }
            } else {
                params.put("parentId", parentIdParam);
            }
            params.put("beginDate", beginDateParam);
            params.put("endDate", endDateParam);

            /*获取数据*/
            List<DatawareFinalChannelConversion> finalChannelConversions = conversionService.getByChannelAndDate(params);
            if (CollectionUtils.isNotEmpty(finalChannelConversions)) {
                for (DatawareFinalChannelConversion channelConversion : finalChannelConversions) {
                    FilterDataViewExcelResponse excelResponse = new FilterDataViewExcelResponse();
                    excelResponse.setBusinessDate(channelConversion.getBusinessDate());

                    Long channelIde = channelConversion.getChannelId();
                    excelResponse.setChannelId(channelIde);

                    //根据channelId获取渠道名称
                    if (1 == channelIde.intValue()) {
                        excelResponse.setChannelName("全部(1)");
                    } else {
                        ChannelInfo channel = channelInfoService.get(channelIde);
                        excelResponse.setChannelName(channel.getName() + "(" + channelIde + ")");
                    }

                    excelResponse.setDauCount(channelConversion.getDauCount());
                    excelResponse.setGamedauCount(channelConversion.getGamedauCount());
                    excelResponse.setBettingCount(channelConversion.getBettingCount());
                    excelResponse.setRechargeCount(channelConversion.getRechargeCount());
                    excelResponse.setRegisteredCount(channelConversion.getRegisteredCount());
                    excelResponse.setDauRegistered(channelConversion.getDauRegistered());
                    excelResponse.setGamedauRegistered(channelConversion.getGamedauRegistered());
                    excelResponse.setBettingRegistered(channelConversion.getBettingRegistered());
                    excelResponse.setRechargeRegistered(channelConversion.getRechargeRegistered());
                    excelResponse.setDauOlder(channelConversion.getDauOlder());
                    excelResponse.setGamedauOlder(channelConversion.getGamedauOlder());
                    excelResponse.setBettingOlder(channelConversion.getBettingOlder());
                    excelResponse.setRechargeOlder(channelConversion.getRechargeOlder());
                    responses.add(excelResponse);
                }
            }
            String fileName = "转化漏斗分析" + com.wf.core.utils.type.DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("转化漏斗分析", FilterDataViewExcelResponse.class).setDataList(responses).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean judgeParamIsBank(String param) {
        if (param.equals("undefined") || param.equals("null") || StringUtils.isBlank(param)) {
            return true;
        }
        return false;
    }

    /**
     * 格式化GMT时间
     *
     * @param date
     * @return
     */
    public String formatGTMDate(String date) {
        DateFormat gmt = new SimpleDateFormat("yyyy-MM-dd");
        date = date.replace("GMT 0800", "GMT +08:00").replace("GMT 0800", "GMT+0800").replaceAll("\\(.*\\)", "");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US);
        Date time = null;
        try {
            time = defaultFormat.parse(date);
            gmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gmt.format(time);
    }

}
