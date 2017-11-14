package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.entity.mysql.ReportGrayUser;
import com.wf.data.dao.mysql.ReportGrayUserDao;
import org.springframework.stereotype.Service;

@Service
public class ReportGrayUserService extends CrudService<ReportGrayUserDao, ReportGrayUser> {
	
	public ReportGrayUser getByUserId(Long userId) {
		return dao.getByUserId(userId);
	}
	
}
