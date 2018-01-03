package com.wf.data.task.dataware;

import com.google.common.collect.Lists;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;
import com.wf.data.dao.data.entity.DatawareBuryingPointHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareBuryingPointHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class BuryingPointDayJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataDictService dataDictService = SpringContextHolder.getBean(DataDictService.class);
    private final DatawareBuryingPointDayService datawareBuryingPointDayService = SpringContextHolder.getBean(DatawareBuryingPointDayService.class);
    private final DatawareBuryingPointHourService datawareBuryingPointHourService = SpringContextHolder.getBean(DatawareBuryingPointHourService.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);

    public void execute() {
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

        boolean buryingFlag = dataConfigService.getBooleanValueByName(DataConstants.DATA_DATAWARE_BURYING_FLAG_DAY);
        if (false == buryingFlag) {
            historyBuryingPoint(uicGroupList);
        } else {
            String searchDate = DateUtils.getYesterdayDate();
            Map<String, Object> params = new HashMap<>();
            params.put("buryingDate", searchDate);
            long count = datawareBuryingPointDayService.getCountByTime(params);
            if (count <= 0) {
                buryingPoint(searchDate, uicGroupList);
            }
        }


        logger.info("每小时埋点汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void historyBuryingPoint(List<Long> uicGroupList) {
        String date = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_BURYING_HISTORY_DAY);

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
            for (String searchDate : datelist) {
                Map<String, Object> params = new HashMap<>();
                params.put("buryingDate", searchDate);
                long count = datawareBuryingPointDayService.getCountByTime(params);
                if (count <= 0) {
                    buryingPoint(searchDate, uicGroupList);
                }

            }
        } catch (Exception e) {
            logger.error("时间格式错误: traceId={}, date={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(date));
            return;
        }


    }

    private void buryingPoint(String yesterday, List<Long> uicGroupList) {
        try {
            DatawareBuryingPointHour log = new DatawareBuryingPointHour();
            log.setBuryingDate(yesterday);
            log.setUserGroup(2);
            //1、所有用户制成正常用户
            datawareBuryingPointHourService.updateUserGroup(log);
            //2、获取当天所有用户
            List<Long> userList = datawareBuryingPointHourService.findUserId(log);
            //3、求当天所有用户与非正常用户的交集
            Collection interColl = CollectionUtils.intersection(userList, uicGroupList);
            List<Long> users = (List) interColl;
            //4、更新交集用户状态为非正常用户
            log.setUserGroup(1);
            log.setUserList(users);
            datawareBuryingPointHourService.updateUserGroup(log);
        } catch (Exception e) {
            logger.error("更新用户状态失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("buryingDate", yesterday);
            map.put("userGroup", 2);

            List<DatawareBuryingPointDay> pointDayList = datawareBuryingPointHourService.findBuryingList(map);

            for (DatawareBuryingPointDay item : pointDayList) {
                DataDict dataDict = dataDictService.getDictByValue("game_type", item.getGameType());
                if (null != dataDict) {
                    item.setGameName(dataDict.getLabel());
                }
                if (null != item.getChannelId()) {
                    ChannelInfo channelInfo = channelInfoService.get(item.getChannelId());
                    if (null != channelInfo) {
                        item.setParentId(channelInfo.getParentId());
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(pointDayList)) {
                datawareBuryingPointDayService.batchSave(pointDayList);
            }
        } catch (Exception e) {
            logger.error("buryingPointDay添加汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


}
