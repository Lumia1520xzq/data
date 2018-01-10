package com.wf.data.service.mall;


import com.wf.core.service.CrudService;
import com.wf.data.dao.mall.InventoryPhyAwardsInfoDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsInfo;
import org.springframework.stereotype.Service;

@Service
public class InventoryPhyAwardsInfoService extends CrudService<InventoryPhyAwardsInfoDao, InventoryPhyAwardsInfo> {

	@Override
	protected void clearCache(InventoryPhyAwardsInfo entity) {
		cacheHander.delete("");
	}
	
}
