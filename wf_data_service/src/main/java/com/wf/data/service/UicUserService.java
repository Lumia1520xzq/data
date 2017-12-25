package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.mycatuic.UicUserDao;
import com.wf.data.dao.mycatuic.entity.UicUser;
import org.springframework.stereotype.Service;

/**
 * 用户信息操作
 * @author fxy
 * @date 2017/10/13
 */
@Service
public class UicUserService extends CrudService<UicUserDao, UicUser> {


	/**
	 * 根据userId获取用户
	 * @param id 用户标识
	 * @return
	 */
	@Override
	public UicUser get(Long id) {
		return cacheHander.cache(DataCacheKey.DATA_UIC_USER_BY_ID.key(id),()-> dao.get(id));
	}


}
