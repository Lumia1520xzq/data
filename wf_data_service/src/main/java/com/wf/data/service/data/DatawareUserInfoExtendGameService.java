package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.datarepo.DatawareUserInfoExtendGameDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendGame;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.DataConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DatawareUserInfoExtendGameService extends CrudService<DatawareUserInfoExtendGameDao, DatawareUserInfoExtendGame> {

    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;

    public DatawareUserInfoExtendGame getByUserIdAndGameType(Map<String,Object> params){
        return dao.getByUserIdAndGameType(params);
    }

    public void changeUserInfoExtendGame(List<Long> activeUserIds){
        String yesterDay = DateUtils.getYesterdayDate();

        List<DatawareUserInfoExtendGame> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activeUserIds)){
            Map<String,Object> params = new HashMap<>();
            for (Long activeUserId:activeUserIds) {
                params.put("userId",activeUserId);
                //用户维度清洗涉及到的游戏
                String gameStr = dataConfigService.findByName(DataConstants.DATA_DATAWARE_USERINFO_EXTEND_GAME).getValue();
                if(StringUtils.isNotEmpty(gameStr)){
                    String[] games = gameStr.split(",");
                    for(String game:games){
                        params.put("gameType",game);
                        //判断是否为游戏新用户
                        DatawareUserInfoExtendGame userInfoExtendGame = dao.getByUserIdAndGameType(params);
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
                            //4、该游戏累计投注次数
                            newRecord.setSevenSumBettingCount(dto.getBettingCount());
                            newRecord.setUserId(activeUserId);
                            newRecord.setGameType(Integer.parseInt(game));
                            list.add(newRecord);
                        }else{
                            //如果是老用户


                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.getYesterdayDate());
        System.out.println(DateUtils.getPrevDate(new Date(),7));
    }
}

