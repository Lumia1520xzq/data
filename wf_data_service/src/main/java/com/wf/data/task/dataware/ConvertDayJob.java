package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareConvertHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class ConvertDayJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final DatawareConvertDayService datawareConvertDayService = SpringContextHolder.getBean(DatawareConvertDayService.class);
    private final DatawareConvertHourService datawareConvertHourService = SpringContextHolder.getBean(DatawareConvertHourService.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);

    public void execute() {
        logger.info("每小时埋点汇总开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_CONVERT_FLAG_DAY);
        if (false == flag) {
            historyConvert();
        } else {
            String yesterdayDate = DateUtils.getYesterdayDate();
            convert(yesterdayDate);
        }


        logger.info("每小时埋点汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyConvert() {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_CONVERT_HISTORY_DAY);

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
            for (String searchDate : datelist) {
                convert(searchDate);
            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }

    private void convert(String yesterday) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("convertDate", yesterday);

            List<DatawareConvertDay> convertList = datawareConvertHourService.findConvertList(map);

            if (CollectionUtils.isNotEmpty(convertList)) {
                long count = datawareConvertDayService.getCountByTime(map);
                if (count <= 0) {
                    for(DatawareConvertDay item :convertList){
                        if (null != item.getChannelId()) {
                            ChannelInfo channelInfo = channelInfoService.get(item.getChannelId());
                            if (null != channelInfo) {
                                if(null == channelInfo.getParentId()){
                                    item.setParentId(item.getChannelId());
                                }else {
                                    item.setParentId(channelInfo.getParentId());
                                }
                            }
                        }
                    }
                    datawareConvertDayService.batchSave(convertList);
                }
            }
        } catch (Exception e) {
            logger.error("datawareConvertDay添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


}
