package com.wf.data.dao.tcard;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.tcard.entity.TcardUserBettingLog;
import com.wf.data.dto.TcardDto;

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

    /**
     * 获取三张汇总数据
     * @param params
     * @return
     */
    List<TcardDto> getSummaryRecord(Map<String, Object> params);

    /**
     * 获取初中高的投注人数
     * @param params
     * @return
     */
    Integer getUserCountByBettingType(Map<String,Object> params);

    /**
     * 获取初中高的桌数
     * @param params
     * @return
     */
    Integer getTablesByBettingType(Map<String,Object> params);


    Double getTableAmount(Map<String, Object> params);
}
