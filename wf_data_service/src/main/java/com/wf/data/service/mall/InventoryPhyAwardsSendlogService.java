package com.wf.data.service.mall;

import com.wf.core.service.CrudService;
import com.wf.data.dao.mall.InventoryPhyAwardsSendlogDao;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InventoryPhyAwardsSendlogService extends CrudService<InventoryPhyAwardsSendlogDao, InventoryPhyAwardsSendlog> {

    public Double getRmbAmountByChannel(Map<String, Object> map) {
        return dao.getRmbAmountByChannel(map);
    }

    public Double getRmbAmountByUserId(Map<String, Object> userParam) {
        return dao.getRmbAmountByUserId(userParam);
    }

    public Long getActivityUsersCount(Map<String, Object> map) {
        return dao.getActivityUsersCount(map);
    }

    public Double getRmbAmountByActivity(Map<String,Object> params) {
        return dao.getRmbAmountByActivity(params);
    }

    public List<Map<String, Object>> getRmbAmountByChannelAndActivity(Map<String, Object> map) {
        return dao.getRmbAmountByChannelAndActivity(map);
    }

    public List<Map<String, Object>> getActivityUsersCountByChannel(Map<String, Object> map) {
        return dao.getActivityUsersCountByChannel(map);
    }

    public List<Map<String, Object>> getRmbAmountsByActivity(Map<String, Object> params) {
        return dao.getRmbAmountsByActivity(params);
    }

    public List<Map<String, Object>> getChannelUsersCountByActivity(Map<String, Object> params) {
        return dao.getChannelUsersCountByActivity(params);
    }

    public Long getMaxCostUserId(Map<String,Object> map) {
        return dao.getMaxCostUserId(map);
    }

    public List<Map<String, Object>> getUserCostPerDay(Map<String, Object> params) {
        return dao.getUserCostPerDay(params);
    }

}
