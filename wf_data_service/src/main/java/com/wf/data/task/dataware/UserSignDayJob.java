package com.wf.data.task.dataware;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.data.DatawareUserSignDayService;
import com.wf.data.service.platform.PlatUserCheckLotteryLogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class UserSignDayJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final PlatUserCheckLotteryLogService platUserCheckLotteryLogService = SpringContextHolder.getBean(PlatUserCheckLotteryLogService.class);
    private final DatawareUserSignDayService datawareUserSignDayService = SpringContextHolder.getBean(DatawareUserSignDayService.class);

    public void execute() {
        logger.info("每小时签到汇总开始:traceId={}", TraceIdUtils.getTraceId());
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }

        boolean buryingFlag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_SIGN_FLAG_DAY);
        if (false == buryingFlag) {
            historyUserSign(uicGroupList);
        } else {
            hourUserSign(uicGroupList);
        }


        logger.info("每小时签到汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyUserSign(List<Long> uicGroupList) {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_SIGN_HISTORY_DAY);

        if (StringUtils.isBlank(date)) {
            logger.error("清洗时间未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }

        String[] dates = date.split(",");
        if (StringUtils.isBlank(dates[0])) {
            logger.error("清洗开始时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }
        if (StringUtils.isBlank(dates[1])) {
            logger.error("清洗结束时间未设置: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }

        try {

            List<String> datelist = DateUtils.getDateList(dates[0], dates[1]);
            String beginDate = "";
            String endDate = "";
            for (String searchDate : datelist) {

                if (datelist.get(0) == searchDate) {
                    beginDate = searchDate;
                    endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                } else if (searchDate == datelist.get(datelist.size() - 1)) {
                    beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                    endDate = searchDate;
                } else {
                    beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
                    endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss");
                }

                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("beginDate", beginDate);
                    map.put("endDate", endDate);
                    List<DatawareUserSignDay> hourList = platUserCheckLotteryLogService.findSignList(map);

                    for (DatawareUserSignDay item : hourList) {
                        item.setUserGroup(getUserGroup(item.getUserId(), uicGroupList));
                    }

                    if (CollectionUtils.isNotEmpty(hourList)) {
                        datawareUserSignDayService.batchSave(hourList);
                    }
                } catch (Exception e) {
                    logger.error("dataware_user_sign_day添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                }
            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }

    private void hourUserSign(List<Long> uicGroupList) {
        Calendar cal = Calendar.getInstance();
        //当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String beginDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm:ss");

        //结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        String endDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            List<DatawareUserSignDay> hourList = platUserCheckLotteryLogService.findSignList(map);

            for (DatawareUserSignDay item : hourList) {
                item.setUserGroup(getUserGroup(item.getUserId(), uicGroupList));
            }

            if (CollectionUtils.isNotEmpty(hourList)) {
                datawareUserSignDayService.batchSave(hourList);
            }
        } catch (Exception e) {
            logger.error("dataware_user_sign_day添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private int getUserGroup(Long userId, List<Long> uicGroupList) {
        Integer userGroupFlag;
        if (CollectionUtils.isNotEmpty(uicGroupList)) {
            if (uicGroupList.contains(userId)) {
                userGroupFlag = 1;
            } else {
                userGroupFlag = 2;
            }
        } else {
            userGroupFlag = 2;
        }
        return userGroupFlag;
    }
}
