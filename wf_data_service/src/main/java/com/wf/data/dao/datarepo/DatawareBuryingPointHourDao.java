package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointDay;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_burying_point_hour")
public interface DatawareBuryingPointHourDao extends CrudDao<DatawareBuryingPointHour> {

    List<DatawareBuryingPointDay> findBuryingList(Map<String, Object> map);

    int updateUserGroup(DatawareBuryingPointHour pointDay);

    List<Long> findUserId(DatawareBuryingPointHour pointDay);

    long getCountByTime(Map<String, Object> map);

    Integer getDauByDateAndHour(Map<String, Object> params);

    Long getDauByTime(Map<String, Object> params);

    int deleteByDate(Map<String, Object> params);
}
