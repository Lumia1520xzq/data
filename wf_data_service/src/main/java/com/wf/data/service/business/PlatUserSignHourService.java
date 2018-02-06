package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserSignDay;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.data.DatawareUserSignDayService;
import com.wf.data.service.platform.PlatSignedUserService;
import com.wf.data.service.platform.PlatUserCheckLotteryLogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class PlatUserSignHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private DatawareUserSignDayService datawareUserSignDayService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private PlatSignedUserService platSignedUserService;
    @Autowired
    private PlatUserCheckLotteryLogService platUserCheckLotteryLogService;

    private void sign(Map<String, Object> map, List<Long> uicGroupList) {
        try {
            List<DatawareUserSignDay> list = Lists.newArrayList();

            List<DatawareUserSignDay> hourList = platUserCheckLotteryLogService.findSignList(map);
            if (CollectionUtils.isNotEmpty(hourList)) {
                list.addAll(hourList);
            }
            List<DatawareUserSignDay> oldList = platSignedUserService.findListFromSignedUser(map);
            if (CollectionUtils.isNotEmpty(oldList)) {
                list.addAll(oldList);
            }

            for (DatawareUserSignDay item : list) {
                item.setUserGroup(getUserGroup(item.getUserId(), uicGroupList));

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

            if (CollectionUtils.isNotEmpty(list)) {
                datawareUserSignDayService.batchSave(list);
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
                params.put("signDate", startDay);
                params.put("startHour", startHour);
                params.put("endHour", endHour);

                long count = datawareUserSignDayService.getCountByTime(params);
                if (count > 0) {
                    datawareUserSignDayService.deleteByDate(params);
                }


                Map<String, Object> map = new HashMap<>();
                map.put("beginDate", startTime);
                map.put("endDate", endTime);
                sign(map, uicGroupList);
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
        String signDate = "";
        String startHour = "";
        String endHour = "";

        for (String searchDate : datelist) {

            if (datelist.get(0) == searchDate) {
                beginDate = searchDate;
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");

                signDate = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "yyyy-MM-dd");
                startHour = DateUtils.formatDate(DateUtils.parseDateTime(startTime), "HH");

            } else if (searchDate == datelist.get(datelist.size() - 1)) {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = searchDate;

                signDate = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "yyyy-MM-dd");
                endHour = DateUtils.formatDate(DateUtils.parseDateTime(endTime), "HH");
                startHour = "";
            } else {
                beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");
                endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate)), "yyyy-MM-dd HH:mm:ss");

                signDate = DateUtils.formatDate(DateUtils.parseDate(searchDate));
                endHour = "";
                startHour = "";
            }


            Map<String, Object> params = new HashMap<>();
            params.put("signDate", signDate);
            params.put("startHour", startHour);
            params.put("endHour", endHour);

            long count = datawareUserSignDayService.getCountByTime(params);
            if (count > 0) {
                datawareUserSignDayService.deleteByDate(params);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            sign(map, uicGroupList);
        }
    }
}
