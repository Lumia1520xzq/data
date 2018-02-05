package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalChannelInfoHourDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoHour;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelInfoHourService extends CrudService<DatawareFinalChannelInfoHourDao, DatawareFinalChannelInfoHour> {
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public List<DatawareFinalChannelInfoHour> getByDateAndHour(Map<String, Object> map) {
        return dao.getByDateAndHour(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<DatawareFinalChannelInfoHour> getSumDataByDateAndHour(Map<String, Object> map) {
        return dao.getSumDataByDateAndHour(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareFinalChannelInfoHour findDataForPandect(Map<String, Object> map) {
        return dao.findDataForPandect(map);
    }
}