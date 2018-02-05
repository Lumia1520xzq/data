package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
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
public class ChannelRetentionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareFinalChannelRetentionService datawareFinalChannelRetentionService;

    public void toDoChannelRetentionAnalysis() {
        logger.info("用户留存分析开始:traceId={}", TraceIdUtils.getTraceId());

        boolean flag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DESTINATION_RETENTION_FLAG);
        if (true == flag) {
            //前天
            String searchDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -2));
            channelRetention(searchDate);
        }

        logger.info("用户留存分析结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void channelRetention(String searchDay) {
        Map<String, Object> channelParams = new HashMap<>();
        channelParams.put("businessDate", searchDay);
        long count = datawareFinalChannelRetentionService.getCountByTime(channelParams);
        if (count > 0) {
            datawareFinalChannelRetentionService.deleteByDate(channelParams);
        }
        String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
        if (StringUtils.isBlank(channelIdList)) {
            logger.error("渠道未设置: traceId={}", TraceIdUtils.getTraceId());
            return;
        }
        try {
            Map<String, Object> mapAll = new HashMap<>();
            mapAll.put("businessDate", searchDay);
            dataRetention(mapAll, null, searchDay, 1);

            List<String> channels = Arrays.asList(channelIdList.split(","));

            List<Long> childChannelList = Lists.newArrayList();
            List<Long> parentChannelList = Lists.newArrayList();


            for (String channelStr : channels) {
                Long channel = Long.valueOf(channelStr);
                ChannelInfo channelInfo = channelInfoService.get(channel);
                if (null != channelInfo) {
                    if (null != channelInfo.getParentId()) {
                        childChannelList.add(channel);
                        Map<String, Object> map = new HashMap<>();
                        map.put("businessDate", searchDay);
                        map.put("channelId", channel);
                        dataRetention(map, channelInfo, searchDay, 0);
                    } else {
                        parentChannelList.add(channel);
                        Map<String, Object> parentMap = new HashMap<>();
                        parentMap.put("businessDate", searchDay);
                        parentMap.put("parentId", channel);
                        dataRetention(parentMap, channelInfo, searchDay, 0);
                    }
                }

            }

            //汇总其他渠道
            parentChannelList.add(1L);
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", searchDay);
            params.put("childChannelList", childChannelList);
            params.put("parentChannelList", parentChannelList);
            dataRetention(params, null, searchDay, 0);


        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void dataRetention(Map<String, Object> params, ChannelInfo channelInfo, String businessDate, Integer flag) {

        DatawareFinalChannelRetention retention = new DatawareFinalChannelRetention();
        retention.setBusinessDate(businessDate);
        if (null == channelInfo) {
            if (flag == 0) {
                retention.setChannelId(0L);
                retention.setParentId(0L);
                retention.setChannelName("其他");
            } else {
                retention.setChannelId(1L);
                retention.setParentId(1L);
                retention.setChannelName("全部");
            }
        } else {
            retention.setChannelName(channelInfo.getName());
            retention.setChannelId(channelInfo.getId());
            if (null == channelInfo.getParentId()) {
                retention.setParentId(channelInfo.getId());
            } else {
                retention.setParentId(channelInfo.getParentId());
            }
        }

        //当天的日活用户列表
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(params);
        //当天的注册用户列表
        List<Long> newUserList = datawareUserInfoService.getNewUserByDate(params);

        //新用户占比=当日新增用户数/当日DAU
        if (CollectionUtils.isNotEmpty(dauUserList)) {
            retention.setDau(Long.valueOf(dauUserList.size()));
        } else {
            retention.setDau(0L);
        }
        if (CollectionUtils.isNotEmpty(newUserList)) {
            retention.setNewUsers(Long.valueOf(newUserList.size()));
        } else {
            retention.setNewUsers(0L);
        }
        if (retention.getDau() > 0) {
            retention.setUsersRate(BigDecimalUtil.div(newUserList.size() * 100, dauUserList.size(), 2));
        } else {
            retention.setUsersRate(0.00);
        }


        //次日活跃用户列表
        String searchDay = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(businessDate), 1), DateUtils.DATE_PATTERN);
        params.put("businessDate", searchDay);
        List<Long> nextDayDauUserList = datawareBuryingPointDayService.getUserIdListByChannel(params);


        Collection interColl = CollectionUtils.intersection(newUserList, nextDayDauUserList);
        //新用户且次日活跃的用户
        List<Long> userList = (List<Long>) interColl;
        int userCount = 0;
        if (null != userList) {
            userCount = userList.size();
        }
        if (retention.getNewUsers() > 0) {
            retention.setUsersDayRetention(BigDecimalUtil.div(userCount * 100, retention.getNewUsers(), 2));
        } else {
            retention.setUsersDayRetention(0.00);
        }

        Collection dauInterColl = CollectionUtils.intersection(dauUserList, nextDayDauUserList);
        //活跃用户且次日活跃的用户
        List<Long> dauList = (List<Long>) dauInterColl;
        int dauCount = 0;
        if (null != dauList) {
            dauCount = dauList.size();
        }
        if (retention.getDau() > 0) {
            retention.setDayRetention(BigDecimalUtil.div(dauCount * 100, retention.getDau(), 2));
        } else {
            retention.setDayRetention(0.00);
        }

        try {
            datawareFinalChannelRetentionService.save(retention);
        } catch (Exception e) {
            logger.error("添加用户留存记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(retention.toString()));
        }

    }

    @Async
    public void dataClean(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                channelRetention(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    channelRetention(searchDate);
                }
            }
        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }
}
