package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;
import com.wf.data.dao.data.entity.DatawareBuryingPointHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_burying_point_hour")
public interface DatawareBuryingPointHourDao extends CrudDao<DatawareBuryingPointHour> {

    List<DatawareBuryingPointDay> findBuryingList(Map<String, Object> map);

    int updateUserGroup(DatawareBuryingPointHour pointDay);

    List<Long> findUserId(DatawareBuryingPointHour pointDay);

    long getCountByTime(Map<String,Object> map);
}
