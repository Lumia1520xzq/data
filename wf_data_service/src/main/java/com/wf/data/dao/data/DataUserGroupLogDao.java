package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataUserGroupLog;

@MyBatisDao(tableName = "data_user_group_log")
public interface DataUserGroupLogDao extends CrudDao<DataUserGroupLog> {

}
