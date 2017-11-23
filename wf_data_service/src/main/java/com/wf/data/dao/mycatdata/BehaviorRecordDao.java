package com.wf.data.dao.mycatdata;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mycatdata.entity.BehaviorRecord;

@MyBatisDao(tableName = "uic_behavior_record")
public interface BehaviorRecordDao extends CrudDao<BehaviorRecord> {

}
