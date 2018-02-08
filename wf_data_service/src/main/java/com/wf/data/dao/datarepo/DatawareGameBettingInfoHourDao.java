package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;

import java.util.Map;

@MyBatisDao(tableName = "dataware_game_betting_info_hour")
public interface DatawareGameBettingInfoHourDao extends CrudDao<DatawareGameBettingInfoHour> {

    long getCountByTime(Map<String, Object> params);

    void deleteByDate(Map<String, Object> params);
}
