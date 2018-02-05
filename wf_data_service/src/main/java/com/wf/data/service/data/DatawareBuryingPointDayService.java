package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
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
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Integer getGameDau(Map<String, Object> map) {
        return dao.getGameDau(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Long getDauByChannel(Map<String, Object> map) {
        return dao.getDauByChannel(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getUserIdListByChannel(Map<String, Object> map) {
        return dao.getUserIdListByChannel(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getHistoryDauIds(Map<String, Object> map) {
        return dao.getHistoryDauIds(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getGameDauIds(Map<String, Object> map) {
        return dao.getGameDauIds(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}