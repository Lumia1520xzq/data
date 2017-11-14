package com.wf.data.dao.mysql;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.UicGroupType;

import java.util.List;

@MyBatisDao(tableName = "uic_group_type")
public interface UicGroupTypeDao extends CrudDao<UicGroupType> {

	List<UicGroupType> findByParentId(long parentId);
}
