package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredRetention;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_registered_retention")
public interface DatawareFinalRegisteredRetentionDao extends CrudDao<DatawareFinalRegisteredRetention> {

    DatawareFinalRegisteredRetention getRetentionByDate(Map<String, Object> map);


}
