package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.TargetDefine;

@MyBatisDao(tableName = "target_define")
public interface TargetDefineDao extends CrudDao<TargetDefine> {

}
