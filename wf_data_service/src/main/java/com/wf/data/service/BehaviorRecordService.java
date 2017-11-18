package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.mycat.entity.BehaviorRecord;
import com.wf.data.dao.mycat.BehaviorRecordDao;
import org.springframework.stereotype.Service;

@Service
public class BehaviorRecordService extends CrudService<BehaviorRecordDao, BehaviorRecord> {

	@Override
	public void save(BehaviorRecord entity) {
		if(entity != null && entity.getUserId() == null){
			entity.setUserId(0L);
		}
		super.save(entity);
	}
}
