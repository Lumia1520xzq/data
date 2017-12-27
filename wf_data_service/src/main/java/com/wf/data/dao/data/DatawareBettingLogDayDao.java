package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBettingLogDay;

import java.util.Map;

@MyBatisDao(tableName = "dataware_betting_log_day")
public interface DatawareBettingLogDayDao extends CrudDao<DatawareBettingLogDay> {

    long getCountByTime(Map<String,Object> map);
}
