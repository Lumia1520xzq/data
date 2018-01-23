package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
@Service
public class ChannelInfoHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DatawareFinalChannelInfoHourService channelInfoHourService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareBuryingPointHourService buryingPointHourService;
    @Autowired
    private DatawareConvertHourService convertHourService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBettingLogHourService bettingLogHourService;

    public void toDoAnalysis() {
        logger.info("每天数据汇总开始:traceId={}", TraceIdUtils.getTraceId());

        //当前日期向前推1小时，保证统计昨天数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        String searchDay = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");

        channelInfo(searchDay, searchHour);
        logger.info("每天数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void channelInfo(String businessDate, String businessHour) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", businessDate);
            params.put("businessHour", businessHour);
            dataKettle(null, businessDate, businessHour);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            //判断数据是否存在
            for (ChannelInfo item : channelInfoList) {
                params.put("parentId", item.getId());
                long count = channelInfoHourService.getCountByTime(params);
                if (count > 0) {
                    continue;
                } else {
                    dataKettle(item, businessDate, businessHour);
                }
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void dataKettle(ChannelInfo channelInfo, String businessDate, String businessHour) {
        DatawareFinalChannelInfoHour info = new DatawareFinalChannelInfoHour();
        info.setBusinessDate(businessDate);
        info.setBusinessHour(businessHour);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());

        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }
        //日活
        Map<String, Object> params = new HashMap<>();
        params.put("buryingDate", businessDate);
        params.put("buryingHour", businessHour);
        if (null != channelInfo) {
            params.put("parentId", info.getParentId());
        }
        Integer hourDau = buryingPointHourService.getDauByDateAndHour(params);
        if (null == hourDau) hourDau = 0;
        info.setHourDau(Long.valueOf(hourDau));

        Long dau = buryingPointHourService.getDauByTime(params);
        if (null == dau) dau = 0L;
        info.setDau(dau);

        //充值
        Map<String, Object> regParams = new HashMap<>();
        regParams.put("convertDate", businessDate);
        regParams.put("convertHour", businessHour);
        if (null != channelInfo) {
            regParams.put("parentId", info.getParentId());
        }
        DatawareFinalChannelInfoHour convertInfo = convertHourService.findRechargeByTime(regParams);
        if (null != convertInfo) {
            if (null == convertInfo.getRechargeCount()) {
                info.setRechargeCount(0L);
            } else {
                info.setRechargeCount(convertInfo.getRechargeCount());
            }
            if (null == convertInfo.getRechargeAmount()) {
                info.setRechargeAmount(0.00);
            } else {
                info.setRechargeAmount(convertInfo.getRechargeAmount());
            }
        }

        DatawareFinalChannelInfoHour rechargeInfo = convertHourService.findRechargeByDate(regParams);
        if (null != rechargeInfo) {
            if (null == rechargeInfo.getRechargeCount()) {
                info.setHourRechargeCount(0L);
            } else {
                info.setHourRechargeCount(rechargeInfo.getRechargeCount());
            }
            if (null == rechargeInfo.getRechargeAmount()) {
                info.setHourRechargeAmount(0.00);
            } else {
                info.setHourRechargeAmount(rechargeInfo.getRechargeAmount());
            }
        }

        //新增用户数
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("businessDate", businessDate);
        userParams.put("businessHour", businessHour);
        if (null != channelInfo) {
            userParams.put("parentId", info.getParentId());
        }
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(userParams);
        if (CollectionUtils.isNotEmpty(newUserList)) {
            info.setHourNewUsers(Long.valueOf(newUserList.size()));
        } else {
            info.setHourNewUsers(0L);

        }

        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(userParams);
        if (CollectionUtils.isNotEmpty(dayNewUserList)) {
            info.setNewUsers(Long.valueOf(dayNewUserList.size()));
        } else {
            info.setNewUsers(0L);

        }


        //投注人数list
        DatawareBettingLogHour hour = new DatawareBettingLogHour();
        hour.setBettingDate(businessDate);
        hour.setBettingHour(businessHour);
        if (null != channelInfo) {
            hour.setParentId(info.getParentId());
        }
        hour.setUserGroup(2);
        List<Long> beetingUserIdList = bettingLogHourService.findUserId(hour);
        Collection interColl = CollectionUtils.intersection(beetingUserIdList, newUserList);
        //当日投注新增用户数
        List<Long> bettingNewUsers = (List<Long>) interColl;
        if (null != bettingNewUsers) {
            info.setHourUserBettingCount(Long.valueOf(bettingNewUsers.size()));
        } else {
            info.setHourUserBettingCount(0L);
        }

        List<Long> beetingList = bettingLogHourService.findUserIdByTime(hour);
        Collection beetingInterColl = CollectionUtils.intersection(beetingList, newUserList);
        List<Long> bettingCount = (List<Long>) beetingInterColl;
        if (null != bettingCount) {
            info.setUserBettingCount(Long.valueOf(bettingCount.size()));
        } else {
            info.setUserBettingCount(0L);
        }


        //投注
        Map<String, Object> bettingParams = new HashMap<>();
        bettingParams.put("bettingDate", businessDate);
        bettingParams.put("bettingHour", businessHour);
        if (null != channelInfo) {
            bettingParams.put("parentId", info.getParentId());
        }
        DatawareBettingLogHour bettingInfo = bettingLogHourService.getSumByDateAndHour(bettingParams);
        if (null != bettingInfo) {
            if (null == bettingInfo.getBettingAmount()) {
                info.setHourBettingAmount(0.00);
            } else {
                info.setHourBettingAmount(bettingInfo.getBettingAmount());
            }
            if (null == bettingInfo.getBettingUserCount()) {
                info.setHourUserCount(0L);
            } else {
                info.setHourUserCount(Long.valueOf(bettingInfo.getBettingUserCount()));
            }
            if (null == bettingInfo.getBettingCount()) {
                info.setHourBettingCount(0L);
            } else {
                info.setHourBettingCount(Long.valueOf(bettingInfo.getBettingCount()));
            }
            if (null == bettingInfo.getResultAmount()) {
                info.setHourResultAmount(0.00);
            } else {
                info.setHourResultAmount(bettingInfo.getResultAmount());
            }

            info.setHourDiffAmount(BigDecimalUtil.round(BigDecimalUtil.sub(info.getHourBettingAmount(), info.getHourResultAmount()), 2));
        }

        DatawareBettingLogHour dayBettingInfo = bettingLogHourService.getBettingByDate(bettingParams);
        if (null != dayBettingInfo) {
            if (null == dayBettingInfo.getBettingAmount()) {
                info.setBettingAmount(0.00);
            } else {
                info.setBettingAmount(dayBettingInfo.getBettingAmount());
            }
            if (null == dayBettingInfo.getBettingUserCount()) {
                info.setUserCount(0L);
            } else {
                info.setUserCount(Long.valueOf(dayBettingInfo.getBettingUserCount()));
            }
            if (null == dayBettingInfo.getBettingCount()) {
                info.setBettingCount(0L);
            } else {
                info.setBettingCount(Long.valueOf(dayBettingInfo.getBettingCount()));
            }
            if (null == dayBettingInfo.getResultAmount()) {
                info.setResultAmount(0.00);
            } else {
                info.setResultAmount(dayBettingInfo.getResultAmount());
            }
            info.setDiffAmount(BigDecimalUtil.round(BigDecimalUtil.sub(info.getBettingAmount(), info.getResultAmount()), 2));
        }


        try {
            channelInfoHourService.save(info);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }

    }


    @Async
    public void dataClean(List<String> datelist) {
        if (DateUtils.formatDate(DateUtils.parseDate(datelist.get(0)), "yyyy-MM-dd").equals(DateUtils.formatDate(DateUtils.parseDate(datelist.get(datelist.size() - 1)), "yyyy-MM-dd"))) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(datelist.get(0), DateUtils.DATE_TIME_PATTERN));
            String beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(datelist.get(0), DateUtils.DATE_PATTERN)), DateUtils.DATE_PATTERN);
            String beginHour = DateUtils.formatDate(calendar.getTime(), "HH");

            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtils.parseDate(datelist.get(datelist.size() - 1), DateUtils.DATE_TIME_PATTERN));
            String endHour = DateUtils.formatDate(cal.getTime(), "HH");
            if (beginHour.equals(endHour)) {
                channelInfo(beginDate, beginHour);
            } else {
                while (!beginHour.equals(endHour)) {
                    channelInfo(beginDate, beginHour);
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    beginHour = DateUtils.formatDate(calendar.getTime(), "HH");
                }
            }

        } else {
            for (String searchDate : datelist) {
                if (datelist.get(0) == searchDate) {

                    String searchDay = DateUtils.formatDate(DateUtils.parseDate(searchDate, DateUtils.DATE_TIME_PATTERN), DateUtils.DATE_PATTERN);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtils.parseDate(searchDate, DateUtils.DATE_TIME_PATTERN));
                    String date = searchDay;
                    while (searchDay.equals(date)) {

                        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
                        channelInfo(searchDay, searchHour);
                        calendar.add(Calendar.HOUR_OF_DAY, 1);
                        date = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
                    }

                } else if (searchDate == datelist.get(datelist.size() - 1)) {
                    String searchDay = DateUtils.formatDate(DateUtils.parseDate(searchDate, DateUtils.DATE_TIME_PATTERN), DateUtils.DATE_PATTERN);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtils.parseDate(searchDate, DateUtils.DATE_TIME_PATTERN));
                    String date = searchDay;
                    while (searchDay.equals(date)) {

                        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
                        channelInfo(searchDay, searchHour);
                        calendar.add(Calendar.HOUR_OF_DAY, -1);
                        date = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
                    }
                } else {
                    String searchDay = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, DateUtils.DATE_PATTERN)), DateUtils.DATE_PATTERN);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, DateUtils.DATE_PATTERN)));
                    String date = searchDay;
                    while (searchDay.equals(date)) {

                        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
                        channelInfo(searchDay, searchHour);
                        calendar.add(Calendar.HOUR_OF_DAY, 1);
                        date = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
                    }
                }
            }
        }

        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }
}
