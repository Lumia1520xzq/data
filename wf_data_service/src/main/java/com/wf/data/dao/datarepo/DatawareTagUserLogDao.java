package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareTagUserLog;

import java.util.Map;

@MyBatisDao(tableName = "dataware_tag_user_log")
public interface DatawareTagUserLogDao extends CrudDao<DatawareTagUserLog> {

    int deleteByDate(Map<String, Object> params);

}
