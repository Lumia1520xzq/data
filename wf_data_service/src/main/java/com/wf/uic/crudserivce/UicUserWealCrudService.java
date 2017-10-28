package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.dao.mysql.UicUserWealDao;
import com.wf.uic.dao.entity.mysql.UicUserWeal;
import org.springframework.stereotype.Service;

/**
 * 用户福利
 * 
 * @author jijie.chen
 *
 */
@Service
public class UicUserWealCrudService extends CrudService<UicUserWealDao, UicUserWeal> {

	public UicUserWeal getByUserId(Long userId, int businessType) {
		return dao.getByUserId(userId, businessType);
	}

}
