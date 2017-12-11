package com.wf.data.task.dataclean;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.service.ReportFishBettingInfoService;
import com.wf.data.service.RoomFishInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class ReportFishInfoJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomFishInfoService roomFishInfoService = SpringContextHolder.getBean(RoomFishInfoService.class);
    private final ReportFishBettingInfoService reportFishBettingInfoService = SpringContextHolder.getBean(ReportFishBettingInfoService.class);


    public void execute() {
        logger.info("捕鱼数据清洗开始:traceId={}", TraceIdUtils.getTraceId());
        String bettingDate = DateUtils.getYesterdayDate();
        Date yesterday = DateUtils.parseDate(DateUtils.getYesterdayDate());
        List<ReportFishBettingInfo> bettingInfoList = getRoomFishInfoList(yesterday);
        String month = DateUtils.formatDate(yesterday, "yyyyMM");
        String tableName = "report_fish_betting_info_" + month;
        try {
            reportFishBettingInfoService.batchSave(tableName, bettingInfoList, bettingDate);
        } catch (Exception e) {
            logger.error("report_fish_betting_info表保存失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
        }
        logger.info("捕鱼数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private List<ReportFishBettingInfo> getRoomFishInfoList(Date searchDate) {
        String beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(searchDate), DateUtils.DATE_TIME_PATTERN);
        String endDate = DateUtils.formatDate(DateUtils.getDayEndTime(searchDate), DateUtils.DATE_TIME_PATTERN);
        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);

        String dbName = "fish";
        String yesterday = DateUtils.formatDate(searchDate,DateUtils.YYYYMMDD_PATTERN);
        dbName = dbName +yesterday;

        return roomFishInfoService.findFishDateByDate(map,dbName);
    }


}
