package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.UserGroupContents;
import com.wf.uic.dao.entity.mysql.UicGroup;
import com.wf.uic.dao.mysql.UicGroupDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
@Service
public class UicGroupCrudService extends CrudService<UicGroupDao, UicGroup> {

	/**
	 * 根据父节点id删除
	 */
	public void deletByGroupTypeParentId(long groupTypeParentId) {
		dao.deletByGroupTypeParentId(groupTypeParentId);
	}

	/**
	 * 判断用户是否在指定的子组中
	 */
	public List<UicGroup> getInGroupByUserId(Long userId, List<Long> groupIds) {
		return dao.findFromSubGroup(userId, groupIds);
	}

	/**
	 * 判断用户是否在指定的父组中
	 */
	public List<UicGroup> getInParentGroupByUserId(Long userId, List<Long> groupIdList) {
		return dao.findFromParentGroup(userId, groupIdList);
	}

	/**
	 * 根据组获取组中的所有用户， 多个组以逗号分隔
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
	 * @return
	 */
	public List<Long> findGroupUsers(List<String> groupIdList) {
		return dao.findGroupUserIds(groupIdList);
	}

	/**
	 * 添加用户到黑白名单组
	 */
	public void saveUserToBlackWhiteGroup(Long userId, Long groupTypeId, String remark) {
		saveUserGroup(userId, groupTypeId, UserGroupContents.BLACK_WHITE_LIST_GROUP, remark);
	}

	/**
	 * 添加用户到黑白名单组
 	 */
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

}