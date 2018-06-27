package com.wf.data.dao.landlords;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.landlords.entity.LandlordsUserAmountLog;
import com.wf.data.dto.LandlordsDto;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "landlords_user_amount_log")
public interface LandlordsUserAmountLogDao extends CrudDao<LandlordsUserAmountLog> {

    List<LandlordsDto> getLandlordList(Map<String, Object> map);


    LandlordsDto getBettingInfo(Map<String, Object> map);

    /**
     * 获取桌费，减去返回桌费
     * @param map
     * @return
     */
    Double getTableAmount(Map<String, Object> map);

    /**
     * 获取道具金额
     * @param map
     * @return
     */
    Double getToolsAmount(Map<String, Object> map);

    /**
     * 获取局数
     * @param map
     * @return
     */
    Integer getGameTimes(Map<String, Object> map);

}
