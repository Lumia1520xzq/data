package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalActivityCost;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_activity_cost")
public interface DatawareFinalActivityCostDao extends CrudDao<DatawareFinalActivityCost> {

    long getCountByTime(Map<String, Object> map);

    int deleteByDate(Map<String, Object> map);

    List<DatawareFinalActivityCost> getCostTop5Activity(Map<String, Object> map);

    List<DatawareFinalActivityCost> getActivityCostInfo(Map<String, Object> map);

    List<String> findDateList(Map<String, Object> map);
}

