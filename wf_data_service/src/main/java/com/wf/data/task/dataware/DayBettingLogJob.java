package com.wf.data.task.dataware;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.data.entity.DatawareBettingLogDay;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.service.*;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBettingLogHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class DayBettingLogJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataDictService dataDictService = SpringContextHolder.getBean(DataDictService.class);
    private final DatawareBettingLogHourService datawareBettingLogHourService = SpringContextHolder.getBean(DatawareBettingLogHourService.class);
    private final DatawareBettingLogDayService datawareBettingLogDayService = SpringContextHolder.getBean(DatawareBettingLogDayService.class);

    public void execute() {
        logger.info("每天投注汇总开始:traceId={}", TraceIdUtils.getTraceId());
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }

        String cleanDay = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_BETTING_DAY);
        String[] cleanDays = cleanDay.split(",");

        Map<String, Object> params = new HashMap<>();

        if (cleanDays.length == 2) {
            List<String> datelist = DateUtils.getDateList(cleanDays[0], cleanDays[1]);
            for (String bettingDate : datelist) {
                params.put("bettingDate", bettingDate);
                long count = datawareBettingLogDayService.getCountByTime(params);
                if(count <=0){
                    dayBettingLog(bettingDate, uicGroupList);
                }
            }

        } else {
            String bettingDate = "";
            if (StringUtils.isNotEmpty(cleanDay)) {
                bettingDate = cleanDay;
            } else {
                bettingDate = DateUtils.getYesterdayDate();
            }
            params.put("bettingDate", bettingDate);
            long count = datawareBettingLogDayService.getCountByTime(params);
            if(count <=0){
                dayBettingLog(bettingDate, uicGroupList);
            }
        }


        logger.info("每天投注汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void dayBettingLog(String yesterday, List<Long> uicGroupList) {
        try {
            DatawareBettingLogHour log = new DatawareBettingLogHour();
            log.setBettingDate(yesterday);
            log.setUserGroup(2);
            //1、所有用户制成正常用户
            datawareBettingLogHourService.updateUserGroup(log);
            //2、获取当天所有用户
            List<Long> userList = datawareBettingLogHourService.findUserId(log);
            //3、求当天所有用户与非正常用户的交集
            Collection interColl = CollectionUtils.intersection(userList, uicGroupList);
            List<Long> users = (List) interColl;
            //4、更新交集用户状态为非正常用户
            log.setUserGroup(1);
            log.setUserList(users);
            datawareBettingLogHourService.updateUserGroup(log);
        } catch (Exception e) {
            logger.error("更新用户状态失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("bettingDate", yesterday);
            params.put("userGroup", 2);
            List<DatawareBettingLogDay> bettingList = datawareBettingLogHourService.findBettingList(params);
            if (CollectionUtils.isNotEmpty(bettingList)) {
                for (DatawareBettingLogDay logDay : bettingList) {
                    DataDict dataDict = dataDictService.getDictByValue("game_type", logDay.getGameType());
                    if (null != dataDict) {
                        logDay.setGameName(dataDict.getLabel());
                    }
                }
                datawareBettingLogDayService.batchSave(bettingList);
            }
        } catch (Exception e) {
            logger.error("更新datawareBettingLogDay失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


}
