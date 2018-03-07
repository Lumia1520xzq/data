package com.wf.data.task.docking;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.docking.SendThirdIdToJddService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时推送奖多多彩票ID
 * 1、游戏平台的全量用户彩票ID
 * 2、昨日新增用户彩票ID
 * @author chengsheng.liu
 * @date 2018年3月5日
 */
public class SendThirdIdToJddJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SendThirdIdToJddService sendThirdIdToJddService = SpringContextHolder.getBean(SendThirdIdToJddService.class);

    public void execute() {
        logger.info("推送奖多多彩票ID开始。。。。。。。。");

        try {
            sendThirdIdToJddService.toDoAnalysis();
        } catch (Exception e) {
            logger.error("sendThirdId发送失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("推送奖多多彩票ID结束。。。。。。。。");
    }


}
