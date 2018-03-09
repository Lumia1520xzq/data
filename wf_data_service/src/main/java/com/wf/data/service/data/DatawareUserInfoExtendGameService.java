package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.datarepo.DatawareUserInfoExtendGameDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendGame;
import com.wf.data.service.DataConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                            newRecord.setFirstActiveTime(DateUtils.getYesterdayDate());
                            //3、该游戏累计投注金额

                            //4、该游戏累计投注次数

                        }else{

                        }
                    }
                }

                //判断是否为游戏新用户
//                if (userInfoExtendBase == null) {//新增用户
//                    //获取新用户基本信息
//                    DatawareUserInfo userInfo = userInfoService.get(activeUserId);
//                    if (userInfo != null) {
//                        saveInfo(userInfo);
//                    }
//                } else {//老用户
//                    Map<String, Object> userParam = new HashMap<>();
//                    userParam.put("userId", activeUserId);
//
//                    //剩余金叶子数
//                    Double useAmount = judgeNull(transAccountService.getUseAmountByUserId(userParam));
//                    //出口成本
//                    Double costAmount = judgeNull(phyAwardsSendlogService.getRmbAmountByUserId(userParam));
//                    //最后一次活跃时间
//                    String lastActiveDate = buryingPointDayService.getLastActiveDate(userParam);
//                    //活跃天数
//                    int activeDates = buryingPointDayService.getActiveDatesByUser(userParam);
//
//                    userInfoExtendBase.setNoUseGoldAmount(useAmount);
//                    userInfoExtendBase.setCostAmount(costAmount);
//                    userInfoExtendBase.setLastActiveDate(lastActiveDate);
//                    userInfoExtendBase.setActiveDates(activeDates);
//                    userInfoExtendBaseService.save(userInfoExtendBase);
//                }


            }
        }
    }





}

