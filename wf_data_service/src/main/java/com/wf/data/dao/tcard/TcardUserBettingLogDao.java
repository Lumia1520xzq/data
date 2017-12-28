package com.wf.data.dao.tcard;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.dao.tcard.entity.TcardUserBettingLog;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "tcard_user_betting_log")
public interface TcardUserBettingLogDao extends CrudDao<TcardUserBettingLog> {

    /**
     * 获取用户每小时投注信息汇总
     * @param params
     * @return
     */
    List<DatawareBettingLogHour> getGameBettingRecord(Map<String, Object> params);

    /**
     * 获取投注和返奖流水
     * @param params
     * @return
     */
    List<DatawareBettingLogHour> getBettingAndAward(Map<String, Object> params);
}
