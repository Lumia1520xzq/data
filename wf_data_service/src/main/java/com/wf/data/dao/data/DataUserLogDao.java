package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataUserLog;

@MyBatisDao(tableName = "data_user_log")
public interface DataUserLogDao extends CrudDao<DataUserLog> {

}
