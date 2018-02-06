package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.data.DatawareUserInfoService;
import org.apache.commons.codec.binary.Base64;
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
public class UserRegisteredHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private UicUserService uicUserService;
    @Autowired
    private ChannelInfoService channelInfoService;

    public void toDoAnalysis() {
        logger.info("每小时注册汇总开始:traceId={}", TraceIdUtils.getTraceId());
        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }
        boolean userinfoFlag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_USERINFO_FLAG_DAY);

        if (true == userinfoFlag) {
            hourUserInfo(uicGroupList);
        }


        logger.info("每小时注册汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void hourUserInfo(List<Long> uicGroupList) {
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

        String searchDay = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
        Map<String, Object> params = new HashMap<>();
        params.put("registeredDate", searchDay);
        params.put("registeredHour", searchHour);
        long count = datawareUserInfoService.getCountByTime(params);
        if (count > 0) {
            datawareUserInfoService.deleteByDate(params);
        }

        if ("23".equals(searchHour)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userGroup", 2);
            datawareUserInfoService.updateUserGroup(userMap);
            if (CollectionUtils.isNotEmpty(uicGroupList)) {
                userMap.put("userGroup", 1);
                userMap.put("userList", uicGroupList);
                datawareUserInfoService.updateUserGroup(userMap);
            }
        }

        UserInfo(map, uicGroupList);

    }

    private void UserInfo(Map<String, Object> map, List<Long> uicGroupList) {
        try {

            List<DatawareUserInfo> userInfoList = uicUserService.findUserInfoByTime(map);

            for (DatawareUserInfo item : userInfoList) {
                item.setUserGroup(getUserGroup(item.getUserId(), uicGroupList));
                if (null != item.getChannelId()) {
                    if (item.getChannelId() == 100001) {
                        if (StringUtils.isNotEmpty(item.getThirdId())) {
                            String thirdId = new String(Base64.decodeBase64(item.getThirdId().getBytes("UTF-8")));
                            item.setThirdId(thirdId);
                        }
                    }
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

            if (CollectionUtils.isNotEmpty(userInfoList)) {
                datawareUserInfoService.batchSave(userInfoList);
            }
        } catch (Exception e) {
            logger.error("dataware_user_info添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
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
                params.put("registeredDate", startDay);
                params.put("startHour", startHour);
                params.put("endHour", endHour);

                long count = datawareUserInfoService.getCountByTime(params);
                if (count > 0) {
                    datawareUserInfoService.deleteByDate(params);
                }


                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", startTime);
                map.put("endDate", endTime);
                UserInfo(map, uicGroupList);
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
        String registeredDate = "";
        String startHour = "";
        String endHour = "";

        for (String searchDate : datelist) {

            if (datelist.get(0) == searchDate) {
                beginDate = searchDate;
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                registeredDate = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
                startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");

            } else if (searchDate == datelist.get(datelist.size() - 1)) {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = searchDate;

                registeredDate = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
                endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
                startHour = "";
            } else {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");

                registeredDate = DateUtils.formatDate(DateUtils.parseDate(searchDate));
                endHour = "";
                startHour = "";
            }


            Map<String, Object> params = new HashMap<>();
            params.put("registeredDate", registeredDate);
            params.put("startHour", startHour);
            params.put("endHour", endHour);

            long count = datawareUserInfoService.getCountByTime(params);
            if (count > 0) {
                datawareUserInfoService.deleteByDate(params);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            UserInfo(map, uicGroupList);
        }
    }
}
