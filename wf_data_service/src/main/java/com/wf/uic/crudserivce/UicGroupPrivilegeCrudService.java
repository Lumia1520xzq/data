package com.wf.uic.crudserivce;

import com.wf.core.cache.CacheData;
import com.wf.core.cache.CacheKey;
import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.UserGroupCacheKey;
import com.wf.uic.common.constants.UserGroupContents;
import com.wf.uic.dao.entity.mysql.UicGroup;
import com.wf.uic.dao.entity.mysql.UicGroupPrivilege;
import com.wf.uic.dao.mysql.UicGroupPrivilegeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author
 *
 */
@Service
public class UicGroupPrivilegeCrudService extends CrudService<UicGroupPrivilegeDao, UicGroupPrivilege> {

	@Autowired
	private UicGroupCrudService uicGroupCrudService;

	/**
	 * 查询用户是否拥有黑白名单的某一权限
	 * @param userId
	 * @param privilegeId
	 * @return
	 */
	public boolean getUserPrivilege(final Long userId, final Long privilegeId) {
		return cacheHander.cache(UserGroupCacheKey.PLAT_USER_PRIVILEGE.key(userId, privilegeId), new CacheData() {
			@Override
			public Object findData() {
				List<UicGroup> groups = uicGroupCrudService.getInParentGroupByUserId(userId,
						Arrays.asList(UserGroupContents.BLACK_WHITE_LIST_GROUP));
				//用户不在黑白名单组，不 做任何判断
				if (groups == null || groups.size() == 0){
					logger.info("用户id:{}不在黑白名单组", userId);
					return true;
				}
				Long groupTypeId = groups.get(0).getGroupTypeId();
				List<Long> groupPrivilegeIds = null;
				if (groupTypeId != null) {
					groupPrivilegeIds = dao.getPrivilegeIdListByGroupId(groupTypeId);
					for (Long id : groupPrivilegeIds) {
						if (id.equals(privilegeId)) {
							return true;
						}
					}
				}
				return false;
			}
		}, CacheKey.DAY_1);

	}

}