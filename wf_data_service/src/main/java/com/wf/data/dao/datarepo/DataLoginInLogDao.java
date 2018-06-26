package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DataLoginInLog;

/**
 * 数据系统登录log
 */
@MyBatisDao(tableName = "data_login_in_log")
public interface DataLoginInLogDao extends CrudDao<DataLoginInLog> {

}
