package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.data.entity.DatawareBuryingPointHour;
import com.wf.data.service.*;
import com.wf.data.service.data.DatawareBuryingPointHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class BuryingPointHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private DataDictService dataDictService;
    @Autowired
    private BuryingPointService buryingPointService;
    @Autowired
    private DatawareBuryingPointHourService datawareBuryingPointHourService;
    @Autowired
    private ChannelInfoService channelInfoService;

    public void toDoAnalysis() {
        logger.info("每小时埋点汇总开始:traceId={}", TraceIdUtils.getTraceId());
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }

        boolean buryingFlag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_BURYING_FLAG_HOUR);
        if (false == buryingFlag) {
            historyBuryingPoint(uicGroupList);
        } else {
            hourBuryingPoint(uicGroupList);
        }


        logger.info("每小时埋点汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyBuryingPoint(List<Long> uicGroupList) {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_BURYING_HISTORY_HOUR);

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

                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", beginDate);
                map.put("endDate", endDate);
                buryingPoint(map, uicGroupList);
            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }

    private void hourBuryingPoint(List<Long> uicGroupList) {
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
        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        buryingPoint(map, uicGroupList);
    }

    private void buryingPoint(Map<String, Object> map, List<Long> uicGroupList) {
        try {

            List<DatawareBuryingPointHour> hourList = buryingPointService.findBuryingHourList(map);

            for (DatawareBuryingPointHour item : hourList) {
                item.setUserGroup(getUserGroup(item.getUserId(), uicGroupList));
                DataDict dataDict = dataDictService.getDictByValue("game_type", item.getGameType());
                if (null != dataDict) {
                    item.setGameName(dataDict.getLabel());
                }
                if (null != item.getChannelId()) {
                    ChannelInfo channelInfo = channelInfoService.get(item.getChannelId());
                    if (null != channelInfo) {
                        if (null == channelInfo.getParentId()) {
                            item.setParentId(item.getChannelId());
                        } else {
                            item.setParentId(channelInfo.getParentId());
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(hourList)) {
                Map<String, Object> params = new HashMap<>();
                params.put("buryingDate", hourList.get(0).getBuryingDate());
                params.put("buryingHour", hourList.get(0).getBuryingHour());
                long count = datawareBuryingPointHourService.getCountByTime(params);
                if (count <= 0) {
                    datawareBuryingPointHourService.batchSave(hourList);
                }
            }
        } catch (Exception e) {
            logger.error("buryingPointHour添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
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
