package com.wf.data.task.dataware;

import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.service.DataConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
public class ChannelInfoAllJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        logger.info("每天数据汇总开始:traceId={}", TraceIdUtils.getTraceId());

        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        List<String> channels = Arrays.asList(channelIdList.split(","));
        for(String channelStr : channels){

        }



        logger.info("每天数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


}
