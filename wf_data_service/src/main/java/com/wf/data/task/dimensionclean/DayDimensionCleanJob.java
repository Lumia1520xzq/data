package com.wf.data.task.dimensionclean;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.business.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每日用户维度清洗任务调度总job
 *
 * @author Joe Huang
 * date 2018年03月14日
 */
public class DayDimensionCleanJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserInfoExtendService userInfoExtendService = SpringContextHolder.getBean(UserInfoExtendService.class);
    private final UserInfoExtendGameService userInfoExtendGameService = SpringContextHolder.getBean(UserInfoExtendGameService.class);

    public void execute() {
        logger.info("每日用户维度清洗任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());

        //用户基本信息维度表清洗
        try {
            userInfoExtendService.toDoAnalysis();
        }catch (Exception e){
            logger.error("userInfoExtendService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        //用户基本信息维度表清洗(各游戏)
        try {
            userInfoExtendGameService.toDoAnalysis();
        }catch (Exception e){
            logger.error("userInfoExtendGameService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("每日用户维度清洗任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }

}
