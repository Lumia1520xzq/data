package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareConvertDay;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_convert_day")
public interface DatawareConvertDayDao extends CrudDao<DatawareConvertDay> {

    long getCountByTime(Map<String, Object> map);

    DatawareFinalChannelInfoAll getConvertByDate(Map<String, Object> map);

    List<Long> getUserIdByDate(Map<String, Object> map);

    List<Long> getUserIdList(Map<String, Object> map);

    Double getRechargeSumByDate(Map<String, Object> map);

    List<Long> getRechargeUserIdsByDate(Map<String, Object> map);

    int deleteByDate(Map<String, Object> params);


    Double getHistoryConvertByDate(Map<String, Object> map);
    
    Double getRegisteredConvertByDate(Map<String, Object> map);

    List<Long> getPayUserIdListByDate(Map<String, Object> payParams);

    DatawareConvertDay getInfoByUser(Map<String, Object> baseParam);

    String getFirstRechargeTime(Map<String, Object> baseParam);

    String getLastRechargeTime(Map<String, Object> baseParam);
}
