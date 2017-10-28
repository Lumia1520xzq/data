package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.dao.mysql.UicUserLogDao;
import com.wf.uic.dao.entity.mysql.UicUserLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UicUserLogCrudService extends CrudService<UicUserLogDao, UicUserLog> {

	@Async
	@Override
	public void save(UicUserLog uicUserLog) {
		super.save(uicUserLog);
	}

    public int countByLog(UicUserLog uicUserLog) {
        return dao.countByLog(uicUserLog);
    }
    
}
