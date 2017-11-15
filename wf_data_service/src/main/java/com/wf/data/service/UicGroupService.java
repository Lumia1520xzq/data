package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.entity.mysql.UicGroup;
import com.wf.data.dao.mysql.UicGroupDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jijie.chen
 *
 */
@Service
public class UicGroupService extends CrudService<UicGroupDao, UicGroup> {


	/**
	 * 判断用户是否在指定的子组中
	 * 
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	public List<UicGroup> getInGroupByUserId(Long userId, List<Long> groupIds) {
		return dao.findFromSubGroup(userId, groupIds);
	}


	/**
	 * 根据组获取组中的所有用户， 多个组以逗号分隔
	 * 
	 * @param groupIds
	 * @return
	 */
	public List<Long> findGroupUsers(String groupIds) {
		List<Long> userIdList = new ArrayList<Long>();
		String[] groupIdsArr = groupIds.split(",");
		if (groupIdsArr != null && groupIdsArr.length > 0) {
			List<String> groupIdList = Arrays.asList(groupIdsArr);
			userIdList = dao.findGroupUserIds(groupIdList);
		}
		return userIdList;
	}

	/**
	 * 根据组获取组中的所有用户
	 * @param groupIdList
	 * @return
	 */
	public List<Long> findGroupUsers(List<String> groupIdList) {
		return dao.findGroupUserIds(groupIdList);
	}
	
}