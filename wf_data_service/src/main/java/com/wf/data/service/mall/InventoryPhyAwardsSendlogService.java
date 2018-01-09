package com.wf.data.service.mall;

import com.wf.core.service.CrudService;
import com.wf.data.dao.mall.InventoryPhyAwardsSendlogDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryPhyAwardsSendlogService extends CrudService<InventoryPhyAwardsSendlogDao, InventoryPhyAwardsSendlog> {

    public Double getRmbAmountByChannel(Map<String, Object> map) {
        return dao.getRmbAmountByChannel(map);
    }

}
