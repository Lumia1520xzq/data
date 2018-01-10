package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_cost")
public interface DatawareFinalChannelCostDao extends CrudDao<DatawareFinalChannelCost> {

    long getCountByTime(Map<String, Object> map);

    DatawareFinalChannelCost findByDate(Map<String,Object> params);
}
