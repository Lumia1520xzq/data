package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_burying_point_day")
public interface DatawareBuryingPointDayDao extends CrudDao<DatawareBuryingPointDay> {

    long getCountByTime(Map<String,Object> map);

    Integer getGameDau(Map<String,Object> map);

    Long getDauByChannel(Map<String,Object> map);

    List<Long> getUserIdListByChannel(Map<String,Object> map);

    List<Long> getHistoryDauIds(Map<String,Object> map);

    List<Long> getGameDauIds(Map<String,Object> map);

    int deleteByDate(Map<String, Object> params);
}
