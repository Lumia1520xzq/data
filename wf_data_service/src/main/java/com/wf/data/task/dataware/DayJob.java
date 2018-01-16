package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.ChannelConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每日任务调度总job
 * @author chengsheng.liu
 * @date 2018年01月15日
 */
public class DayJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelConversionService channelConversionService = SpringContextHolder.getBean(ChannelConversionService.class);

    public void execute() {
        logger.info("每日任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());




        try {
            channelConversionService.toDoConversionAnalysis();
        }catch (Exception e){
            logger.error("dataware_final_channel_conversion清洗失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        logger.info("每日任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }





}
