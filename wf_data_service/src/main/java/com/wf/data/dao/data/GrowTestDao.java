package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.GrowTest;

@MyBatisDao(tableName = "grow_test")
public interface GrowTestDao extends CrudDao<GrowTest> {

}
