package com.wf.data.mqc.processor;

import com.wf.core.event.BettingTaskEvent;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.dao.data.entity.ReportChangeNote;
import com.wf.data.service.ReportChangeNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportChangeBettingProcessor {
    @Autowired
    private ReportChangeNoteService reportChangeNoteService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void process(final BettingTaskEvent event) {

        Long userId = event.getUserId();
        if (userId == null) {
            return;
        }
        if (event.getBettingAmount() == 0 && event.getResturnAmount() == 0) {
            return;
        }
        ReportChangeNote changeNote = new ReportChangeNote();

        changeNote.setUserId(userId);

        if (event.getGameType() != null) {
            changeNote.setGameType(event.getGameType().longValue());
        }
        changeNote.setChannelId(event.getChannelId());

        if (event.getBettingAmount() != null) {
            changeNote.setBettingAmount(event.getBettingAmount().doubleValue());
        }
        if (event.getResturnAmount() != null) {
            changeNote.setResultAmount(event.getResturnAmount().doubleValue());
        }
        if(null != event.getCreateTime()){
            changeNote.setCreateTime(event.getCreateTime());
        }
        try {
            reportChangeNoteService.save(changeNote);
        }catch (Exception e){
            logger.error("保存单笔投注流水失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }



    }


}
