package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.BettingLogDayService;
import com.wf.data.service.business.BuryingPointDayService;
import com.wf.data.service.business.ConvertDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每日任务调度总job
 *
 * @author chengsheng.liu
 * @date 2018年01月15日
 */
public class DayBaseJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BettingLogDayService bettingLogDayService = SpringContextHolder.getBean(BettingLogDayService.class);
    private final BuryingPointDayService buryingPointDayService = SpringContextHolder.getBean(BuryingPointDayService.class);
    private final ConvertDayService convertDayService = SpringContextHolder.getBean(ConvertDayService.class);

    public void execute() {
        logger.info("每日基础数据任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());


        try {
            bettingLogDayService.toDoBettingLogAnalysis();
        } catch (Exception e) {
            logger.error("toDoBettingLogAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            buryingPointDayService.toDoBuryingPointAnalysis();
        } catch (Exception e) {
            logger.error("toDoBuryingPointAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            convertDayService.toDoConvertAnalysis();
        } catch (Exception e) {
            logger.error("toDoConvertAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        logger.info("每日基础数据任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
