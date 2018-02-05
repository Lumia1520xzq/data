package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareBuryingPointHourDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;
import com.wf.data.dao.data.entity.DatawareBuryingPointHour;
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

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> findUserId(DatawareBuryingPointHour pointDay) {
        return dao.findUserId(pointDay);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Integer getDauByDateAndHour(Map<String, Object> params) {
        return dao.getDauByDateAndHour(params);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Long getDauByTime(Map<String, Object> params) {
        return dao.getDauByTime(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}