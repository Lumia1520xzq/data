package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareBettingLogHourService;
import com.wf.data.service.data.DatawareBuryingPointHourService;
import com.wf.data.service.data.DatawareGameBettingInfoHourService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/2/7
 */

@Service
public class GameBettingInfoHourService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DatawareGameBettingInfoHourService gameBettingInfoHourService;

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private DatawareBuryingPointHourService buryingPointHourService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private DatawareBettingLogHourService bettingLogHourService;


    public void toDoAnalysis() {
        logger.info("每天数据汇总开始:traceId={}", TraceIdUtils.getTraceId());

        //当前日期向前推1小时，保证统计昨天数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        String searchDay = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
        String searchHour = DateUtils.formatDate(calendar.getTime(), "HH");

        channelGameHourInfo(searchDay, searchHour);
        logger.info("每小时添加游戏数据汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }


    @Async
    public void dataClean(String startTime, String endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(startTime, DateUtils.DATE_TIME_PATTERN));
        Long startTimeStamp = DateUtils.parseDateTime(startTime).getTime();
        Long endTimeStamp = DateUtils.parseDateTime(endTime).getTime();
        while (startTimeStamp < endTimeStamp) {//按小时清洗数据
            String businessDate = DateUtils.formatDate(calendar.getTime(), DateUtils.DATE_PATTERN);
            String businessHour = DateUtils.formatDate(calendar.getTime(), "HH");
            channelGameHourInfo(businessDate, businessHour);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            startTimeStamp = calendar.getTime().getTime();
        }
        logger.info("游戏小时老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void channelGameHourInfo(String businessDate, String businessHour) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("businessDate", businessDate);
            params.put("businessHour", businessHour);
            //删除该小时所有数据
            gameBettingInfoHourService.deleteByDate(params);
            //清洗数据
            dataKettle(businessDate, businessHour);
        } catch (Exception e) {
            logger.error("添加游戏小时数据汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void dataKettle(String businessDate, String businessHour) {
        //获取所有主渠道
        List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();

        //获取所有游戏类型
        List<DataDict> gameDataDictList = dataDictService.findListByType("game_type");

        //各个渠道的所有游戏数据
        if (CollectionUtils.isNotEmpty(channelInfoList)) {
            for (ChannelInfo channelInfo : channelInfoList) {
                Map<String, Object> allGameParams = new HashMap<>();
                allGameParams.put("parentId", channelInfo.getId());//主渠道

                DatawareGameBettingInfoHour gameBettingInfoHour = insertGameData(allGameParams, businessDate, businessHour);

                gameBettingInfoHour.setParentId(channelInfo.getId());
                gameBettingInfoHour.setChannelId(channelInfo.getId());
                gameBettingInfoHour.setGameType(1000);
                gameBettingInfoHour.setBusinessDate(businessDate);
                gameBettingInfoHour.setBusinessHour(businessHour);
                gameBettingInfoHourService.save(gameBettingInfoHour);
            }
        }

        //各个游戏所有渠道数据
        if (CollectionUtils.isNotEmpty(gameDataDictList)) {
            for (DataDict dict : gameDataDictList) {
                Map<String, Object> allChannelParams = new HashMap<>();
                allChannelParams.put("gameType", dict.getValue());//游戏渠道

                DatawareGameBettingInfoHour gameBettingInfoHour = insertGameData(allChannelParams, businessDate, businessHour);

                gameBettingInfoHour.setParentId(1L);
                gameBettingInfoHour.setChannelId(1L);
                gameBettingInfoHour.setGameType(dict.getValue());
                gameBettingInfoHour.setBusinessDate(businessDate);
                gameBettingInfoHour.setBusinessHour(businessHour);
                gameBettingInfoHourService.save(gameBettingInfoHour);
            }
        }

        //循环渠道和游戏类型
        if (CollectionUtils.isNotEmpty(channelInfoList) && CollectionUtils.isNotEmpty(gameDataDictList)) {
            for (ChannelInfo channelInfo : channelInfoList) {
                for (DataDict dict : gameDataDictList) {
                    Map<String, Object> gameAndChannelParams = new HashMap<>();
                    gameAndChannelParams.put("parentId", channelInfo.getId());//主渠道
                    gameAndChannelParams.put("gameType", dict.getValue());//游戏

                    DatawareGameBettingInfoHour gameBettingInfoHour = insertGameData(gameAndChannelParams, businessDate, businessHour);

                    gameBettingInfoHour.setParentId(channelInfo.getId());
                    gameBettingInfoHour.setChannelId(channelInfo.getId());
                    gameBettingInfoHour.setGameType(dict.getValue());
                    gameBettingInfoHour.setBusinessDate(businessDate);
                    gameBettingInfoHour.setBusinessHour(businessHour);
                    gameBettingInfoHourService.save(gameBettingInfoHour);
                }
            }
        }
    }

    private DatawareGameBettingInfoHour insertGameData(Map<String, Object> params, String businessDate, String businessHour) {
        DatawareGameBettingInfoHour gameBettingInfoHour = new DatawareGameBettingInfoHour();

        //****************************************日活信息*****************************************//
        params.put("buryingDate", businessDate);
        params.put("buryingHour", businessHour);
        //每小时日活
        Integer hourDau = buryingPointHourService.getDauByDateAndHour(params);
        if (null == hourDau) hourDau = 0;
        gameBettingInfoHour.setHourDau(Long.valueOf(hourDau));

        //当日总日活
        Long dau = buryingPointHourService.getDauByTime(params);
        if (null == dau) dau = 0L;
        gameBettingInfoHour.setDau(dau);

        //****************************************投注信息*****************************************//
        params.put("bettingDate", businessDate);
        params.put("bettingHour", businessHour);
        //每小时投注信息
        DatawareBettingLogHour bettingLogHour = bettingLogHourService.getSumByDateAndHour(params);
        if (bettingLogHour != null) {
            //每小时投注金额
            gameBettingInfoHour.setHourBettingAmount(bettingLogHour.getBettingAmount() == null ? 0D : bettingLogHour.getBettingAmount());
            //每小时投注笔数
            gameBettingInfoHour.setHourBettingCount(bettingLogHour.getBettingCount() == null ? 0L : bettingLogHour.getBettingCount().longValue());
            //每小时返奖金额
            gameBettingInfoHour.setHourReturnAmount(bettingLogHour.getResultAmount() == null ? 0D : bettingLogHour.getResultAmount());
            //每小时投注人数
            gameBettingInfoHour.setHourBettingUserCount(bettingLogHour.getBettingUserCount() == null ? 0L : bettingLogHour.getBettingUserCount().longValue());
        }

        //当天当前时间总投注信息
        DatawareBettingLogHour bettingLogDate = bettingLogHourService.getBettingByDate(params);
        if (bettingLogDate != null) {
            //当天总投注金额
            gameBettingInfoHour.setBettingAmount(bettingLogDate.getBettingAmount() == null ? 0D : bettingLogDate.getBettingAmount());
            //当天总投注笔数
            gameBettingInfoHour.setBettingCount(bettingLogDate.getBettingCount() == null ? 0L : bettingLogDate.getBettingCount().longValue());
            //当天总返奖金额
            gameBettingInfoHour.setReturnAmount(bettingLogDate.getResultAmount() == null ? 0D : bettingLogDate.getResultAmount());
            //当天总投注人数
            gameBettingInfoHour.setBettingUserCount(bettingLogDate.getBettingUserCount() == null ? 0L : bettingLogDate.getBettingUserCount().longValue());
        }
        return gameBettingInfoHour;
    }


}
