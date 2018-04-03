package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.core.utils.type.DateUtils;
import com.wf.data.dao.datarepo.DatawareUserInfoExtendGameDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendGame;
import com.wf.data.dto.TcardDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DatawareUserInfoExtendGameService extends CrudService<DatawareUserInfoExtendGameDao, DatawareUserInfoExtendGame> {

    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    public DatawareUserInfoExtendGame getByUserIdAndGameType(Map<String,Object> params){
        return dao.getByUserIdAndGameType(params);
    }

    public void updateNewUserFlag(String month){
         dao.updateNewUserFlag(month);
    }

    public Long getAllCount(){
         return dao.getAllCount();
    }

    public void deleteAll(){
        dao.deleteAll();
    }

    public void updateBettingData(Map<String,Object> params){
        dao.updateBettingData(params);
    }

    public void changeUserInfoExtendGame(List<Long> activeUserIds,Integer gameType){
        String yesterDay = DateUtils.getYesterdayDate();
        String dayWeekBefore = DateUtils.formatDate(DateUtils.getPrevDate(new Date(),7));
        List<DatawareUserInfoExtendGame> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activeUserIds)){
            Map<String,Object> params = new HashMap<>();
            for (Long activeUserId:activeUserIds) {
                params.put("userId",activeUserId);
                //用户维度清洗表涉及到的游戏
                params.put("gameType",gameType);
                //判断是否为游戏新用户
                DatawareUserInfoExtendGame userInfoExtendGame = getByUserIdAndGameType(params);
                //新用户
                if(userInfoExtendGame == null){
                    DatawareUserInfoExtendGame newRecord = new DatawareUserInfoExtendGame();
                    //1、是否为游戏新用户
                    newRecord.setNewUserFlag(0);
                    //2、首次活跃时间
                    newRecord.setFirstActiveTime(yesterDay);
                    params.put("beginDate",yesterDay);
                    params.put("endDate",yesterDay);
                    TcardDto dto = datawareBettingLogDayService.getBettingByUserIdAndGameType(params);
                    //3、该游戏累计投注金额
                    newRecord.setSumBettingAmount(dto.getBettingAmount());
                    //4、该游戏累计投注次数
                    newRecord.setSumBettingCount(dto.getBettingCount());
                    //5、近七日投注金额
                    newRecord.setSevenSumBettingAmount(dto.getBettingAmount());
                    //4、近七日投注次数
                    newRecord.setSevenSumBettingCount(dto.getBettingCount());
                    newRecord.setUserId(activeUserId);
                    newRecord.setGameType(gameType);
                    list.add(newRecord);
                }else{
                    params.put("beginDate",dayWeekBefore);
                    params.put("endDate",yesterDay);
                    TcardDto dto = datawareBettingLogDayService.getBettingByUserIdAndGameType(params);
                    //近七日投注金额
                    Double sevenSumBettingAmount = dto.getBettingAmount();
                    //七日投注次数
                    Integer sevenSumBettingCount = dto.getBettingCount();
                    params.put("beginDate",null);
                    params.put("endDate",yesterDay);
                    dto = datawareBettingLogDayService.getBettingByUserIdAndGameType(params);
                    Double sumBettingAmount = dto.getBettingAmount();
                    Integer sumBettingCount= dto.getBettingCount();
                    params.put("sevenSumBettingAmount",sevenSumBettingAmount);
                    params.put("sevenSumBettingCount",sevenSumBettingCount);
                    params.put("sumBettingAmount",sumBettingAmount);
                    params.put("sumBettingCount",sumBettingCount);
                    updateBettingData(params);
                 }
            }
        }
        batchSave(list);
    }



}

