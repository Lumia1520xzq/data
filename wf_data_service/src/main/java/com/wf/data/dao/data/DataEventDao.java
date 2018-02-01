package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataEvent;


@MyBatisDao(tableName = "data_event")
public interface DataEventDao extends CrudDao<DataEvent> {

	
}

