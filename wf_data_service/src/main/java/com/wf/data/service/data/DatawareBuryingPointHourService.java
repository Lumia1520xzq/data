package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareBuryingPointHourDao;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointDay;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointHour;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareBuryingPointHourService extends CrudService<DatawareBuryingPointHourDao, DatawareBuryingPointHour> {

    public List<DatawareBuryingPointDay> findBuryingList(Map<String, Object> map) {
        return dao.findBuryingList(map);
    }

    public void updateUserGroup(DatawareBuryingPointHour pointDay) {
        dao.updateUserGroup(pointDay);
    }

    public List<Long> findUserId(DatawareBuryingPointHour pointDay) {
        return dao.findUserId(pointDay);
    }

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public Integer getDauByDateAndHour(Map<String, Object> params) {
        return dao.getDauByDateAndHour(params);
    }

    public Long getDauByTime(Map<String, Object> params) {
        return dao.getDauByTime(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}