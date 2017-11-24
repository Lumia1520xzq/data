package com.wf.data.mqc;

import com.wf.core.event.BettingTaskEvent;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.mqc.processor.DataIpRiskProcessor;
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
public class DataIpRiskListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DataIpRiskProcessor dataIpRiskProcessor;

    @Override
    public void onMessage(Message message) {

        try {
            BettingTaskEvent event = (BettingTaskEvent) rabbitTemplate.getMessageConverter().fromMessage(message);

            dataIpRiskProcessor.process(event);
        } catch (Exception e) {
            logger.error("DataIpRiskListener处理错误: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }

}
