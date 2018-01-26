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
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.service.*;
import com.wf.data.service.data.DatawareBettingLogHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class BettingLogHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportChangeNoteService reportChangeNoteService;
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private DataDictService dataDictService;
    @Autowired
    private DatawareBettingLogHourService datawareBettingLogHourService;
    @Autowired
    private TcardUserBettingLogService tcardUserBettingLogService;
    @Autowired
    private RoomFishInfoService roomFishInfoService;
    @Autowired
    private ChannelInfoService channelInfoService;

    public void toDoAnalysis() {
        logger.info("每小时投注汇总开始:traceId={}", TraceIdUtils.getTraceId());
        int flag = dataConfigService.getIntValueByName(DataConstants.DATA_DATAWARE_FLAG);
        if (flag == 1) {
            cleanBydays();
        } else {
            cleanByhour();
        }
        logger.info("每小时投注汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void cleanBydays() {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_DAYS);

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


        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
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

                Map<String, Object> params = new HashMap<>();
                params.put("beginDate", beginDate);
                params.put("endDate", endDate);
                //汇总游戏投注信息
                getBettingLogFromReport(params, uicGroupList);
                //汇总三张投注信息
                getBettingLogFromTcard(params, uicGroupList);
            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }

        //汇总捕鱼投注信息
        getFishInfo(DateUtils.parseDateTime(dates[0]), DateUtils.parseDateTime(dates[1]), uicGroupList);

    }

    private void cleanByhour() {

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

        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }


        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        //汇总游戏投注信息
        getBettingLogFromReport(params, uicGroupList);
        //汇总三张投注信息
        getBettingLogFromTcard(params, uicGroupList);
        //汇总捕鱼投注信息
        getFishInfo(cal.getTime(), calendar.getTime(), uicGroupList);
    }


    /**
     * 游戏投注信息
     * 1:梦想飞镖,2:梦想桌球,3:套圈,4:热血军团
     * 5:乱斗三国,8:摩托车,9:三国,12:糖果夺宝
     *
     * @param params
     * @return
     */
    private void getBettingLogFromReport(Map<String, Object> params, List<Long> uicGroupList) {
        try {

            String gameType = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_GAMETYPE);
            String[] gameTypes = gameType.split(",");
            params.put("gameTypes", Arrays.asList(gameTypes));
            List<DatawareBettingLogHour> bettingLogHourList = reportChangeNoteService.getGameBettingRecord(params);
            for (DatawareBettingLogHour logHour : bettingLogHourList) {
                logHour.setUserGroup(getUserGroup(logHour.getUserId(), uicGroupList));
                DataDict dataDict = dataDictService.getDictByValue("game_type", logHour.getGameType());
                if (null != dataDict) {
                    logHour.setGameName(dataDict.getLabel());
                }
                if (null != logHour.getChannelId()) {
                    ChannelInfo channelInfo = channelInfoService.get(logHour.getChannelId());
                    if (null != channelInfo) {
                        if (null == channelInfo.getParentId()) {
                            logHour.setParentId(logHour.getChannelId());
                        } else {
                            logHour.setParentId(channelInfo.getParentId());
                        }
                    }
                }
            }
            try {
                if (CollectionUtils.isNotEmpty(bettingLogHourList)) {
                    datawareBettingLogHourService.batchSave(bettingLogHourList);
                }
            } catch (Exception e) {
                logger.error("FromReport添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            }
        } catch (Exception e) {
            logger.error("FromReport获取记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }

    /**
     * 三张投注信息
     *
     * @param params
     * @return
     */
    private void getBettingLogFromTcard(Map<String, Object> params, List<Long> uicGroupList) {
        try {

            List<DatawareBettingLogHour> bettingLogHourList = tcardUserBettingLogService.getGameBettingRecord(params);
            for (DatawareBettingLogHour logHour : bettingLogHourList) {
                logHour.setUserGroup(getUserGroup(logHour.getUserId(), uicGroupList));
                logHour.setGameType(11);
                DataDict dataDict = dataDictService.getDictByValue("game_type", logHour.getGameType());
                if (null != dataDict) {
                    logHour.setGameName(dataDict.getLabel());
                }
                if (null != logHour.getChannelId()) {
                    ChannelInfo channelInfo = channelInfoService.get(logHour.getChannelId());
                    if (null != channelInfo) {
                        if (null == channelInfo.getParentId()) {
                            logHour.setParentId(logHour.getChannelId());
                        } else {
                            logHour.setParentId(channelInfo.getParentId());
                        }
                    }
                }
            }
            try {
                if (CollectionUtils.isNotEmpty(bettingLogHourList)) {
                    datawareBettingLogHourService.batchSave(bettingLogHourList);
                }
            } catch (Exception e) {
                logger.error("FromTcard添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            }
        } catch (Exception e) {
            logger.error("FromTcard获取记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

    }

    private void getFishInfo(Date startTime, Date endTime, List<Long> uicGroupList) {
        Map<String, Object> params = new HashMap<>();
        String startDate = DateUtils.formatDate(startTime, "yyyy-MM-dd HH:mm:ss");
        String endDate = DateUtils.formatDate(endTime, "yyyy-MM-dd HH:mm:ss");
        List<String> datelist = DateUtils.getDateList(startDate, endDate);

        if (DateUtils.formatDate(startTime, "yyyy-MM-dd").equals(DateUtils.formatDate(endTime, "yyyy-MM-dd"))) {
            params.put("beginDate", startDate);
            params.put("endDate", endDate);
            String day = DateUtils.formatDate(startTime, DateUtils.YYYYMMDD_PATTERN);
            String dbName = "fish";
            dbName = dbName + day;
            getBettingLogFromFish(params, dbName, uicGroupList);
        } else {
            for (String dat : datelist) {
                if (datelist.get(0) == dat) {
                    params.put("beginDate", startDate);
                    params.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(startTime), "yyyy-MM-dd HH:mm:ss"));
                    String day = DateUtils.formatDate(startTime, DateUtils.YYYYMMDD_PATTERN);
                    String dbName = "fish";
                    dbName = dbName + day;
                    getBettingLogFromFish(params, dbName, uicGroupList);
                } else if (dat == datelist.get(datelist.size() - 1)) {
                    params.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(endTime), "yyyy-MM-dd HH:mm:ss"));
                    params.put("endDate", endDate);
                    String day = DateUtils.formatDate(endTime, DateUtils.YYYYMMDD_PATTERN);
                    String dbName = "fish";
                    dbName = dbName + day;
                    getBettingLogFromFish(params, dbName, uicGroupList);
                } else {
                    params.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(dat, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                    params.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(dat, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                    String day = DateUtils.formatDate(DateUtils.parseDate(dat, "yyyy-MM-dd"), DateUtils.YYYYMMDD_PATTERN);
                    String dbName = "fish";
                    dbName = dbName + day;
                    getBettingLogFromFish(params, dbName, uicGroupList);
                }
            }

        }

    }

    /**
     * 捕鱼投注信息
     *
     * @param params
     * @return
     */
    private void getBettingLogFromFish(Map<String, Object> params, String dbName, List<Long> uicGroupList) {
        try {
            List<DatawareBettingLogHour> bettingLogHourList = roomFishInfoService.getGameBettingRecord(params, dbName);
            for (DatawareBettingLogHour logHour : bettingLogHourList) {
                logHour.setUserGroup(getUserGroup(logHour.getUserId(), uicGroupList));
                logHour.setGameType(10);
                DataDict dataDict = dataDictService.getDictByValue("game_type", logHour.getGameType());
                if (null != dataDict) {
                    logHour.setGameName(dataDict.getLabel());
                }
                if (null != logHour.getChannelId()) {
                    ChannelInfo channelInfo = channelInfoService.get(logHour.getChannelId());
                    if (null != channelInfo) {
                        if (null == channelInfo.getParentId()) {
                            logHour.setParentId(logHour.getChannelId());
                        } else {
                            logHour.setParentId(channelInfo.getParentId());
                        }
                    }
                }
            }
            try {
                if (CollectionUtils.isNotEmpty(bettingLogHourList)) {
                    datawareBettingLogHourService.batchSave(bettingLogHourList);
                }
            } catch (Exception e) {
                logger.error("FromFish添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            }
        } catch (Exception e) {
            logger.error("FromFish获取记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
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


    @Async
    public void dataClean(String startTime, String endTime) {
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }

        try {
            String startDay = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
            String endDay = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
            String startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");
            String endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
            if (startDay.equals(endDay)) {
                Map<String, Object> params = new HashMap<>();
                params.put("bettingDate", startDay);
                params.put("startHour", startHour);
                params.put("endHour", endHour);

                long count = datawareBettingLogHourService.getCountByTime(params);
                if (count > 0) {
                    datawareBettingLogHourService.deleteByDate(params);
                }


                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", startTime);
                map.put("endDate", endTime);
                //汇总游戏投注信息
                getBettingLogFromReport(map, uicGroupList);
                //汇总三张投注信息
                getBettingLogFromTcard(map, uicGroupList);
                //汇总捕鱼投注信息
                getFishInfo(DateUtils.parseDateTime(startTime), DateUtils.parseDateTime(endTime), uicGroupList);

            } else {
                forDay(startTime, endTime, uicGroupList);
            }

        } catch (Exception e) {
            logger.error("错误: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }


        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void forDay(String startTime, String endTime, List<Long> uicGroupList) {
        List<String> datelist = DateUtils.getDateList(startTime, endTime);
        String beginDate = "";
        String endDate = "";
        String bettingDate = "";
        String startHour = "";
        String endHour = "";

        for (String searchDate : datelist) {

            if (datelist.get(0) == searchDate) {
                beginDate = searchDate;
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                bettingDate = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
                startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");

            } else if (searchDate == datelist.get(datelist.size() - 1)) {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = searchDate;

                bettingDate = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
                endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
                startHour = "";
            } else {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");

                bettingDate = DateUtils.formatDate(DateUtils.parseDate(searchDate));
                endHour = "";
                startHour = "";
            }


            Map<String, Object> params = new HashMap<>();
            params.put("bettingDate", bettingDate);
            params.put("startHour", startHour);
            params.put("endHour", endHour);

            long count = datawareBettingLogHourService.getCountByTime(params);
            if (count > 0) {
                datawareBettingLogHourService.deleteByDate(params);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            //汇总游戏投注信息
            getBettingLogFromReport(params, uicGroupList);
            //汇总三张投注信息
            getBettingLogFromTcard(params, uicGroupList);
            //汇总捕鱼投注信息
        }
        getFishInfo(DateUtils.parseDateTime(startTime), DateUtils.parseDateTime(endTime), uicGroupList);
    }
}
