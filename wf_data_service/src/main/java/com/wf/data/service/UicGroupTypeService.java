package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.dao.entity.mysql.UicGroupType;
import com.wf.data.dao.mysql.UicGroupTypeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UicGroupTypeService extends CrudService<UicGroupTypeDao, UicGroupType> {

	/**
	 * 获取黑名单用户组下的子节点
	 * @return
	 */
	public List<UicGroupType> findBlackListGroup() {
		return dao.findByParentId(UserGroupContents.BLACK_LIST_GROUP);
	}
	
	/**
	 * 获取白名单用户组下的子节点
	 * @return
	 */
	public List<UicGroupType> findWhiteListGroup() {
		return dao.findByParentId(UserGroupContents.WHITE_LIST_GROUP);
	}
	
}
