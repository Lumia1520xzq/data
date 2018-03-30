package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.controller.response.HourlyMonitorExprotResponse;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelInfoHourService;
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
 * 实时预警
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/hour/view")
public class HourlyMonitorViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelInfoHourService datawareFinalChannelInfoHourService;

    /**
     * 实时预警
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String businessDate = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            businessDate = data.getString("businessDate");
        }
        try {
            Date now = new Date();
            if (StringUtils.isBlank(businessDate) || businessDate.equals(DateUtils.formatDate(now))) {
                businessDate = getDate();
            } else if (DateUtils.parseDate(businessDate).after(now)) {
                businessDate = getDate();
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
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
        //今天的数据
        params.put("businessDate", businessDate);
        List<DatawareFinalChannelInfoHour> todData = datawareFinalChannelInfoHourService.getByDateAndHour(params);

        //昨天的数据
        params.put("businessDate", DateUtils.getPrevDate(businessDate, 1));
        List<DatawareFinalChannelInfoHour> yesData = datawareFinalChannelInfoHourService.getByDateAndHour(params);

        //获取今天最后一条记录
        if (CollectionUtils.isNotEmpty(todData)) {
            DatawareFinalChannelInfoHour lastRecord = todData.get(todData.size() - 1);
            String hour = lastRecord.getBusinessHour();
            //获取昨天相同时间点的数据
            params.put("businessHour", hour);
            List<DatawareFinalChannelInfoHour> yesLastRecordList = datawareFinalChannelInfoHourService.getByDateAndHour(params);
            DatawareFinalChannelInfoHour yesLastRecord = new DatawareFinalChannelInfoHour();
            if (CollectionUtils.isNotEmpty(yesLastRecordList)) {
                yesLastRecord = yesLastRecordList.get(0);
            }
            String dayDauRate = cal(lastRecord.getDau(), yesLastRecord.getDau());
            String dayUserCountRate = cal(lastRecord.getUserCount(), yesLastRecord.getUserCount());
            String dayRechargeCountRate = cal(lastRecord.getRechargeCount(), yesLastRecord.getRechargeCount());
            String dayRechargeAmountRate = cal(lastRecord.getRechargeAmount(), yesLastRecord.getRechargeAmount());
            String dayNewUsersRate = cal(lastRecord.getNewUsers(), yesLastRecord.getNewUsers());
            String dayBettingAmountRate = cal(lastRecord.getBettingAmount(), yesLastRecord.getBettingAmount());
            String dayDiffAmountRate = cal(lastRecord.getDiffAmount(), yesLastRecord.getDiffAmount());
            //获取一周前相同时间点的数据
            params.put("businessDate", DateUtils.getPrevDate(businessDate, 7));
            List<DatawareFinalChannelInfoHour> weekLastRecordList = datawareFinalChannelInfoHourService.getByDateAndHour(params);
            DatawareFinalChannelInfoHour weekLastRecord = new DatawareFinalChannelInfoHour();
            if (CollectionUtils.isNotEmpty(weekLastRecordList)) {
                weekLastRecord = weekLastRecordList.get(0);
            }
            String weekDauRate = cal(lastRecord.getDau(), weekLastRecord.getDau());
            String weekUserCountRate = cal(lastRecord.getUserCount(), weekLastRecord.getUserCount());
            String weekRechargeCountRate = cal(lastRecord.getRechargeCount(), weekLastRecord.getRechargeCount());
            String weekRechargeAmountRate = cal(lastRecord.getRechargeAmount(), weekLastRecord.getRechargeAmount());
            String weekNewUsersRate = cal(lastRecord.getNewUsers(), weekLastRecord.getNewUsers());
            String weekBettingAmountRate = cal(lastRecord.getBettingAmount(), weekLastRecord.getBettingAmount());
            String weekDiffAmountRate = cal(lastRecord.getDiffAmount(), weekLastRecord.getDiffAmount());

            lastRecord.setDayDauRate(dayDauRate);
            lastRecord.setDayUserCountRate(dayUserCountRate);
            lastRecord.setDayRechargeCountRate(dayRechargeCountRate);
            lastRecord.setDayRechargeAmountRate(dayRechargeAmountRate);
            lastRecord.setDayNewUsersRate(dayNewUsersRate);
            lastRecord.setDayBettingAmountRate(dayBettingAmountRate);
            lastRecord.setDayDiffAmountRate(dayDiffAmountRate);

            lastRecord.setWeekDauRate(weekDauRate);
            lastRecord.setWeekUserCountRate(weekUserCountRate);
            lastRecord.setWeekRechargeCountRate(weekRechargeCountRate);
            lastRecord.setWeekRechargeAmountRate(weekRechargeAmountRate);
            lastRecord.setWeekNewUsersRate(weekNewUsersRate);
            lastRecord.setWeekBettingAmountRate(weekBettingAmountRate);
            lastRecord.setWeekDiffAmountRate(weekDiffAmountRate);

            todData.set(todData.size() - 1, lastRecord);
        }

        //历史七天的数据
        params.put("beginDate", DateUtils.getPrevDate(businessDate, 7));
        params.put("endDate", DateUtils.getPrevDate(businessDate, 1));
        List<DatawareFinalChannelInfoHour> historyOriginalList = datawareFinalChannelInfoHourService.getSumDataByDateAndHour(params);
        List<DatawareFinalChannelInfoHour> historyData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(historyOriginalList)) {
            for (DatawareFinalChannelInfoHour record : historyOriginalList) {
                DatawareFinalChannelInfoHour info = new DatawareFinalChannelInfoHour();
                info.setBusinessHour(record.getBusinessHour());
                info.setHourDau(record.getHourDau() / 7);
                info.setHourUserCount(record.getHourUserCount() / 7);
                info.setHourRechargeCount(record.getHourRechargeCount() / 7);
                info.setHourRechargeAmount(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourRechargeAmount(), 7), 0));
                info.setHourNewUsers(record.getHourNewUsers() / 7);
                info.setHourBettingAmount(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourBettingAmount(), 7), 0));
                info.setHourDiffAmount(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourDiffAmount(), 7), 0));
                historyData.add(info);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("todData", todData);
        map.put("yesData", yesData);
        map.put("historyData", historyData);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }

    /**
     * 数据下载
     */
    @RequestMapping("/export")
    public void export(@RequestParam String parentId, @RequestParam String businessDate, HttpServletResponse response) {
        /* 整理参数*/
        Long parentIdParam = null;
        String businessDateParam = null;

        try {
            Date now = new Date();
            if (judgeParamIsBank(formatGTMDate(businessDate)) || formatGTMDate(businessDate).equals(DateUtils.formatDate(now))) {
                businessDateParam = getDate();
            } else if (DateUtils.parseDate(formatGTMDate(businessDate)).after(now)) {
                businessDateParam = getDate();
            }else {
                businessDateParam = formatGTMDate(businessDate);
            }
            if (!judgeParamIsBank(parentId)) {
                parentIdParam = Long.parseLong(parentId);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(formatGTMDate(businessDate)));
        }

        Map<String, Object> params = new HashMap<>(3);
        if (null == parentId) {
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
        params.put("businessDate", businessDateParam);

        /*获取数据*/
        List<DatawareFinalChannelInfoHour> todData = datawareFinalChannelInfoHourService.getByDateAndHour(params);
        List<HourlyMonitorExprotResponse>responses = new ArrayList<>();

        //获取搜索日期最后一条记录
        if (CollectionUtils.isNotEmpty(todData)) {
            for (DatawareFinalChannelInfoHour finalChannelInfoHour : todData) {
                HourlyMonitorExprotResponse exprotResponse = new HourlyMonitorExprotResponse();
                exprotResponse.setBusinessDate(finalChannelInfoHour.getBusinessDate());
                exprotResponse.setBusinessHour(finalChannelInfoHour.getBusinessHour());
                exprotResponse.setParentId(finalChannelInfoHour.getParentId());
                exprotResponse.setHourDau(finalChannelInfoHour.getHourDau());
                exprotResponse.setHourUserBettingCount(finalChannelInfoHour.getHourBettingCount());
                exprotResponse.setHourRechargeCount(finalChannelInfoHour.getHourRechargeCount());
                exprotResponse.setHourRechargeAmount(finalChannelInfoHour.getHourRechargeAmount());
                exprotResponse.setHourNewUsers(finalChannelInfoHour.getHourNewUsers());
                exprotResponse.setHourBettingAmount(finalChannelInfoHour.getHourBettingAmount());
                exprotResponse.setHourDiffAmount(finalChannelInfoHour.getHourDiffAmount());
                exprotResponse.setDau(finalChannelInfoHour.getDau());
                exprotResponse.setUserBettingCount(finalChannelInfoHour.getUserBettingCount());
                exprotResponse.setRechargeCount(finalChannelInfoHour.getRechargeCount());
                exprotResponse.setRechargeAmount(finalChannelInfoHour.getRechargeAmount());
                exprotResponse.setNewUsers(finalChannelInfoHour.getNewUsers());
                exprotResponse.setBettingAmount(finalChannelInfoHour.getBettingAmount());
                exprotResponse.setDiffAmount(finalChannelInfoHour.getDiffAmount());
                responses.add(exprotResponse);
            }
        }

        try {
            String fileName = "小时数据下载" + com.wf.core.utils.type.DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("小时数据下载", HourlyMonitorExprotResponse.class).setDataList(responses).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -1);
        Date date = cal.getTime();
        return DateUtils.formatDate(date, DateUtils.DATE_PATTERN);
    }

    private String cal(Long last, Long notlast) {
        if (null == notlast || 0 == notlast) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last - notlast, notlast), "#.##%");
    }

    private String cal(Double last, Double notlast) {
        if (null == notlast || 0 == notlast) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last - notlast, notlast), "#.##%");
    }

    //判断导出参数是否为空
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
