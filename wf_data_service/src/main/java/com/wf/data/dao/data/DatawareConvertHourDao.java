package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.dao.data.entity.DatawareConvertHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_convert_hour")
public interface DatawareConvertHourDao extends CrudDao<DatawareConvertHour> {

    List<DatawareConvertDay> findConvertList(Map<String, Object> map);

    long getCountByTime(Map<String,Object> map);

}
