package com.wf.data.service;

import com.wf.core.cache.CacheData;
import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.entity.mysql.UicBehaviorType;
import com.wf.data.dao.mysql.UicBehaviorTypeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jijie.chen
 *
 */
@Service
public class UicBehaviorTypeService extends CrudService<UicBehaviorTypeDao, UicBehaviorType> {


	@Transactional(transactionManager = "tractionManager-mysql")
	@DataSource(name = DataSourceContext.DATA_SOURCE_WRITE)
	public UicBehaviorType getByEventId(final Long eventId) {
		return cacheHander.cache(DataCacheKey.UIC_USER_BEHAVIORTYPE_BY_EVENTID.key(eventId), new CacheData() {
			@Override
			public Object findData() {
				return dao.getByEventId(eventId);
			}
		});
		
	}

}