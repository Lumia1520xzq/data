package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendGame;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareUserInfoExtendGameService;
import com.wf.data.service.data.DatawareUserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Joe Huang
 * date 2018/3/14
 */

@Service
public class UserInfoExtendGameService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**每次查询数量*/
    private static final long PAGE_SIZE = 50000;

    @Autowired
    private DatawareUserInfoService userInfoService;
    @Autowired
    private DatawareBuryingPointDayService buryingPointDayService;
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private DatawareBettingLogDayService bettingLogDayService;
    @Autowired
    private DatawareUserInfoExtendGameService  datawareUserInfoExtendGameService;

    /**
     * 重置 dataware_user_info_extend_game
     */
    public void resetUserInfoExtendGame(){
        String yesterday = DateUtils.getYesterdayDate();
        String dayWeekBefore = DateUtils.formatDate(com.wf.core.utils.type.DateUtils.getPrevDate(new Date(),7));
        try {
            //删除原有数据
            long allUserExtendGameCount = datawareUserInfoExtendGameService.getAllCount();
            if (allUserExtendGameCount > 0) {
                datawareUserInfoExtendGameService.deleteAll();
            }
            String gameStr = dataConfigService.findByName(DataConstants.DATA_DATAWARE_USERINFO_EXTEND_GAME).getValue();
            /*分页查询dataware_user_info_extend_game数据*/
            HashMap<String, Object> alluserInfoParam = new HashMap<>();
            alluserInfoParam.put("yesterdayParam", yesterday);
            long userCount = userInfoService.getCountByTime(alluserInfoParam);
            //页数
            int pageCount = (int) Math.ceil(1.0 * userCount / PAGE_SIZE);
            for (int i = 1; i <= pageCount; i++) {
                long minIndex = (i - 1) * PAGE_SIZE;
                long maxIndex = PAGE_SIZE;
                Map<String, Object> params = new HashMap<>();
                params.put("minIndex", minIndex);
                params.put("maxIndex", maxIndex);
                params.put("endDate", yesterday);
                List<DatawareUserInfo> userInfos = userInfoService.getBaseUserInfoLimit(params);
                if (CollectionUtils.isNotEmpty(userInfos)) {
                    for (DatawareUserInfo userInfo : userInfos) {
                        List<DatawareUserInfoExtendGame> list = new ArrayList<>();
                        if(StringUtils.isNotEmpty(gameStr)) {
                            String[] games = gameStr.split(",");
                            Map<String,Object> map = new HashMap<>();
                            map.put("userId",userInfo.getUserId());
                            for (String game:games) {
                                //先判断此用户是否该游戏中首次投注记录
                                map.put("gameType",Integer.parseInt(game));
                                DatawareBettingLogDay bettingLogDay = bettingLogDayService.getByUserIdAndGameType(map);
                                //如果有投注信息
                                if(bettingLogDay != null) {
                                    DatawareUserInfoExtendGame newRecord = new DatawareUserInfoExtendGame();
                                    newRecord.setUserId(userInfo.getUserId());
                                    newRecord.setGameType(Integer.parseInt(game));
                                    newRecord.setNewUserFlag(1);
                                    newRecord.setFirstActiveTime(bettingLogDay.getBettingDate());
                                    map.put("beginDate",null);
                                    map.put("endDate",yesterday);
                                    TcardDto dto = bettingLogDayService.getBettingByUserIdAndGameType(map);
                                    //累计投注金额
                                    newRecord.setSumBettingAmount(dto.getBettingAmount());
                                    //累计投注次数
                                    newRecord.setSumBettingCount(dto.getBettingCount());
                                    map.put("beginDate",dayWeekBefore);
                                    dto = bettingLogDayService.getBettingByUserIdAndGameType(map);
                                    //近7日累计投注金额
                                    newRecord.setSevenSumBettingAmount(dto.getBettingAmount());
                                    //近7日累计投注次数
                                    newRecord.setSevenSumBettingCount(dto.getBettingCount());
                                    list.add(newRecord);
                                }
                            }
                            datawareUserInfoExtendGameService.batchSave(list);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("重置用户维度基本信息失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("用户维度基本信息表重置结束:traceId={}", TraceIdUtils.getTraceId());
    }

    /**
     * 定时清洗用户基本信息
     */
    public void toDoAnalysis() {
    String yesterday = DateUtils.getYesterdayDate();
    String yesMonth = DateUtils.formatDate(DateUtils.getPrevDate(new Date(),1),DateUtils.MONTH_PATTERN);
    //1.把用户标识为新用户的新用户改成老用户
    datawareUserInfoExtendGameService.updateNewUserFlag(yesMonth);
    /* 获取每个游戏的活跃用户，只更改活跃用户的信息*/
        //1.获取昨天的每个对应的活跃用户
        Map<String, Object> param = new HashMap<>();
        param.put("businessDate", yesterday);
        String gameStr = dataConfigService.findByName(DataConstants.DATA_DATAWARE_USERINFO_EXTEND_GAME).getValue();
        if(StringUtils.isNotEmpty(gameStr)) {
            String[] games = gameStr.split(",");
            for (String game : games){
                param.put("gameType",game);
                List<Long> activeUserIds = buryingPointDayService.getUserIdListByChannel(param);
                datawareUserInfoExtendGameService.changeUserInfoExtendGame(activeUserIds,Integer.parseInt(game));
            }
        }
    }

}
