package com.wf.uic.service;

import com.wf.core.service.CrudService;
import com.wf.uic.dao.entity.mycat.UicBehaviorRecord;
import com.wf.uic.dao.mycat.MycatUicBehaviorRecordDao;
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
