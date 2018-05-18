package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_cost")
public interface DatawareFinalChannelCostDao extends CrudDao<DatawareFinalChannelCost> {

    long getCountByTime(Map<String, Object> map);

    DatawareFinalChannelCost findByDate(Map<String, Object> params);

    Double findMonthCost(Map<String, Object> params);

    int deleteByDate(Map<String, Object> map);

    List<DatawareFinalChannelCost> getCostTop5Channel(Map<String, Object> params);

    List<String> findDateList(Map<String, Object> params);

    List<DatawareFinalChannelCost> getCostInfo(Map<String,Object> map);
}
