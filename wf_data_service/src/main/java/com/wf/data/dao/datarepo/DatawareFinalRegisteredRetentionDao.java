package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRegisteredRetention;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_registered_retention")
public interface DatawareFinalRegisteredRetentionDao extends CrudDao<DatawareFinalRegisteredRetention> {

    DatawareFinalRegisteredRetention getRetentionByDate(Map<String, Object> map);


}
