package com.wf.data.service.business;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
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
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class UserSignHourService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private PlatUserCheckLotteryLogService platUserCheckLotteryLogService;
    @Autowired
    private DatawareUserSignDayService datawareUserSignDayService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private PlatSignedUserService platSignedUserService;

    public void toDoAnalysis() {
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
        if (true == buryingFlag) {
            hourUserSign(uicGroupList);
        }


        logger.info("每小时签到汇总结束:traceId={}", TraceIdUtils.getTraceId());
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

        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);

        //
        String searchDay = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");
        if ("23".equals(searchHour)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userGroup", 2);
            userMap.put("signDate", searchDay);
            datawareUserSignDayService.updateUserGroup(userMap);
            if (CollectionUtils.isNotEmpty(uicGroupList)) {
                userMap.put("userGroup", 1);
                userMap.put("signDate", searchDay);
                userMap.put("userList", uicGroupList);
                datawareUserSignDayService.updateUserGroup(userMap);
            }
        }

        sign(map, uicGroupList, searchDay, searchHour);
    }

    private void sign(Map<String, Object> map, List<Long> uicGroupList, String searchDay, String searchHour) {
        try {
            List<DatawareUserSignDay> hourList = platUserCheckLotteryLogService.findSignList(map);
            List<DatawareUserSignDay> oldList = platSignedUserService.findListFromSignedUser(map);
            if (CollectionUtils.isNotEmpty(oldList)) {
                hourList.addAll(oldList);
            }
            for (DatawareUserSignDay item : hourList) {
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

            if (CollectionUtils.isNotEmpty(hourList)) {
                Map<String, Object> params = new HashMap<>();
                params.put("signDate", searchDay);
                params.put("signHour", searchHour);
                long count = datawareUserSignDayService.getCountByTime(params);
                if (count <= 0) {
                    datawareUserSignDayService.batchSave(hourList);
                }
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
