package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_betting_log_hour")
public interface DatawareBettingLogHourDao extends CrudDao<DatawareBettingLogHour> {

    int updateUserGroup(DatawareBettingLogHour logHour);

    List<Long> findUserId(DatawareBettingLogHour logHour);

    List<Long> findUserIdByTime(DatawareBettingLogHour logHour);

    List<DatawareBettingLogDay> findBettingList(Map<String, Object> params);

    long getCountByTime(Map<String, Object> map);

    DatawareBettingLogHour getSumByDateAndHour(Map<String, Object> params);

    DatawareBettingLogHour getBettingByDate(Map<String, Object> params);

    int deleteByDate(Map<String, Object> params);
}
