package com.wf.data.dao.mall;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsInfo;

@MyBatisDao(tableName = "inventory_phy_awards_info")
public interface InventoryPhyAwardsInfoDao extends CrudDao<InventoryPhyAwardsInfo> {

}
