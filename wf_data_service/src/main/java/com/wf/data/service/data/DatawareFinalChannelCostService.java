package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalChannelCostDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelCostService extends CrudService<DatawareFinalChannelCostDao, DatawareFinalChannelCost> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelCost findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    public Double findMonthCost(Map<String, Object> params) {
        return dao.findMonthCost(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public List<DatawareFinalChannelCost> getCostTop5Channel(Map<String, Object> params) {
        return dao.getCostTop5Channel(params);
    }

    public List<String> findDateList(Map<String, Object> map) {
        return dao.findDateList(map);
    }

    public List<DatawareFinalChannelCost> getCostInfo(Map<String, Object> map) {
        return dao.getCostInfo(map);
    }
}