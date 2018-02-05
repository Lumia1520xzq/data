package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalChannelRetentionDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelRetentionService extends CrudService<DatawareFinalChannelRetentionDao, DatawareFinalChannelRetention> {
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareFinalChannelRetention findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    public long deleteByDate(Map<String, Object> map) {
        return dao.deleteByDate(map);
    }

}