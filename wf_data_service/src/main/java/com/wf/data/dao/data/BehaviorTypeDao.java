package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.BehaviorType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_behavior_type")
public interface BehaviorTypeDao extends CrudDao<BehaviorType> {

	/**
	 * 获取eventid的详情
	 * @return
	 */
	BehaviorType getByEventId(@Param("eventId") Long eventId);

	/**
	 * 获取所有可用的eventid
	 * @return
	 */
	List<Long> getActiveEventId();

	/**
	 * 获取所有信息
	 * @return
	 */
	List<BehaviorType> findAll();

	/**
	 * 获取某eventid下的子eventIDs的明细
	 * @param eventId
	 * @return
	 */
	List<BehaviorType> findByParentEventId(Long eventId);

	/**
	 * 获取某eventid下的子eventIDs
	 * @param parentEventId
	 * @return
	 */
    List<Long> findEventIds(Long parentEventId);

	/**
	 * 根据eventID获取名称
	 * @param eventId
	 * @return
	 */
	String findEventNameById(Long eventId);
}
