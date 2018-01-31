package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataDailyRecord;


@MyBatisDao(tableName = "data_daily_record")
public interface DataDailyRecordDao extends CrudDao<DataDailyRecord> {
	


}
