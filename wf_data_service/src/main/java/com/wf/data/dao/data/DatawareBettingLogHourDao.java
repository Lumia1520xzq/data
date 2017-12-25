package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBettingLogDay;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_betting_log_hour")
public interface DatawareBettingLogHourDao extends CrudDao<DatawareBettingLogHour> {

    int updateUserGroup(DatawareBettingLogHour logHour);

    List<Long> findUserId(DatawareBettingLogHour logHour);

    List<DatawareBettingLogDay> findBettingList(Map<String ,Object> params);

}
