package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareConvertHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.data.DatawareConvertHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class ConvertHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private TransConvertService transConvertService;
    @Autowired
    private DatawareConvertHourService datawareConvertHourService;
    @Autowired
    private ChannelInfoService channelInfoService;

    public void toDoAnalysis() {
        logger.info("每小时充值汇总开始:traceId={}", TraceIdUtils.getTraceId());


        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_CONVERT_FLAG_HOUR);
        if (false == flag) {
            historyConvert();
        } else {
            hourConvert();
        }


        logger.info("每小时充值汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyConvert() {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_CONVERT_HISTORY_HOUR);

        if (StringUtils.isBlank(date)) {
            logger.error("清洗时间未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }

        String[] dates = date.split(",");
        if (StringUtils.isBlank(dates[0])) {
            logger.error("清洗开始时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }
        if (StringUtils.isBlank(dates[1])) {
            logger.error("清洗结束时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }

        try {
            if (DateUtils.formatDate(DateUtils.parseDate(dates[0]), "yyyy-MM-dd").equals(DateUtils.formatDate(DateUtils.parseDate(dates[1]), "yyyy-MM-dd"))) {
                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", dates[0]);
                map.put("endDate", dates[1]);

                convert(map);
            } else {

                List<String> datelist = DateUtils.getDateList(dates[0], dates[1]);
                String beginDate = "";
                String endDate = "";
                for (String searchDate : datelist) {

                    if (datelist.get(0) == searchDate) {
                        beginDate = searchDate;
                        endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                    } else if (searchDate == datelist.get(datelist.size() - 1)) {
                        beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                        endDate = searchDate;
                    } else {
                        beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
                        endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("beginDate", beginDate);
                    map.put("endDate", endDate);
                    convert(map);
                }
            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }

    private void hourConvert() {
        Calendar cal = Calendar.getInstance();
        //当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String beginDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm:ss");

        //结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        String endDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");

        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);

        String searchDay = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
        Map<String, Object> params = new HashMap<>();
        params.put("convertDate", searchDay);
        params.put("convertHour", searchHour);

        long count = datawareConvertHourService.getCountByTime(params);
        if (count > 0) {
            datawareConvertHourService.deleteByDate(params);
        }
        convert(map);
    }

    private void convert(Map<String, Object> map) {
        try {
            List<DatawareConvertHour> hourList = transConvertService.findConvertList(map);

            if (CollectionUtils.isNotEmpty(hourList)) {
                for (DatawareConvertHour item : hourList) {
                    if (null != item.getChannelId()) {
                        ChannelInfo channelInfo = channelInfoService.get(item.getChannelId());
                        if (null != channelInfo) {
                            if (null == channelInfo.getParentId()) {
                                item.setParentId(item.getChannelId());
                            } else {
                                item.setParentId(channelInfo.getParentId());
                            }
                        }
                    }
                }
                datawareConvertHourService.batchSave(hourList);
            }
        } catch (Exception e) {
            logger.error("datawareConvertHour添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


    @Async
    public void dataClean(String startTime, String endTime) {

        try {
            String startDay = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
            String endDay = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
            String startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");
            String endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
            if (startDay.equals(endDay)) {
                Map<String, Object> params = new HashMap<>();
                params.put("convertDate", startDay);
                params.put("startHour", startHour);
                params.put("endHour", endHour);

                long count = datawareConvertHourService.getCountByTime(params);
                if (count > 0) {
                    datawareConvertHourService.deleteByDate(params);
                }


                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", startTime);
                map.put("endDate", endTime);
                convert(map);
            } else {
                forDay(startTime, endTime);
            }

        } catch (Exception e) {
            logger.error("错误: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }


        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void forDay(String startTime, String endTime) {
        List<String> datelist = DateUtils.getDateList(startTime, endTime);
        String beginDate = "";
        String endDate = "";
        String signDate = "";
        String startHour = "";
        String endHour = "";

        for (String searchDate : datelist) {

            if (datelist.get(0) == searchDate) {
                beginDate = searchDate;
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                signDate = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
                startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");

            } else if (searchDate == datelist.get(datelist.size() - 1)) {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = searchDate;

                signDate = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
                endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
                startHour = "";
            } else {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");

                signDate = DateUtils.formatDate(DateUtils.parseDate(searchDate));
                endHour = "";
                startHour = "";
            }


            Map<String, Object> params = new HashMap<>();
            params.put("convertDate", signDate);
            params.put("startHour", startHour);
            params.put("endHour", endHour);

            long count = datawareConvertHourService.getCountByTime(params);
            if (count > 0) {
                datawareConvertHourService.deleteByDate(params);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            convert(map);
        }
    }

}
