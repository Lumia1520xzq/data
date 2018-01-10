package com.wf.data.dao.trans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.trans.entity.TransFragment;

@MyBatisDao(tableName = "trans_fragment")
public interface TransFragmentDao extends CrudDao<TransFragment> {

}
