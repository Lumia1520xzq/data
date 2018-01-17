package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.ChannelConversionService;
import com.wf.data.service.business.ChannelCostService;
import com.wf.data.service.business.ChannelInfoAllService;
import com.wf.data.service.business.ChannelRetentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每日任务调度总job
 *
 * @author chengsheng.liu
 * @date 2018年01月15日
 */
public class DayFinalJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelCostService channelCostService = SpringContextHolder.getBean(ChannelCostService.class);
    private final ChannelInfoAllService channelInfoAllService = SpringContextHolder.getBean(ChannelInfoAllService.class);
    private final ChannelRetentionService channelRetentionService = SpringContextHolder.getBean(ChannelRetentionService.class);
    private final ChannelConversionService channelConversionService = SpringContextHolder.getBean(ChannelConversionService.class);

    public void execute() {
        logger.info("每日任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());
        try {
            channelCostService.toDoChannelCostAnalysis();
        } catch (Exception e) {
            logger.error("toDoChannelCostAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        try {
            channelInfoAllService.toDoChannelInfoAllAnalysis();
        } catch (Exception e) {
            logger.error("toDoChannelInfoAllAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        try {
            channelRetentionService.toDoChannelRetentionAnalysis();
        } catch (Exception e) {
            logger.error("toDoChannelRetentionAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        try {
            channelConversionService.toDoConversionAnalysis();
        } catch (Exception e) {
            logger.error("toDoConversionAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        logger.info("每日任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
