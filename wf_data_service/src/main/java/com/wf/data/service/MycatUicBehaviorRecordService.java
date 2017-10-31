package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.entity.mycat.UicBehaviorRecord;
import com.wf.data.dao.mycat.MycatUicBehaviorRecordDao;
import org.springframework.stereotype.Service;

@Service
public class MycatUicBehaviorRecordService extends CrudService<MycatUicBehaviorRecordDao, UicBehaviorRecord> {

	@Override
	public void save(UicBehaviorRecord entity) {
		if(entity != null && entity.getUserId() == null){
			entity.setUserId(0L);
		}
		super.save(entity);
	}
}
