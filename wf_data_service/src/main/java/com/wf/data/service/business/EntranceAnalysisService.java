package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.constants.ChannelConstants;
import com.wf.data.common.constants.UserClassifyContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;
import com.wf.data.service.BehaviorRecordService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
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
    private DatawareBuryingPointDayService buryingPointDayService;
    @Autowired
    private DatawareUserInfoService userInfoService;

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
                    params.put("beginDate", searchDate);
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

        //参数对象
        Map<String, Object> dauParams = new HashMap<>();

        //局部变量
        List<Long> activeUsers;
        List<Long> convertUsers;

        //渠道当天所有活跃用户
        Map<String, Object> paramMap1 = new HashMap<>();
        paramMap1.put("businessDate", beginDate);
        paramMap1.put("parentId", channelId);
        List<Long> allChannelUsers = buryingPointDayService.getUserIdListByChannel(paramMap1);

        //前15天日期
        String fifteenDateBegin = DateUtils.getPrevDate(beginDate, 15);
        String fifteenDateEnd = DateUtils.getPrevDate(beginDate, 1);
        //渠道新用户
        List<Long> channelNewUsers = getChannelNewUsers(beginDate, channelId);
        //渠道15天内活跃的用户
        List<Long> channelAvtiveUsers = getChannelAvtiveUsers(fifteenDateBegin, channelId, fifteenDateEnd);

        //循环入口
        for (DataDict entranceDict : entranceDicts) {
            //入口信息
            Long eventId = Long.parseLong(entranceDict.getValue().toString());
            String eventName = entranceDict.getLabel();

            //当前入口DAU
            dauParams.put("beginDate", beginDate);
            dauParams.put("channelId", channelId);
            dauParams.put("eventId", entranceDict.getValue());
            dauParams.put("limitCount", 50000);
            List<Long> entranceAllUserIds = behaviorRecordService.getUserIdsByEntrance(dauParams);
            if (entranceAllUserIds == null) {
                entranceAllUserIds = new ArrayList<>();
            }

            //循环获取充值用户分类
            for (DataDict userConvertDict : userConvertTypeList) {
                DatawareFinalEntranceAnalysis finalEntranceAnalysis = new DatawareFinalEntranceAnalysis();
                if (userConvertDict.getValue() == 0) {//所有用户
                    convertUsers = entranceAllUserIds;
                } else {
                    convertUsers = getConvertUserIds(userConvertDict.getValue(), channelId, beginDate, entranceAllUserIds);
                }
                List<Long> entranceUserIdByType = ListUtils.intersection(entranceAllUserIds, convertUsers);
                finalEntranceAnalysis.setEventId(eventId);
                finalEntranceAnalysis.setEventName(eventName);
                finalEntranceAnalysis.setActiveUserType(0);
                finalEntranceAnalysis.setConvertUserType(userConvertDict.getValue());
                finalEntranceAnalysis.setBusinessDate(beginDate);
                saveInfo(channelId, entranceUserIdByType, finalEntranceAnalysis, allChannelUsers);
            }

            //循环获取活跃用户分类
            for (DataDict userActiveDict : userActiveTypeList) {
                DatawareFinalEntranceAnalysis finalEntranceAnalysis = new DatawareFinalEntranceAnalysis();
                if (userActiveDict.getValue() == 0) {//所有用户
                    continue;
                } else {
                    activeUsers = getActiveUserIds(userActiveDict.getValue(), channelNewUsers, channelAvtiveUsers, allChannelUsers);
                }
                List<Long> entranceUserIdByType = ListUtils.intersection(entranceAllUserIds, activeUsers);
                finalEntranceAnalysis.setEventId(eventId);
                finalEntranceAnalysis.setEventName(eventName);
                finalEntranceAnalysis.setActiveUserType(userActiveDict.getValue());
                finalEntranceAnalysis.setConvertUserType(0);
                finalEntranceAnalysis.setBusinessDate(beginDate);
                saveInfo(channelId, entranceUserIdByType, finalEntranceAnalysis, allChannelUsers);
            }
        }

        //更新入口dau占比
        Map<String, Object> eaparams = new HashMap<>();
        eaparams.put("beginDate", beginDate);
        List<DatawareFinalEntranceAnalysis> entranceAnalyses = entranceAnalysisService.getEntranceAnalysisByDate(eaparams);
        Double entranceDauRate = 0D;

        //所有用户DAU
        eaparams.put("activeUserType", "0");
        eaparams.put("convertUserType", "0");
        Long totalDAU = entranceAnalysisService.getAllEntranceDau(eaparams);

        if (CollectionUtils.isNotEmpty(entranceAnalyses)) {
            for (DatawareFinalEntranceAnalysis entranceAnalysis : entranceAnalyses) {
                if (null != entranceAnalysis) {
                    if (entranceAnalysis.getActiveUserType() == 0 && entranceAnalysis.getConvertUserType() == 0) {//各个入口DAU占比
                        //当前入口dau占比
                        entranceDauRate = cal(entranceAnalysis.getEntranceDau(), totalDAU);
                        entranceAnalysis.setEntranceDauRate(entranceDauRate);
                    } else {
                        //获取当前入口dau
                        eaparams.put("activeUserType", entranceAnalysis.getActiveUserType().toString());
                        eaparams.put("convertUserType", entranceAnalysis.getConvertUserType().toString());
                        Long entranceDau = entranceAnalysisService.getCurrentEntranceDau(eaparams);
                        entranceDauRate = cal(entranceAnalysis.getEntranceDau(), entranceDau);
                        entranceAnalysis.setEntranceDauRate(entranceDauRate);
                    }
                    entranceAnalysisService.save(entranceAnalysis);
                }
            }
        }
    }

    //15日内活跃用户
    private List<Long> getChannelAvtiveUsers(String fifteenDateBegin, Long channelId, String fifteenDateEnd) {
        Map<String, Object> activeUserParam = new HashMap<>();
        activeUserParam.put("channelId", channelId);
        activeUserParam.put("beginDate", fifteenDateBegin);
        activeUserParam.put("endDate", fifteenDateEnd);
        List<Long> activeUsers = buryingPointDayService.getActiveUsersInFifteenDay(activeUserParam);
        if (null == activeUsers) {
            activeUsers = new ArrayList<>();
        }
        return activeUsers;
    }

    //获取渠道新用户id
    private List<Long> getChannelNewUsers(String beginDate, Long channelId) {
        Map<String, Object> newUserParam = new HashMap<>();
        newUserParam.put("channelId", channelId);
        newUserParam.put("businessDate", beginDate);
        List<Long> newUserList = userInfoService.getNewUserByDate(newUserParam);
        if (null == newUserList) {
            newUserList = new ArrayList<>();
        }
        return newUserList;
    }

    //保存数据
    private void saveInfo(Long channelId, List<Long> entranceUserIds, DatawareFinalEntranceAnalysis finalEntranceAnalysis, List<Long> allChannelUsers) {

        Long entranceDau = Long.parseLong(String.valueOf(entranceUserIds.size()));
        Long eventId = finalEntranceAnalysis.getEventId();
        String beginDate = finalEntranceAnalysis.getBusinessDate();

        /*签到人数 **/
        Map<String, Object> signParams = new HashMap<>();
        signParams.put("beginDate", beginDate);
        signParams.put("channelId", channelId);
        List<Long> signedUserIds = signDayService.getSignedUserIds(signParams);
        //取当前入口的签到人数
        Collection signedEntranceUserList = CollectionUtils.intersection(entranceUserIds, signedUserIds);
        Long entranceSign = Long.parseLong(String.valueOf(signedEntranceUserList.size()));//签到人数
        Double entranceSignRate = cal(entranceSign, entranceDau);//签到转化率

        /*投注人数 **/
        Map<String, Object> bettingParams = new HashMap<>();
        bettingParams.put("beginDate", beginDate);
        bettingParams.put("channelId", channelId);
        List<Long> bettingUserIdList = bettingLogDayService.getBettingUserIdListByDate(bettingParams);
        Collection bettingEntranceUserList = CollectionUtils.intersection(entranceUserIds, bettingUserIdList);
        Long entranceBetting = Long.parseLong(String.valueOf(bettingEntranceUserList.size()));//投注人数
        Double entranceBettingRate = cal(entranceBetting, entranceDau);//投注转化率

        /*充值人数 **/
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("beginDate", beginDate);
        payParams.put("channelId", channelId);
        List<Long> payUserIdList = convertDayService.getPayUserIdListByDate(payParams);
        Collection payntranceUserList = CollectionUtils.intersection(entranceUserIds, payUserIdList);
        Long entrancePay = Long.parseLong(String.valueOf(payntranceUserList.size()));//付费人数
        Double entrancePayRate = cal(entrancePay, entranceDau);//付费转化率

        /*次日留存 **/
        Map<String, Object> yesDauParams = new HashMap<>();
        yesDauParams.put("beginDate", DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(beginDate), -1)));
        yesDauParams.put("channelId", channelId);
        yesDauParams.put("eventId", eventId);
        yesDauParams.put("limitCount", 50000);
        //前一天入口dau
        List<Long> yesEntranceUserIds = behaviorRecordService.getUserIdsByEntrance(yesDauParams);
        Collection DayRetentionUserIdList = CollectionUtils.intersection(yesEntranceUserIds, allChannelUsers);
        Long DayRetentionUserCount = Long.parseLong(String.valueOf(DayRetentionUserIdList.size()));
        Double entranceDayRetention = cal(DayRetentionUserCount, entranceDau);

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
    }

    //根据用户分层获取用户ID
    private List<Long> getConvertUserIds(Integer userConvertType, Long channelId, String searchDate, List<Long> entranceAllUserIds) {

        //获取充值用户
        List<Long> convertUsers;
        Integer minAmount = null;
        Integer maxAmount = null;
        //获取活跃用户中未充值用户
        if (userConvertType == 1) {
            //1.已充值的用户
            Map<String, Object> convertedParams = new HashMap<>();
            convertedParams.put("parentId", channelId);
            convertedParams.put("searchDate", searchDate);
            convertedParams.put("minAmount", 0);
            List<Long> convertedUsers = convertDayService.getUsersByRechargeType(convertedParams);
            //2.入口所有用户和已充值用户求差集
            convertUsers = (List<Long>) CollectionUtils.disjunction(entranceAllUserIds, CollectionUtils.intersection(entranceAllUserIds, convertedUsers));
        } else {
            switch (userConvertType) {
                case 2:
                    minAmount = 1;
                    maxAmount = 100;
                    break;
                case 3:
                    minAmount = 100;
                    maxAmount = 1000;
                    break;
                case 4:
                    minAmount = 1000;
                    maxAmount = 10000;
                    break;
                case 5:
                    minAmount = 10000;
                    maxAmount = 100000;
                    break;
                case 6:
                    minAmount = 100000;
                    break;
            }
            Map<String, Object> convertParams = new HashMap<>();
            convertParams.put("parentId", channelId);
            convertParams.put("searchDate", searchDate);
            convertParams.put("rechargeType", userConvertType);
            convertParams.put("minAmount", minAmount);
            convertParams.put("maxAmount", maxAmount);
            convertUsers = convertDayService.getUsersByRechargeType(convertParams);
            if (CollectionUtils.isEmpty(convertUsers)) {
                convertUsers = new ArrayList<>();
            }
        }
        return convertUsers;
    }

    //获取活跃分类用户ID
    private List<Long> getActiveUserIds(Integer userActiveType, List<Long> channelNewUsers, List<Long> channelAvtiveUsers, List<Long> allChannelUsers) {
        if (userActiveType == UserClassifyContents.USERGROUP_NEWUSER) {//新用户
            return channelNewUsers;
        } else if (userActiveType == UserClassifyContents.USERGROUP_FIFTEEN_ACTIVE) {//15天内活跃用户
            return channelAvtiveUsers;
        } else if (userActiveType == UserClassifyContents.USERGROUP_FIFTEEN_NO_ACTIVE) {
            List<Long> noActiveUsers = (List<Long>) CollectionUtils.disjunction(allChannelUsers, CollectionUtils.intersection(allChannelUsers, channelAvtiveUsers));
            if (null == noActiveUsers) {
                noActiveUsers = new ArrayList<>();
            }
            return noActiveUsers;
        }
        return null;
    }

    private Double cal(Long d1, Long d2) {
        if (d1 == null || d2 == null || d2 == 0L) {
            return 0D;
        }
        return BigDecimalUtil.div(d1 * 100, d2, 2);
    }
}
