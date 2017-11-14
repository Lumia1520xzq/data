package com.wf.data.dao.mysql;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.UicGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_group")
public interface UicGroupDao extends CrudDao<UicGroup> {

	void deletByGroupTypeParentId(@Param("groupTypeParentId") long groupTypeParentId);
	
	void deletByUserIdAndParentId(@Param("userId") long userId, @Param("groupTypeParentId") long groupTypeParentId);

	List<UicGroup> findFromSubGroup(@Param("userId") Long userId, @Param("groupIdList") List<Long> groupIdList);

	List<UicGroup> findFromParentGroup(@Param("userId") Long userId, @Param("groupIdList") List<Long> groupIdList);

	List<Long> findGroupUserIds(@Param("groupIdList") List<String> groupIdList);

	List<Long> getUserInGroupType(@Param("userId") Long userId);

	@Delete("delete from uic_group where user_id = #{userId} and group_type_id = #{groupTypeId}")
	void deletByUserId(@Param("userId") long userId, @Param("groupTypeId") long groupTypeId);
}
