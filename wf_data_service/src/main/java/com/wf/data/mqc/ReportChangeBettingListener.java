package com.wf.data.mqc;

import com.wf.core.event.BettingTaskEvent;
import com.wf.data.mqc.processor.ReportChangeBettingProcessor;
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
public class ReportChangeBettingListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ReportChangeBettingProcessor bettingProcessor;

    @Override
    public void onMessage(Message message) {

        try {
            BettingTaskEvent event = (BettingTaskEvent) rabbitTemplate.getMessageConverter().fromMessage(message);

            bettingProcessor.process(event);
        } catch (Exception e) {
            logger.error("ReportChangeBettingListener处理错误:{}", e);
        }

    }

}
