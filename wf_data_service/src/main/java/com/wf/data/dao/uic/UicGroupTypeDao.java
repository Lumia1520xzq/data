package com.wf.data.dao.uic;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.uic.entity.UicGroupType;

import java.util.List;

@MyBatisDao(tableName = "uic_group_type")
public interface UicGroupTypeDao extends CrudDao<UicGroupType> {

	List<UicGroupType> findByParentId(long parentId);
}
