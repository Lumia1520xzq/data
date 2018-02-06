package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_info_hour")
public interface DatawareFinalChannelInfoHourDao extends CrudDao<DatawareFinalChannelInfoHour> {
    long getCountByTime(Map<String, Object> map);

    int deleteByDate(Map<String, Object> params);

    List<DatawareFinalChannelInfoHour> getByDateAndHour(Map<String, Object> map);


    List<DatawareFinalChannelInfoHour> getSumDataByDateAndHour(Map<String, Object> map);

    DatawareFinalChannelInfoHour findDataForPandect(Map<String, Object> map);
}
