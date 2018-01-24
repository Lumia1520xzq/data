package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.BehaviorType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_behavior_type")
public interface BehaviorTypeDao extends CrudDao<BehaviorType> {
	
	BehaviorType getByEventId(@Param("eventId") Long eventId);

	List<Long> getActiveEventId();

	List<BehaviorType> findAll();

	List<BehaviorType> findByParentEventId(Long eventId);

    List<Long> findEventIds(Long parentEventId);

	String findEventNameById(Long eventId);
}
