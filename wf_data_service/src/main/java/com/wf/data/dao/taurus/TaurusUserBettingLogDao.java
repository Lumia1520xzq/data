package com.wf.data.dao.taurus;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.taurus.entity.TaurusUserBettingLog;

import java.util.Map;

@MyBatisDao(tableName = "taurus_user_betting_log")
public interface TaurusUserBettingLogDao extends CrudDao<TaurusUserBettingLog> {

    Double getTableAmount(Map<String,Object> params);

    Integer getUserCountByBettingType(Map<String,Object> params);

    Integer getTablesByBettingType(Map<String,Object> params);
}
