package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareConvertDay;
import com.wf.data.dao.datarepo.entity.DatawareConvertHour;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoHour;

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

    int deleteByDate(Map<String, Object> params);

    Long findrechargeCountByDate(Map<String, Object> map);

    DatawareConvertHour getNewUserConverInfo(Map<String, Object> params);
}
