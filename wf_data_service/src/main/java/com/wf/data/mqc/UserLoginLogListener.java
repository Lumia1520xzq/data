package com.wf.data.mqc;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.mqc.processor.UserLoginLogProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年6月14日
 */
public class UserLoginLogListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserLoginLogProcessor userLoginLogProcessor;

    @Override
    public void onMessage(Message message) {

        try {
            Map<String, Object> mapReq = (Map<String, Object>) rabbitTemplate.getMessageConverter().fromMessage(message);

            userLoginLogProcessor.process(mapReq);
        } catch (Exception e) {
            logger.error("UserLoginLogListener处理错误: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }

}
