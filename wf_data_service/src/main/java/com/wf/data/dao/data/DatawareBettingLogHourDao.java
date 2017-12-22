package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;

@MyBatisDao(tableName = "dataware_betting_log_hour")
public interface DatawareBettingLogHourDao extends CrudDao<DatawareBettingLogHour> {

}
