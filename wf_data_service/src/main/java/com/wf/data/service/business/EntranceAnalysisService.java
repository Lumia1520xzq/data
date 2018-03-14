package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.constants.ChannelConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;
import com.wf.data.service.BehaviorRecordService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * @author shihui
 * @date 2018/2/28
 */

@Service
public class EntranceAnalysisService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataDictService dictService;
    @Autowired
    private BehaviorRecordService behaviorRecordService;
    @Autowired
    private DatawareUserSignDayService signDayService;
    @Autowired
    private DatawareBettingLogDayService bettingLogDayService;
    @Autowired
    private DatawareConvertDayService convertDayService;
    @Autowired
    private DatawareFinalEntranceAnalysisService entranceAnalysisService;
    @Autowired
    private DatawareUserInfoExtendBaseService userInfoExtendBaseService;
    @Autowired
    private DatawareUserInfoExtendStatisticsService userInfoExtendStatisticsService;

    public void toDoEntranceAnalysis() {
        logger.info("每天奖多多各入口用户数据统计开始:traceId={}", TraceIdUtils.getTraceId());

        entranceAnalysis(DateUtils.getYesterdayDate());

        logger.info("每天奖多多各入口用户数据统计结束:traceId={}", TraceIdUtils.getTraceId());
    }

    @Async
    public void historyEntranceAnalysis(String startTime, String endTime) {
        try {

            if (startTime.equals(endTime)) {
                Map<String, Object> params = new HashMap<>();
                params.put("beginDate", endTime);
                List<DatawareFinalEntranceAnalysis> analysisServices = entranceAnalysisService.getEntranceAnalysisByDate(params);
                if (analysisServices.size() > 0) {
                    entranceAnalysisService.deleteByDate(params);
                }
                entranceAnalysis(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("beginDate", endTime);
                    List<DatawareFinalEntranceAnalysis> analysisServices = entranceAnalysisService.getEntranceAnalysisByDate(params);
                    if (analysisServices.size() > 0) {
                        entranceAnalysisService.deleteByDate(params);
                    }
                    entranceAnalysis(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("奖多多入口用户老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void entranceAnalysis(String beginDate) {

        //活跃用户分类
        List<DataDict> userActiveTypeList = dictService.getDictList("user_active_type");
        //充值用户分类
        List<DataDict> userConvertTypeList = dictService.getDictList("user_convert_type");
        //渠道-奖多多
        Long channelId = ChannelConstants.JS_CHANNEL;
        //奖多多所有入口
        List<DataDict> entranceDicts = dictService.getDictList("jdd_entrance_type");
        //所有入口总日活
        Long totalDAU = 0L;

        for (DataDict entranceDict : entranceDicts) {
            for (DataDict userActiveDict : userActiveTypeList) {//循环获取活跃用户分类
                for (DataDict userConvertDict : userConvertTypeList) {//循环获取充值用户分类
                    List<Long> usersParam = getParamUsers(userActiveDict.getValue(), userConvertDict.getValue(), channelId);
                    DatawareFinalEntranceAnalysis finalEntranceAnalysis = new DatawareFinalEntranceAnalysis();
                    Long entranceDau;
                    Long entranceSign;
                    Double entranceSignRate;
                    Long entranceBetting;
                    Double entranceBettingRate;
                    Long entrancePay;
                    Double entrancePayRate;
                    Double entranceDayRetention;

                    /*DAU **/
                    Map<String, Object> dauParams = new HashMap<>();
                    dauParams.put("beginDate", beginDate);
                    dauParams.put("channelId", channelId);
                    dauParams.put("eventId", entranceDict.getValue());
                    dauParams.put("limitCount", 50000);
                    //当前入口dau
                    List<Long> entranceUserIds = behaviorRecordService.getUserIdsByEntrance(dauParams);
                    entranceDau = Long.parseLong(String.valueOf(entranceUserIds.size()));
                    totalDAU += entranceDau;

                    /*签到人数 **/
                    Map<String, Object> signParams = new HashMap<>();
                    signParams.put("beginDate", beginDate);
                    signParams.put("channelId", channelId);
                    List<Long> signedUserIds = signDayService.getSignedUserIds(signParams);
                    //取当前入口的签到人数
                    Collection signedEntranceUserList = CollectionUtils.intersection(entranceUserIds, signedUserIds);
                    entranceSign = Long.parseLong(String.valueOf(signedEntranceUserList.size()));//签到人数
                    entranceSignRate = cal(entranceSign, entranceDau);//签到转化率

                    /*投注人数 **/
                    Map<String, Object> bettingParams = new HashMap<>();
                    bettingParams.put("beginDate", beginDate);
                    bettingParams.put("channelId", channelId);
                    List<Long> bettingUserIdList = bettingLogDayService.getBettingUserIdListByDate(bettingParams);
                    Collection bettingEntranceUserList = CollectionUtils.intersection(entranceUserIds, bettingUserIdList);
                    entranceBetting = Long.parseLong(String.valueOf(bettingEntranceUserList.size()));//投注人数
                    entranceBettingRate = cal(entranceBetting, entranceDau);//投注转化率

                    /*充值人数 **/
                    Map<String, Object> payParams = new HashMap<>();
                    payParams.put("beginDate", beginDate);
                    payParams.put("channelId", channelId);
                    List<Long> payUserIdList = convertDayService.getPayUserIdListByDate(payParams);
                    Collection payntranceUserList = CollectionUtils.intersection(entranceUserIds, payUserIdList);
                    entrancePay = Long.parseLong(String.valueOf(payntranceUserList.size()));//付费人数
                    entrancePayRate = cal(entrancePay, entranceDau);//付费转化率

                    /*次日留存 **/
                    Map<String, Object> yesDauParams = new HashMap<>();
                    yesDauParams.put("beginDate", DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(beginDate), -1)));
                    yesDauParams.put("channelId", channelId);
                    yesDauParams.put("eventId", entranceDict.getValue());
                    yesDauParams.put("limitCount", 50000);
                    //前一天入口dau
                    List<Long> yesEntranceUserIds = behaviorRecordService.getUserIdsByEntrance(yesDauParams);
                    Collection DayRetentionUserIdList = CollectionUtils.intersection(yesEntranceUserIds, entranceUserIds);
                    Long DayRetentionUserCount = Long.parseLong(String.valueOf(DayRetentionUserIdList.size()));
                    entranceDayRetention = cal(DayRetentionUserCount, entranceDau);

                    finalEntranceAnalysis.setBusinessDate(beginDate);
                    finalEntranceAnalysis.setEventId(Long.parseLong(entranceDict.getValue().toString()));
                    finalEntranceAnalysis.setEventName(entranceDict.getLabel());
                    finalEntranceAnalysis.setEntranceDau(entranceDau);
                    finalEntranceAnalysis.setEntranceDauRate(0D);
                    finalEntranceAnalysis.setEntranceSign(entranceSign);
                    finalEntranceAnalysis.setEntranceSignRate(entranceSignRate);
                    finalEntranceAnalysis.setEntranceBetting(entranceBetting);
                    finalEntranceAnalysis.setEntranceBettingRate(entranceBettingRate);
                    finalEntranceAnalysis.setEntrancePay(entrancePay);
                    finalEntranceAnalysis.setEntrancePayRate(entrancePayRate);
                    finalEntranceAnalysis.setEntranceDayRetention(entranceDayRetention);
                    entranceAnalysisService.save(finalEntranceAnalysis);

                    //更新入口dau占比
                    Map<String, Object> eaparams = new HashMap<>();
                    eaparams.put("beginDate", beginDate);
                    List<DatawareFinalEntranceAnalysis> entranceAnalyses = entranceAnalysisService.getEntranceAnalysisByDate(eaparams);
                    if (CollectionUtils.isNotEmpty(entranceAnalyses)) {
                        for (DatawareFinalEntranceAnalysis entranceAnalysis : entranceAnalyses) {
                            //当前入口dau占比
                            Double entranceDauRate = cal(entranceAnalysis.getEntranceDau(), totalDAU);
                            entranceAnalysis.setEntranceDauRate(entranceDauRate);
                            entranceAnalysisService.save(entranceAnalysis);
                        }
                    }
                }
            }
        }
    }

    private List<Long> getParamUsers(Integer userActiveType, Integer userConvertType, Long channelId) {
        //获取前15天日期
        String currentDate = LocalDate.now().minusDays(15).toString();
        //是否新用户
        String newUserFlag = "1";
        if (userActiveType == 1) {
            newUserFlag = "0";
        }

        List<Long> users = new ArrayList<>();
        if (userActiveType == 0 || userConvertType == 0) {
            return users;
        }

        Map<String, Object> activeParams = new HashMap<>();
        activeParams.put("channelId", channelId);
        activeParams.put("lastActiveDate", currentDate);
        activeParams.put("newUserFlag", newUserFlag);

        //活跃用户
        List<Long> activeUsers = userInfoExtendBaseService.getUserIdByDate(activeParams);

        //充值用户

        //交集

        return users;
    }

    private Double cal(Long d1, Long d2) {
        if (d1 == null || d2 == null || d2 == 0L) {
            return 0D;
        }
        return BigDecimalUtil.div(d1 * 100, d2, 2);
    }
}
