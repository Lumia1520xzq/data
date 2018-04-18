package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareFinalGameInfoService;
import com.wf.data.service.data.DatawareUserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/4/4
 */
@Service
public class GameOverViewService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DatawareFinalGameInfoService finalGameInfoService;

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private DatawareBuryingPointDayService buryingPointDayService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private DatawareBettingLogDayService bettingLogDayService;

    @Autowired
    private DatawareUserInfoService userInfoService;

    public void toDoGameOverViewAnalysis() {
        logger.info("每天游戏总览数据汇总开始:traceId={}", TraceIdUtils.getTraceId());

        //获取昨天日期
        String searchDate = DateUtils.getYesterdayDate();

        //清洗数据
        cleanFinalGameInfo(searchDate);

        logger.info("每天游戏总览数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void cleanFinalGameInfo(String searchDate) {

        //判断表中是否存在当前日期数据
        Map<String, Object> param = new HashMap<>();
        int dataCount = finalGameInfoService.getCountByDate(param);
        if (dataCount > 0) {//清除原有数据
            finalGameInfoService.deleteByDate(param);
        }
        //清洗数据
        dataCollect(searchDate, 0);//自动更新
    }

    @Async
    public void historyGameOverViewAnalysis(String startTime, String endTime) {
        logger.info("游戏数据总览老数据清洗开始:traceId={}", TraceIdUtils.getTraceId());
        try {
            if (startTime.equals(endTime)) {
                doAnalysis(startTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    doAnalysis(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("游戏数据总览老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void doAnalysis(String searchDate) {
        //判断是否有老数据
        Map<String, Object> param = new HashMap<>();
        param.put("beginDate", searchDate);
        if (finalGameInfoService.getCountByDate(param) > 0) {
            finalGameInfoService.deleteByDate(param);
        }
        dataCollect(searchDate, 1);//手动更新
    }

    private void dataCollect(String searchDate, int updateType) {

        List<DatawareFinalGameInfo> gameInfoList = new ArrayList<>();

        //获取所有主渠道
        List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();

        //获取所有游戏类型
        List<DataDict> gameDataDictList = dataDictService.findListByType("game_type");

        //各个游戏所有渠道数据
        if (CollectionUtils.isNotEmpty(gameDataDictList)) {
            for (DataDict dict : gameDataDictList) {
                gameInfoList.add(getInfoByParam(searchDate, null, dict.getValue(), updateType));
            }
        }

        //循环渠道和游戏类型
        if (CollectionUtils.isNotEmpty(channelInfoList) && CollectionUtils.isNotEmpty(gameDataDictList)) {
            for (ChannelInfo channelInfo : channelInfoList) {
                for (DataDict dict : gameDataDictList) {
                    gameInfoList.add(getInfoByParam(searchDate, channelInfo.getId(), dict.getValue(), updateType));
                }
            }
        }
        finalGameInfoService.batchSave(gameInfoList);
    }

    private DatawareFinalGameInfo getInfoByParam(String searchDate, Long initParentId, Integer gameType, int updateType) {
        DatawareFinalGameInfo gameInfo = new DatawareFinalGameInfo();
        Long parentId = initParentId;
        if (initParentId == null) {
            parentId = 1L;
        }
        /*全量用户投注信息*/
        Map<String, Object> bettingParam = new HashMap<>();
        bettingParam.put("searchDate", searchDate);
        bettingParam.put("bettingDate", searchDate);
        bettingParam.put("parentId", initParentId);
        bettingParam.put("gameType", gameType);
        //获取全量用户dau
        Integer dauI = buryingPointDayService.getGameDau(bettingParam);
        Long dau = dauI == null ? 0L : Long.parseLong(dauI.toString());
        //全量用户投注信息
        DatawareBettingLogDay bettingLogDayInfo = bettingLogDayService.getBettingInfoByDate(bettingParam);

        //(搜索日期)活跃用户
        List<Long> activeUserIds = buryingPointDayService.getGameDauIds(bettingParam);
        //(T-1日)活跃用户
        bettingParam.put("searchDate", DateUtils.getPrevDate(searchDate, 1));
        List<Long> oneDayBeforeActiveUserIds = buryingPointDayService.getGameDauIds(bettingParam);
        //(T-3日)活跃用户
        bettingParam.put("searchDate", DateUtils.getPrevDate(searchDate, 3));
        List<Long> threeDayBeforeActiveUserIds = buryingPointDayService.getGameDauIds(bettingParam);
        //(T-7日)活跃用户
        bettingParam.put("searchDate", DateUtils.getPrevDate(searchDate, 7));
        List<Long> sevenDayBeforeActiveUserIds = buryingPointDayService.getGameDauIds(bettingParam);

        //全量用户次日留存
        List<Long> onDayActive = (List<Long>) CollectionUtils.intersection(activeUserIds, oneDayBeforeActiveUserIds);
        Double oneDayRelation = division(onDayActive.size(), oneDayBeforeActiveUserIds.size());
        //全量用户三日留存
        List<Long> threeDayActive = (List<Long>) CollectionUtils.intersection(activeUserIds, threeDayBeforeActiveUserIds);
        Double threeDayRelation = division(threeDayActive.size(), threeDayBeforeActiveUserIds.size());
        //全量用户七日留存
        List<Long> sevenDayActive = (List<Long>) CollectionUtils.intersection(activeUserIds, sevenDayBeforeActiveUserIds);
        Double sevenDayRelation = division(sevenDayActive.size(), threeDayBeforeActiveUserIds.size());

        /*新增用户投注信息*/
        //新增用户ID集合
        Map<String, Object> newUserParam = new HashMap<>();
        newUserParam.put("parentId", initParentId);
        //(搜索日期新用户)
        newUserParam.put("businessDate", searchDate);
        List<Long> newUserIds = userInfoService.getNewUserByDate(newUserParam);
        //(T-1日新用户)
        newUserParam.put("businessDate", DateUtils.getPrevDate(searchDate, 1));
        List<Long> oneDayBeforeNewUserIds = userInfoService.getNewUserByDate(newUserParam);
        //(T-3日新用户)
        newUserParam.put("businessDate", DateUtils.getPrevDate(searchDate, 3));
        List<Long> threeDayBeforeNewUserIds = userInfoService.getNewUserByDate(newUserParam);
        //(T-7日新用户)
        newUserParam.put("businessDate", DateUtils.getPrevDate(searchDate, 7));
        List<Long> sevenDayBeforeNewUserIds = userInfoService.getNewUserByDate(newUserParam);

        Map<String, Object> newUserBettingParam = new HashMap<>();
        newUserBettingParam.put("searchDate", searchDate);
        newUserBettingParam.put("bettingDate", searchDate);
        newUserBettingParam.put("parentId", initParentId);
        newUserBettingParam.put("gameType", gameType);
        newUserBettingParam.put("newUserIds", newUserIds);
        //新增用户投注信息
        DatawareBettingLogDay newUserBettingLogDayInfo = bettingLogDayService.getBettingInfoByDate(newUserBettingParam);

        //(搜索日)新增用户且活跃用户
        List<Long> newAndActiveList = (List<Long>) CollectionUtils.intersection(newUserIds, activeUserIds);
        //(T-1日)新增用户且活跃用户
        List<Long> newAndActiveOneList = (List<Long>) CollectionUtils.intersection(oneDayBeforeNewUserIds, oneDayBeforeActiveUserIds);
        //(T-3日)新增用户且活跃用户
        List<Long> newAndActivethreeList = (List<Long>) CollectionUtils.intersection(threeDayBeforeNewUserIds, threeDayBeforeActiveUserIds);
        //(T-7日)新增用户且活跃用户
        List<Long> sevenAndActiveSevenList = (List<Long>) CollectionUtils.intersection(sevenDayBeforeNewUserIds, sevenDayBeforeActiveUserIds);

        //新增用户次日留存
        List<Long> newUserOnDayActive = (List<Long>) CollectionUtils.intersection(newAndActiveOneList, activeUserIds);
        Double newUserOneDayRelation = division(newUserOnDayActive.size(), newAndActiveOneList.size());
        //新增用户三日留存
        List<Long> newUserThreeDayActive = (List<Long>) CollectionUtils.intersection(newAndActivethreeList, activeUserIds);
        Double newUserThreeDayRelation = division(newUserThreeDayActive.size(), newAndActivethreeList.size());
        //新增用户七日留存
        List<Long> newUserSevenDayActive = (List<Long>) CollectionUtils.intersection(sevenAndActiveSevenList, activeUserIds);
        Double newUserSevenDayRelation = division(newUserSevenDayActive.size(), sevenAndActiveSevenList.size());

        //导入率（游戏DAU/平台DAU）
        Map<String, Object> importRateParam = new HashMap<>();
        importRateParam.put("parentId", initParentId);
        importRateParam.put("searchDate", searchDate);
        Integer platDAU = buryingPointDayService.getGameDau(importRateParam);
        Double importRate = division(dau, Long.parseLong(platDAU.toString()));

        //累计用户数(渠道+游戏)
        Map<String, Object> totalUserParam = new HashMap<>();
        totalUserParam.put("parentId", initParentId);
        totalUserParam.put("gameType", gameType);
        Integer totalUserCount = buryingPointDayService.getGameDau(totalUserParam);

        /*更新留存*/
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("parentId", parentId);
            param.put("gameType", gameType);
            param.put("searchDate", DateUtils.getPrevDate(searchDate, 1));
            //更新(T-1日)次日留存
            DatawareFinalGameInfo oneDayBeforeFinalGameInfo = finalGameInfoService.getInfoByDateAndGameType(param);
            if (oneDayBeforeFinalGameInfo != null) {
                oneDayBeforeFinalGameInfo.setOneDayRetention(BigDecimalUtil.mul(oneDayRelation, 100));
                oneDayBeforeFinalGameInfo.setNewUserOneDayRetention(BigDecimalUtil.mul(newUserOneDayRelation, 100));
                finalGameInfoService.save(oneDayBeforeFinalGameInfo);
            }
            //更新(T-3日)三日留存
            param.put("searchDate", DateUtils.getPrevDate(searchDate, 3));
            DatawareFinalGameInfo threeDayBeforeFinalGameInfo = finalGameInfoService.getInfoByDateAndGameType(param);
            if (threeDayBeforeFinalGameInfo != null) {
                threeDayBeforeFinalGameInfo.setThreeDayRetention(BigDecimalUtil.mul(threeDayRelation, 100));
                threeDayBeforeFinalGameInfo.setNewUserThreeDayRetention(BigDecimalUtil.mul(newUserThreeDayRelation, 100));
                finalGameInfoService.save(threeDayBeforeFinalGameInfo);
            }
            //更新(T-7日)七日留存
            param.put("searchDate", DateUtils.getPrevDate(searchDate, 7));
            DatawareFinalGameInfo sevenDayBeforeFinalGameInfo = finalGameInfoService.getInfoByDateAndGameType(param);
            if (sevenDayBeforeFinalGameInfo != null) {
                sevenDayBeforeFinalGameInfo.setSevenDayRetention(BigDecimalUtil.mul(sevenDayRelation, 100));
                sevenDayBeforeFinalGameInfo.setNewUserSevenDayRetention(BigDecimalUtil.mul(newUserSevenDayRelation, 100));
                finalGameInfoService.save(sevenDayBeforeFinalGameInfo);
            }
        } catch (Exception e) {
            logger.error("更新dataware_final_game_info的次，三，七日留存失败.Date:" + DateUtils.getPrevDate(searchDate, 1) + ";parentId:" + parentId + ";gameType:" + gameType);
        }

        gameInfo.setParentId(parentId);
        gameInfo.setChannelId(parentId);
        gameInfo.setGameType(gameType);
        gameInfo.setBusinessDate(searchDate);
        gameInfo.setDau(dau);
        gameInfo.setBettingUserCount(bettingLogDayInfo.getUserCount());
        gameInfo.setBettingAmount(bettingLogDayInfo.getBettingAmount());
        gameInfo.setBettingCount(Long.parseLong(bettingLogDayInfo.getBettingCount().toString()));
        gameInfo.setReturnAmount(bettingLogDayInfo.getResultAmount());
        gameInfo.setDiffAmount(BigDecimalUtil.sub(gameInfo.getBettingAmount(), gameInfo.getReturnAmount()));
        gameInfo.setReturnRate(BigDecimalUtil.mul(division(gameInfo.getReturnAmount(), gameInfo.getBettingAmount()), 100));
        gameInfo.setBettingArpu(division(gameInfo.getBettingAmount(), gameInfo.getBettingUserCount()));
        gameInfo.setBettingAsp(division(gameInfo.getBettingAmount(), gameInfo.getBettingCount()));
        gameInfo.setBettingConversion(BigDecimalUtil.mul(division(gameInfo.getBettingUserCount(), dau), 100));
        gameInfo.setOneDayRetention(0.00);
        gameInfo.setThreeDayRetention(0.00);
        gameInfo.setSevenDayRetention(0.00);
        gameInfo.setNewUserCount((long) newAndActiveList.size());
        gameInfo.setNewUserBettingUserCount(newUserBettingLogDayInfo.getUserCount());
        gameInfo.setNewUserBettingAmount(newUserBettingLogDayInfo.getBettingAmount());
        gameInfo.setNewUserBettingCount(Long.parseLong(newUserBettingLogDayInfo.getBettingCount().toString()));
        gameInfo.setNewUserReturnAmount(newUserBettingLogDayInfo.getResultAmount());
        gameInfo.setNewUserDiffAmount(BigDecimalUtil.sub(gameInfo.getNewUserBettingAmount(), gameInfo.getNewUserReturnAmount()));
        gameInfo.setNewUserReturnRate(BigDecimalUtil.mul(division(gameInfo.getNewUserReturnAmount(), gameInfo.getNewUserBettingAmount()), 100));
        gameInfo.setNewUserBettingArpu(division(gameInfo.getNewUserBettingAmount(), gameInfo.getNewUserBettingUserCount()));
        gameInfo.setNewUserBettingAsp(division(gameInfo.getNewUserBettingAmount(), gameInfo.getNewUserBettingCount()));
        gameInfo.setNewUserBettingConversion(BigDecimalUtil.mul(division(gameInfo.getNewUserBettingUserCount(), Long.parseLong(String.valueOf(newUserIds.size()))), 100));
        gameInfo.setNewUserOneDayRetention(0.00);
        gameInfo.setNewUserThreeDayRetention(0.00);
        gameInfo.setNewUserSevenDayRetention(0.00);
        gameInfo.setImportRate(BigDecimalUtil.mul(importRate, 100));
        gameInfo.setTotalUserCount(Long.parseLong(totalUserCount.toString()));

        //手动更新时，判断是否要更改次，三，七日留存
        if (updateType == 1) {
            String yesterdayStr = DateUtils.getYesterdayDate();
            Map<String, Object> retentionParam = new HashMap<>();
            retentionParam.put("parentId", initParentId);
            retentionParam.put("gameType", gameType);
            Map<String, Object> newUserretentionParam = new HashMap<>();
            newUserretentionParam.put("parentId", initParentId);
            int differDays = DateUtils.getDateInterval(searchDate, yesterdayStr);
            if (differDays >= 1) {//更新当前日期的次日留存
                //(T+1日)活跃用户
                retentionParam.put("searchDate", DateUtils.getPrevDate(searchDate, -1));
                List<Long> oneDayAfterActiveUserIds = buryingPointDayService.getGameDauIds(retentionParam);
                //全量用户次日留存
                List<Long> onDayAfterActive = (List<Long>) CollectionUtils.intersection(activeUserIds, oneDayAfterActiveUserIds);
                Double oneDayafterRelation = division(onDayAfterActive.size(), activeUserIds.size());
                //新增用户次日留存(当天新增且活跃用户且第二天活跃/当天新增且活跃用户)
                List<Long> newUserOnDayAfterActive = (List<Long>) CollectionUtils.intersection(newAndActiveList, oneDayAfterActiveUserIds);
                Double newUserOneDayAfterRelation = division(newUserOnDayAfterActive.size(), newAndActiveList.size());
                gameInfo.setOneDayRetention(BigDecimalUtil.mul(oneDayafterRelation, 100));
                gameInfo.setNewUserOneDayRetention(BigDecimalUtil.mul(newUserOneDayAfterRelation, 100));
            }
            if (differDays >= 3) {//更新当前日期的三日留存
                //(T+3日)活跃用户
                retentionParam.put("searchDate", DateUtils.getPrevDate(searchDate, -3));
                List<Long> threeDayAfterActiveUserIds = buryingPointDayService.getGameDauIds(retentionParam);
                //全量用户次日留存
                List<Long> threeDayAfterActive = (List<Long>) CollectionUtils.intersection(activeUserIds, threeDayAfterActiveUserIds);
                Double threeDayafterRelation = division(threeDayAfterActive.size(), activeUserIds.size());
                //新增用户T+3日留存
                List<Long> newUserThreeDayAfterActive = (List<Long>) CollectionUtils.intersection(newAndActiveList, threeDayAfterActiveUserIds);
                Double newUserThreeDayAfterRelation = division(newUserThreeDayAfterActive.size(), newAndActiveList.size());
                gameInfo.setThreeDayRetention(BigDecimalUtil.mul(threeDayafterRelation, 100));
                gameInfo.setNewUserThreeDayRetention(BigDecimalUtil.mul(newUserThreeDayAfterRelation, 100));
            }
            if (differDays >= 7) {//更新当前日期的七日留存
                //(T+7日)活跃用户
                retentionParam.put("searchDate", DateUtils.getPrevDate(searchDate, -7));
                List<Long> sevenDayAfterActiveUserIds = buryingPointDayService.getGameDauIds(retentionParam);
                //全量用户次日留存
                List<Long> sevenDayAfterActive = (List<Long>) CollectionUtils.intersection(activeUserIds, sevenDayAfterActiveUserIds);
                Double sevenDayafterRelation = division(sevenDayAfterActive.size(), activeUserIds.size());
                //新增用户T+7日留存
                List<Long> newUserSevenDayAfterActive = (List<Long>) CollectionUtils.intersection(newAndActiveList, sevenDayAfterActiveUserIds);
                Double newUserSevenDayAfterRelation = division(newUserSevenDayAfterActive.size(), newAndActiveList.size());
                gameInfo.setSevenDayRetention(BigDecimalUtil.mul(sevenDayafterRelation, 100));
                gameInfo.setNewUserSevenDayRetention(BigDecimalUtil.mul(newUserSevenDayAfterRelation, 100));
            }

        }
        return gameInfo;
    }

    private double division(Double d1, Double d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 2);
        }
        return resulet;
    }

    private double division(Double d1, Long d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 2);
        }
        return resulet;
    }

    private double division(Long d1, Long d2) {
        double resulet = 0;
        if (d1 != null && d1 != 0 && d2 != null && d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 2);
        }
        return resulet;
    }

    private double division(int d1, int d2) {
        double resulet = 0;
        if (d2 != 0) {
            resulet = BigDecimalUtil.div(d1, d2, 2);
        }
        return resulet;
    }
}
