package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelConversion;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_conversion")
public interface DatawareFinalChannelConversionDao extends CrudDao<DatawareFinalChannelConversion> {

    long getCountByTime(Map<String,Object> map);
}
