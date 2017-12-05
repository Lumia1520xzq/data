package com.wf.data.task.dataclean;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.ReportFishBettingInfoService;
import com.wf.data.service.RoomFishInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Component
public class OldReportFishInfoJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoomFishInfoService roomFishInfoService = SpringContextHolder.getBean(RoomFishInfoService.class);
    private final ReportFishBettingInfoService reportFishBettingInfoService = SpringContextHolder.getBean(ReportFishBettingInfoService.class);
    private final DataConfigService dataConfigService= SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        logger.info("捕鱼数据清洗开始:traceId={}", TraceIdUtils.getTraceId());
        String date = dataConfigService.findByName(DataConstants.DATA_FISH_BEGIN_DAY).getValue();
        if (StringUtils.isBlank(date)) {
            logger.error("捕鱼开始时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }
        Date startDate = DateUtils.parseDate(date);
        Date nextDate = startDate;
        Date endDate = DateUtils.parseDate(DateUtils.formatDate(new Date()));
        int i = 0;
        while (nextDate.getTime() <= endDate.getTime()) {
            nextDate = DateUtils.getNextDate(startDate, i++);
            String bettingDate = DateUtils.formatDate(nextDate);
            Date searchDate = DateUtils.parseDate(bettingDate);
            List<ReportFishBettingInfo> bettingInfoList = getRoomFishInfoList(searchDate);
            String month = DateUtils.formatDate(searchDate, "yyyyMM");
            String tableName = "report_fish_betting_info_" + month;
            try {
                reportFishBettingInfoService.batchSave(tableName, bettingInfoList, bettingDate);
            } catch (Exception e) {
                logger.error("report_fish_betting_info表保存失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
            }
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
        String yesterday = DateUtils.formatDate(searchDate, DateUtils.YYYYMMDD_PATTERN);
        dbName = dbName + yesterday;

        return roomFishInfoService.findFishDateByDate(map, dbName);
    }


}
