package com.wf.data.dao.mall;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@MyBatisDao(tableName = "inventory_phy_awards_sendlog")
public interface InventoryPhyAwardsSendlogDao extends CrudDao<InventoryPhyAwardsSendlog> {

    public InventoryPhyAwardsSendlog getByActivityIdBusinessId(@Param("activityId") Long activityId, @Param("businessId") Long businessId);

    Double getRmbAmountByChannel(Map<String,Object> map);

    Double getRmbAmountByUserId(Map<String, Object> userParam);
}
