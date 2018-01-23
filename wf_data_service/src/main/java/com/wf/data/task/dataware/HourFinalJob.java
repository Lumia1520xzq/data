package com.wf.data.task.dataware;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.business.ChannelInfoHourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每小时任务调度总job
 *
 * @author chengsheng.liu
 * @date 2018年01月15日
 */
public class HourFinalJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelInfoHourService channelInfoHourService = SpringContextHolder.getBean(ChannelInfoHourService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        logger.info("每小时任务调度总job开始:traceId={}", TraceIdUtils.getTraceId());
        String openStr = dataConfigService.getStringValueByName(DataConstants.DATA_FINAL_HOUR_OPEN);

        String[] openFlag = openStr.split(",");

        try {
            if ("true".equals(openFlag[0])) {
                channelInfoHourService.toDoAnalysis();
            }
        } catch (Exception e) {
            logger.error("channelInfoHourService调度失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }


        logger.info("每小时任务调度总job结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
