package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareBuryingPointDayDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareBuryingPointDayService extends CrudService<DatawareBuryingPointDayDao, DatawareBuryingPointDay> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public Integer getGameDau(Map<String, Object> map) {
        return dao.getGameDau(map);
    }

    public Long getDauByChannel(Map<String, Object> map) {
        return dao.getDauByChannel(map);
    }

    public List<Long> getUserIdListByChannel(Map<String, Object> map) {
        return dao.getUserIdListByChannel(map);
    }

    public List<Long> getHistoryDauIds(Map<String, Object> map) {
        return dao.getHistoryDauIds(map);
    }

    public List<Long> getGameDauIds(Map<String, Object> map) {
        return dao.getGameDauIds(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}