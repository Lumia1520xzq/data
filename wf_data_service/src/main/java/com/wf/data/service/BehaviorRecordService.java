package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.mycatdata.BehaviorRecordDao;
import com.wf.data.dao.mycatdata.entity.BehaviorRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BehaviorRecordService extends CrudService<BehaviorRecordDao, BehaviorRecord> {

	@Async
	@Override
	public void save(BehaviorRecord entity) {
		if(entity != null && entity.getUserId() == null){
			entity.setUserId(0L);
		}
		super.save(entity);
	}
}
