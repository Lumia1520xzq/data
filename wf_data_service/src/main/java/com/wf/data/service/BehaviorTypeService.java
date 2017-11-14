package com.wf.data.service;

import com.wf.core.cache.CacheData;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.entity.mysql.BehaviorType;
import com.wf.data.dao.mysql.BehaviorTypeDao;
import org.springframework.stereotype.Service;

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

}