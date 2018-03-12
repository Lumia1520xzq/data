package com.wf.data.task.dataware;

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
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final RegisteredArpuService registeredArpuService = SpringContextHolder.getBean(RegisteredArpuService.class);
    private final RegisteredRetentionService registeredRetentionService = SpringContextHolder.getBean(RegisteredRetentionService.class);
    private final EntranceAnalysisService entranceAnalysisService = SpringContextHolder.getBean(EntranceAnalysisService.class);
    private final UserInfoExtendService userInfoExtendService = SpringContextHolder.getBean(UserInfoExtendService.class);

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
        //奖多多渠道各个入口用户数据总结
        try {
            entranceAnalysisService.toDoEntranceAnalysis();
        } catch (Exception e) {
            logger.error("toDoConversionAnalysis调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        String openStr = dataConfigService.getStringValueByName(DataConstants.DATA_FINAL_HOUR_OPEN);

        String[] openFlag = openStr.split(",");

        try {
            if ("true".equals(openFlag[1])) {
                registeredArpuService.toDoAnalysis();
            }
        } catch (Exception e) {
            logger.error("registeredArpuService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            if ("true".equals(openFlag[2])) {
                registeredRetentionService.toDoAnalysis();
            }
        } catch (Exception e) {
            logger.error("registeredArpuService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        //用户基本信息维度表清洗
        try {
            userInfoExtendService.toDoAnalysis();
        }catch (Exception e){
            logger.error("userInfoExtendService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }

        logger.info("每日任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
