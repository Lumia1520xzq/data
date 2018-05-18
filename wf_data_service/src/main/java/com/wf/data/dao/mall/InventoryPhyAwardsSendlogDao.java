package com.wf.data.dao.mall;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "inventory_phy_awards_sendlog")
public interface InventoryPhyAwardsSendlogDao extends CrudDao<InventoryPhyAwardsSendlog> {

    public InventoryPhyAwardsSendlog getByActivityIdBusinessId(@Param("activityId") Long activityId, @Param("businessId") Long businessId);

    Double getRmbAmountByChannel(Map<String,Object> map);

    Double getRmbAmountByUserId(Map<String, Object> userParam);

    Long getActivityUsersCount(Map<String,Object> map);

    Double getRmbAmountByActivity(Map<String,Object> map);

    List<Map<String, Object>> getRmbAmountByChannelAndActivity(Map<String,Object> map);

    List<Map<String, Object>> getActivityUsersCountByChannel(Map<String,Object> map);

    List<Map<String,Object>> getRmbAmountsByActivity(Map<String, Object> params);

    List<Map<String,Object>> getChannelUsersCountByActivity(Map<String, Object> params);
}
