package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.mycatdata.TransAccountDao;
import com.wf.data.dao.mycatdata.entity.TransAccount;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransAccountService extends CrudService<TransAccountDao, TransAccount> {
	public Double getUseAmountByUserId(Map<String,Object> params) {
		return dao.getUseAmountByUserId(params);
	}
}
