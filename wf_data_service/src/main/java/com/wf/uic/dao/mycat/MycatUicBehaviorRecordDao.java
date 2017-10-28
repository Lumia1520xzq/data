package com.wf.uic.dao.mycat;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.uic.dao.entity.mycat.UicBehaviorRecord;

@MyBatisDao(tableName = "uic_behavior_record")
public interface MycatUicBehaviorRecordDao extends CrudDao<UicBehaviorRecord> {

}
