package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareConvertDayDao;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareConvertDayService extends CrudService<DatawareConvertDayDao, DatawareConvertDay> {

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareFinalChannelInfoAll getConvertByDate(Map<String, Object> map) {
        return dao.getConvertByDate(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getUserIdByDate(Map<String, Object> map) {
        return dao.getUserIdByDate(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Double getHistoryConvertByDate(Map<String, Object> params) {
        return dao.getHistoryConvertByDate(params);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Double getRegisteredConvertByDate(Map<String, Object> params) {
        return dao.getRegisteredConvertByDate(params);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Double getRechargeSumByDate(Map<String, Object> map) {
        return dao.getRechargeSumByDate(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getRechargeUserIdsByDate(Map<String, Object> map) {
        return dao.getRechargeUserIdsByDate(map);
    }
}