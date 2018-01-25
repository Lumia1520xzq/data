package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_info_hour")
public interface DatawareFinalChannelInfoHourDao extends CrudDao<DatawareFinalChannelInfoHour> {
    long getCountByTime(Map<String, Object> map);

    List<DatawareFinalChannelInfoHour> getByDateAndHour(Map<String, Object> map);


    List<DatawareFinalChannelInfoHour> getSumDataByDateAndHour(Map<String, Object> map);

}
