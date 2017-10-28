package com.wf.uic.service;

import com.wf.core.cache.CacheData;
import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.ControllerCacheKey;
import com.wf.uic.dao.entity.mycat.UicBehaviorType;
import com.wf.uic.dao.mysql.UicBehaviorTypeDao;
import org.springframework.stereotype.Service;

/**
 * @author jijie.chen
 *
 */
@Service
public class UicBehaviorTypeService extends CrudService<UicBehaviorTypeDao, UicBehaviorType> {

	public UicBehaviorType getByEventId(final Long eventId) {
		return cacheHander.cache(ControllerCacheKey.UIC_USER_BEHAVIORTYPE_BY_EVENTID.key(eventId), new CacheData() {
			@Override
			public Object findData() {
				return dao.getByEventId(eventId);
			}
		});
		
	}

}