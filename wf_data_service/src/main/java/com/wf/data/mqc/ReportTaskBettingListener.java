package com.wf.data.mqc;

import com.wf.core.event.BettingTaskEvent;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengsheng.liu
 * @date 2017年6月14日
 */
public class ReportTaskBettingListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage(Message message) {

        BettingTaskEvent event = (BettingTaskEvent) rabbitTemplate.getMessageConverter().fromMessage(message);
        try {
            rabbitTemplate.convertAndSend("report_change_betting", event);
        } catch (Exception e) {
            logger.error("ReportTaskBettingListener转发失败: traceId={}, ex={},event={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(event));

        }

    }

}
