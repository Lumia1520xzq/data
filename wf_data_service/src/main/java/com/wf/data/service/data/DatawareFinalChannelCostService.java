package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalChannelCostDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelCostService extends CrudService<DatawareFinalChannelCostDao, DatawareFinalChannelCost> {

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareFinalChannelCost findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Double findMonthCost(Map<String, Object> params) {
        return dao.findMonthCost(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}