package com.wf.data.service;

import com.wf.core.cache.CacheData;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.data.BehaviorTypeDao;
import com.wf.data.dao.data.entity.BehaviorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jijie.chen
 *
 */
@Service
public class BehaviorTypeService extends CrudService<BehaviorTypeDao, BehaviorType> {

	public BehaviorType getByEventId(final Long eventId) {
		return cacheHander.cache(DataCacheKey.UIC_USER_BEHAVIORTYPE_BY_EVENTID.key(eventId), new CacheData() {
			@Override
			public Object findData() {
				return dao.getByEventId(eventId);
			}
		});
		
	}

	/**
	 * 获取所有游戏日活埋点
	 * @return
	 */
	public List<Long> getActiveEventId(){
		return cacheHander.cache(DataCacheKey.DATA_ACTIVE_EVENT.key(),() -> dao.getActiveEventId());
	}

	public List<BehaviorType> findAll() {
		return dao.findAll();
	}

	public List<BehaviorType> findByParentEventId(Long eventId) {
		return dao.findByParentEventId(eventId);
	}

	public List<Long> findEventIds(Long parentEventId) {
		return dao.findEventIds(parentEventId);
	}

	public String findEventNameById(Long eventId) {
		return dao.findEventNameById(eventId);
	}

}