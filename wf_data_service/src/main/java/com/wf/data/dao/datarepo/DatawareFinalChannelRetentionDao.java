package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelRetention;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_retention")
public interface DatawareFinalChannelRetentionDao extends CrudDao<DatawareFinalChannelRetention> {

    long getCountByTime(Map<String, Object> map);

    long deleteByDate(Map<String, Object> map);

    DatawareFinalChannelRetention findByDate(Map<String, Object> params);
}
