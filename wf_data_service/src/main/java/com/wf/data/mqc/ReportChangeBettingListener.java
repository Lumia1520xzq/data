package com.wf.data.mqc;

import com.wf.core.event.BettingTaskEvent;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.mqc.processor.ReportChangeBettingProcessor;
import com.wf.data.service.DataConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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
    @Autowired
    private DataConfigService dataConfigService;

    @Override
    public void onMessage(Message message) {

        BettingTaskEvent event = (BettingTaskEvent) rabbitTemplate.getMessageConverter().fromMessage(message);
        try {
            String gameType = dataConfigService.getStringValueByName(DataConstants.DATA_BETTING_GAMETYPE_CONFINE);
            String[] gameTypes = gameType.split(",");
            List<String> gameTypeList = Arrays.asList(gameTypes);
            if (!gameTypeList.contains(event.getGameType().toString())) {
                bettingProcessor.process(event);
            }
        } catch (Exception e) {
            logger.error("ReportChangeBettingListener处理错误: data={}, ex={},traceId={}", GfJsonUtil.toJSONString(event.toString()), LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
        }

    }

}
