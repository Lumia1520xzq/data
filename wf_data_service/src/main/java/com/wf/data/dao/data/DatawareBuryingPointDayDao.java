package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;

import java.util.Map;

@MyBatisDao(tableName = "dataware_burying_point_day")
public interface DatawareBuryingPointDayDao extends CrudDao<DatawareBuryingPointDay> {

    long getCountByTime(Map<String,Object> map);
}
