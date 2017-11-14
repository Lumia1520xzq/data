package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.dao.entity.mycat.UicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author jianjian.huang
 * @date 2017年11月7日
 */

@Service
public class EsGrayService {
	
	@Autowired
	private EsClientFactory esClientFactory;

	public UicUser getNewUser(Long userId){
		if (userId == null) {
			return null;
		}
		UicUser user = esClientFactory.get(EsContents.UIC_USER, EsContents.UIC_USER, userId.toString(), UicUser.class);
		return user;
	}
	
}