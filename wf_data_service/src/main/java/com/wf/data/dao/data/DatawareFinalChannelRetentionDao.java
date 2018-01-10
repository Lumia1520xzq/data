package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_retention")
public interface DatawareFinalChannelRetentionDao extends CrudDao<DatawareFinalChannelRetention> {

    long getCountByTime(Map<String,Object> map);

    DatawareFinalChannelRetention findByDate(@Param("date")  String date);
}
