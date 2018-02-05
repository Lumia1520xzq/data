package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareFinalRegisteredRetentionService;
import com.wf.data.service.data.DatawareUserInfoService;
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
public class RegisteredRetentionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalRegisteredRetentionService datawareFinalRegisteredRetentionService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;

    public void toDoAnalysis() {
        logger.info("用户RegisteredRetention分析开始:traceId={}", TraceIdUtils.getTraceId());

        String searchDate = DateUtils.getYesterdayDate();
        RegisteredRetention(searchDate);
        logger.info("用户RegisteredRetention分析结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void RegisteredRetention(String businessDate) {
        try {
            dataKettle(null, businessDate);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                dataKettle(item, businessDate);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void dataKettle(ChannelInfo channelInfo, String businessDate) {
        DatawareFinalRegisteredRetention info = new DatawareFinalRegisteredRetention().toInit();
        info.setBusinessDate(businessDate);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }

        Map<String, Object> registeredParams = new HashMap<>();
        registeredParams.put("businessDate", businessDate);
        if (channelInfo != null) {
            registeredParams.put("parentId", channelInfo.getId());
        } else {
            registeredParams.put("parentId", 1);
        }

        DatawareFinalRegisteredRetention retention = datawareFinalRegisteredRetentionService.getRetentionByDate(registeredParams);
        if (null != retention) {
            info = retention;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", businessDate);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        }
        //注册用户数
        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(params);
        if (CollectionUtils.isNotEmpty(dayNewUserList)) {
            info.setNewUsers(Long.valueOf(dayNewUserList.size()));
        } else {
            info.setNewUsers(0L);
        }
        try {
            datawareFinalRegisteredRetentionService.save(info);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(params);

        for (int i = 1; i <= 14; i++) {
            daysRetention(dauUserList, channelInfo, businessDate, i);
        }

    }

    private void daysRetention(List<Long> dauUserList, ChannelInfo channelInfo, String businessDate, int i) {

        Map<String, Object> params = new HashMap<>();
        String registeredDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(businessDate), -i));
        params.put("businessDate", registeredDate);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }

        DatawareFinalRegisteredRetention retention = datawareFinalRegisteredRetentionService.getRetentionByDate(params);
        if (null == retention) {
            return;
        }

        Map<String, Object> registeredParams = new HashMap<>();
        params.put("businessDate", registeredDate);
        if (channelInfo != null) {
            registeredParams.put("parentId", channelInfo.getId());
        }
        //注册用户数
        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(params);
        Collection interColl = CollectionUtils.intersection(dayNewUserList, dauUserList);
        //新用户且次日活跃的用户
        List<Long> userList = (List<Long>) interColl;
        int userCount = 0;
        if (null != userList) {
            userCount = userList.size();
        }
        if (i == 1) {
            retention.setRegisteredDau2(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention2(BigDecimalUtil.div(retention.getRegisteredDau2(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention2(0.00);
            }
        } else if (i == 2) {
            retention.setRegisteredDau3(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention3(BigDecimalUtil.div(retention.getRegisteredDau3(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention3(0.00);
            }
        } else if (i == 3) {
            retention.setRegisteredDau4(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention4(BigDecimalUtil.div(retention.getRegisteredDau4(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention4(0.00);
            }
        } else if (i == 4) {
            retention.setRegisteredDau5(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention5(BigDecimalUtil.div(retention.getRegisteredDau5(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention5(0.00);
            }
        } else if (i == 5) {
            retention.setRegisteredDau6(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention6(BigDecimalUtil.div(retention.getRegisteredDau6(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention6(0.00);
            }
        } else if (i == 6) {
            retention.setRegisteredDau7(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention7(BigDecimalUtil.div(retention.getRegisteredDau7(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention7(0.00);
            }
        } else if (i == 7) {
            retention.setRegisteredDau8(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention8(BigDecimalUtil.div(retention.getRegisteredDau8(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention8(0.00);
            }
        } else if (i == 8) {
            retention.setRegisteredDau9(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention9(BigDecimalUtil.div(retention.getRegisteredDau9(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention9(0.00);
            }
        } else if (i == 9) {
            retention.setRegisteredDau10(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention10(BigDecimalUtil.div(retention.getRegisteredDau10(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention10(0.00);
            }
        } else if (i == 10) {
            retention.setRegisteredDau11(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention11(BigDecimalUtil.div(retention.getRegisteredDau11(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention11(0.00);
            }
        } else if (i == 11) {
            retention.setRegisteredDau12(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention12(BigDecimalUtil.div(retention.getRegisteredDau12(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention12(0.00);
            }
        } else if (i == 12) {
            retention.setRegisteredDau13(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention13(BigDecimalUtil.div(retention.getRegisteredDau13(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention13(0.00);
            }
        } else if (i == 13) {
            retention.setRegisteredDau14(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention14(BigDecimalUtil.div(retention.getRegisteredDau14(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention14(0.00);
            }
        } else if (i == 14) {
            retention.setRegisteredDau15(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention15(BigDecimalUtil.div(retention.getRegisteredDau15(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention15(0.00);
            }
        }

        try {
            datawareFinalRegisteredRetentionService.save(retention);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(retention.toString()));
        }
    }


    @Async
    public void dataClean(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                historyRegisteredRetention(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    historyRegisteredRetention(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void historyRegisteredRetention(String businessDate) {
        try {
            historyDataKettle(null, businessDate);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                historyDataKettle(item, businessDate);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


    private void historyDataKettle(ChannelInfo channelInfo, String businessDate) {
        DatawareFinalRegisteredRetention info = new DatawareFinalRegisteredRetention().toInit();
        info.setBusinessDate(businessDate);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }

        Map<String, Object> registeredParams = new HashMap<>();
        registeredParams.put("businessDate", businessDate);
        if (channelInfo != null) {
            registeredParams.put("parentId", channelInfo.getId());
        } else {
            registeredParams.put("parentId", 1);
        }

        DatawareFinalRegisteredRetention retention = datawareFinalRegisteredRetentionService.getRetentionByDate(registeredParams);
        if (null != retention) {
            info = retention;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", businessDate);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        }
        //注册用户数
        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(params);
        if (CollectionUtils.isNotEmpty(dayNewUserList)) {
            info.setNewUsers(Long.valueOf(dayNewUserList.size()));
        } else {
            info.setNewUsers(0L);
        }


        if (info.getNewUsers() > 0) {
            for (int i = 1; i <= 14; i++) {
                info = getRetention(info, dayNewUserList, channelInfo, businessDate, i);
            }
        }

        try {
            datawareFinalRegisteredRetentionService.save(info);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }

    }


    private DatawareFinalRegisteredRetention getRetention(DatawareFinalRegisteredRetention retention, List<Long> dayNewUserList, ChannelInfo channelInfo, String businessDate, int i) {

        Date endDate = DateUtils.getNextDate(DateUtils.parseDate(businessDate), i);
        String endDateStr = DateUtils.formatDate(endDate);
        if (endDate.getTime() >= DateUtils.parseDate(DateUtils.formatCurrentDateYMD()).getTime()) {
            return retention;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", endDateStr);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        }

        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(params);
        Collection interColl = CollectionUtils.intersection(dayNewUserList, dauUserList);
        //新用户且活跃的用户
        List<Long> userList = (List<Long>) interColl;
        int userCount = 0;
        if (null != userList) {
            userCount = userList.size();
        }

        if (i == 1) {
            retention.setRegisteredDau2(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention2(BigDecimalUtil.div(retention.getRegisteredDau2(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention2(0.00);
            }
        } else if (i == 2) {
            retention.setRegisteredDau3(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention3(BigDecimalUtil.div(retention.getRegisteredDau3(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention3(0.00);
            }
        } else if (i == 3) {
            retention.setRegisteredDau4(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention4(BigDecimalUtil.div(retention.getRegisteredDau4(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention4(0.00);
            }
        } else if (i == 4) {
            retention.setRegisteredDau5(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention5(BigDecimalUtil.div(retention.getRegisteredDau5(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention5(0.00);
            }
        } else if (i == 5) {
            retention.setRegisteredDau6(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention6(BigDecimalUtil.div(retention.getRegisteredDau6(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention6(0.00);
            }
        } else if (i == 6) {
            retention.setRegisteredDau7(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention7(BigDecimalUtil.div(retention.getRegisteredDau7(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention7(0.00);
            }
        } else if (i == 7) {
            retention.setRegisteredDau8(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention8(BigDecimalUtil.div(retention.getRegisteredDau8(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention8(0.00);
            }
        } else if (i == 8) {
            retention.setRegisteredDau9(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention9(BigDecimalUtil.div(retention.getRegisteredDau9(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention9(0.00);
            }
        } else if (i == 9) {
            retention.setRegisteredDau10(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention10(BigDecimalUtil.div(retention.getRegisteredDau10(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention10(0.00);
            }
        } else if (i == 10) {
            retention.setRegisteredDau11(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention11(BigDecimalUtil.div(retention.getRegisteredDau11(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention11(0.00);
            }
        } else if (i == 11) {
            retention.setRegisteredDau12(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention12(BigDecimalUtil.div(retention.getRegisteredDau12(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention12(0.00);
            }
        } else if (i == 12) {
            retention.setRegisteredDau13(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention13(BigDecimalUtil.div(retention.getRegisteredDau13(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention13(0.00);
            }
        } else if (i == 13) {
            retention.setRegisteredDau14(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention14(BigDecimalUtil.div(retention.getRegisteredDau14(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention14(0.00);
            }
        } else if (i == 14) {
            retention.setRegisteredDau15(Long.valueOf(userCount));
            if(retention.getNewUsers() >0){
                retention.setRetention15(BigDecimalUtil.div(retention.getRegisteredDau15(), retention.getNewUsers(), 2));
            }else {
                retention.setRetention15(0.00);
            }
        }

        return retention;


    }

}
