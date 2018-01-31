package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredArpu;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_registered_arpu")
public interface DatawareFinalRegisteredArpuDao extends CrudDao<DatawareFinalRegisteredArpu> {

    DatawareFinalRegisteredArpu getArpuByDate(Map<String,Object> map);
}
