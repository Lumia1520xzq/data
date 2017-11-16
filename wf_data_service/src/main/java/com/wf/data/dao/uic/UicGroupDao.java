package com.wf.data.dao.uic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.uic.entity.UicGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_group")
public interface UicGroupDao extends CrudDao<UicGroup> {


	List<UicGroup> findFromSubGroup(@Param("userId") Long userId, @Param("groupIdList") List<Long> groupIdList);

	List<Long> findGroupUserIds(@Param("groupIdList") List<String> groupIdList);
}
