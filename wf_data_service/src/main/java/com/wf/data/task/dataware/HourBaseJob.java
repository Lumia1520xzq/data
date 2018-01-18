package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每小时任务调度总job
 *
 * @author chengsheng.liu
 * @date 2018年01月15日
 */
public class HourBaseJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BettingLogHourService bettingLogHourService = SpringContextHolder.getBean(BettingLogHourService.class);
    private final BuryingPointHourService buryingPointHourService = SpringContextHolder.getBean(BuryingPointHourService.class);
    private final ConvertHourService convertHourService = SpringContextHolder.getBean(ConvertHourService.class);
    private final UserRegisteredHourService userRegisteredHourService = SpringContextHolder.getBean(UserRegisteredHourService.class);
    private final UserSignHourService userSignHourService = SpringContextHolder.getBean(UserSignHourService.class);

    public void execute() {
        logger.info("每小时基础数据任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());


        try {
            bettingLogHourService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("bettingLogHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            buryingPointHourService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("buryingPointHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            convertHourService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("convertHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            userRegisteredHourService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("userRegisteredHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            userSignHourService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("userSignHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        logger.info("每小时基础数据任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
