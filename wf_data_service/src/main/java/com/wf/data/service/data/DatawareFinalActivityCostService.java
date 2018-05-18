package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalActivityCostDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalActivityCost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatawareFinalActivityCostService extends CrudService<DatawareFinalActivityCostDao, DatawareFinalActivityCost> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public int deleteByDate(Map<String, Object> map) {
        return dao.deleteByDate(map);
    }

    public List<DatawareFinalActivityCost> getCostTop5Activity(Map<String, Object> map) {
        return dao.getCostTop5Activity(map);
    }

    public List<DatawareFinalActivityCost> getActivityCostInfo(Map<String, Object> map) {
        return dao.getActivityCostInfo(map);
    }

    public List<String> findDateList(Map<String, Object> map) {
        return dao.findDateList(map);
    }
}
