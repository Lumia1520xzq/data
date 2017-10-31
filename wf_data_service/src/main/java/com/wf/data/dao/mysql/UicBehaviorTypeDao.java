package com.wf.data.dao.mysql;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.UicBehaviorType;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "uic_behavior_type")
public interface UicBehaviorTypeDao extends CrudDao<UicBehaviorType> {
	
	UicBehaviorType getByEventId(@Param("eventId") Long eventId);

}
