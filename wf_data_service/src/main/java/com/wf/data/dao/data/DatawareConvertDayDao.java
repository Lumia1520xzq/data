package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_convert_day")
public interface DatawareConvertDayDao extends CrudDao<DatawareConvertDay> {

    long getCountByTime(Map<String, Object> map);

    DatawareFinalChannelInfoAll getConvertByDate(Map<String, Object> map);

    List<Long> getUserIdByDate(Map<String,Object> map);

    Double getRechargeSumByDate(Map<String,Object> map);

    List<Long> getRechargeUserIdsByDate(Map<String,Object> map);

    int deleteByDate(Map<String, Object> params);


    Double getHistoryConvertByDate(Map<String, Object> map);
}
