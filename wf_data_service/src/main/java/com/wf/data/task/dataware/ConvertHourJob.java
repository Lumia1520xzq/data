package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareConvertHour;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.data.DatawareConvertHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class ConvertHourJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final DatawareConvertHourService datawareConvertHourService = SpringContextHolder.getBean(DatawareConvertHourService.class);

    public void execute() {
        logger.info("每小时充值汇总开始:traceId={}", TraceIdUtils.getTraceId());


        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_BURYING_FLAG_HOUR);
        if (false == flag) {
            historyConvert();
        } else {
            hourConvert();
        }


        logger.info("每小时充值汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyConvert() {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_BURYING_HISTORY_HOUR);

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
        convert(map);
    }

    private void convert(Map<String, Object> map) {
        try {
            List<DatawareConvertHour> hourList = transConvertService.findConvertList(map);

            if (CollectionUtils.isNotEmpty(hourList)) {
                Map<String, Object> params = new HashMap<>();
                params.put("convertDate", hourList.get(0).getConvertDate());
                params.put("convertHour", hourList.get(0).getConvertHour());
                long count = datawareConvertHourService.getCountByTime(params);
                if (count <= 0) {
                    datawareConvertHourService.batchSave(hourList);
                }
            }
        } catch (Exception e) {
            logger.error("datawareConvertHour添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

}
