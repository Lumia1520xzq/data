package com.wf.data.task.dimensionclean;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
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
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        logger.info("每日用户维度清洗任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());
        String flagStr = dataConfigService.findByName(DataConstants.DATA_DATAWARE_DIMENSION_CLEAN_FLAG).getValue();
        if(StringUtils.isNotEmpty(flagStr)) {
            String[] flags = flagStr.split(",");
            //用户基本信息维度表清洗
            if(Boolean.parseBoolean(flags[0]) == true){
                try {
                    userInfoExtendService.toDoAnalysis();
                }catch (Exception e){
                    logger.error("userInfoExtendService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
                }
            }else{
                logger.info("开关未开,用户基本信息维度表未清洗:traceId={}", TraceIdUtils.getTraceId());
            }

            //用户基本信息维度表清洗(各游戏)
            if(Boolean.parseBoolean(flags[1]) == true) {
                try {
                    userInfoExtendGameService.toDoAnalysis();
                } catch (Exception e) {
                    logger.error("userInfoExtendGameService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
                }
            }else{
                logger.info("开关未开,用户基本信息维度表(各游戏信息)未清洗:traceId={}", TraceIdUtils.getTraceId());
            }
            logger.info("每日用户维度清洗任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());

        }else {
            logger.info("每日用户维度清洗任务开关未设置:traceId={}", TraceIdUtils.getTraceId());
        }

    }

}
