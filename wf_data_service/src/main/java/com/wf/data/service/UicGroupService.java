package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.constants.UserGroupContents;
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
	 * 根据父节点id删除
	 * @param groupTypeParentId
	 */
	public void deletByGroupTypeParentId(long groupTypeParentId) {
		dao.deletByGroupTypeParentId(groupTypeParentId);
	}

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
	 * 判断用户是否在指定的父组中
	 * @param userId
	 * @param groupIdList
	 * @return
	 */
	public List<UicGroup> getInParentGroupByUserId(Long userId, List<Long> groupIdList) {
		return dao.findFromParentGroup(userId, groupIdList);
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
	
	public void saveUserToBlackWhiteGroup(Long userId, Long groupTypeId, String remark) {
		saveUserGroup(userId, groupTypeId, UserGroupContents.BLACK_WHITE_LIST_GROUP, remark);
	}

	// 添加用户到黑白名单组
	public void saveUserToBlackWhiteGroup(Long userId, Long groupTypeId) {
		saveUserToBlackWhiteGroup(userId, groupTypeId, "");
	}

	public void saveUserGroup(Long userId, Long groupTypeId, Long groupParentTypeId, String remark) {
		List<UicGroup> groups = getInParentGroupByUserId(userId, Arrays.asList(groupParentTypeId));
		if (groups == null || groups.isEmpty()) {
			UicGroup uicGroup = new UicGroup();
			uicGroup.setUserId(userId);
			uicGroup.setGroupTypeId(groupTypeId);
			uicGroup.setGroupTypeParentId(groupParentTypeId);
			uicGroup.setUserData(remark);
			save(uicGroup);
		} else {
			UicGroup uicGroup = groups.get(0);
			uicGroup.setGroupTypeId(groupTypeId);
			uicGroup.setGroupTypeParentId(groupParentTypeId);
			uicGroup.setUserData(remark);
			save(uicGroup);
		}
	}
	
	public List<Long> getUserInGroupType(Long userId) {
		return dao.getUserInGroupType(userId);
	}

	public void deletByUserIdAndParentId(Long userId, long singleRecharge) {
		dao.deletByUserIdAndParentId(userId, singleRecharge);
	}
	
	public void deletByUserId(long userId, long groupTypeId) {
		dao.deletByUserId(userId, groupTypeId);
	}
}