package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.BehaviorType;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "uic_behavior_type")
public interface BehaviorTypeDao extends CrudDao<BehaviorType> {
	
	BehaviorType getByEventId(@Param("eventId") Long eventId);

}