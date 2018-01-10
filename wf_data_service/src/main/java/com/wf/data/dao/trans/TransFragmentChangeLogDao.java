package com.wf.data.dao.trans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.trans.entity.TransFragmentChangeLog;

@MyBatisDao(tableName = "trans_fragment_change_log")
public interface TransFragmentChangeLogDao extends CrudDao<TransFragmentChangeLog> {

}
