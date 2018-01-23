package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.dao.data.entity.DatawareConvertHour;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoHour;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_convert_hour")
public interface DatawareConvertHourDao extends CrudDao<DatawareConvertHour> {

    List<DatawareConvertDay> findConvertList(Map<String, Object> map);

    long getCountByTime(Map<String, Object> map);


    /**
     * 查询当前小时的充值
     * @param map
     * @return
     */
    DatawareFinalChannelInfoHour findRechargeByDate(Map<String, Object> map);

    /**
     * 查询一天截止当前小时的充值
     * @param map
     * @return
     */
    DatawareFinalChannelInfoHour findRechargeByTime(Map<String, Object> map);

}
