package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalRegisteredRetentionDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRegisteredRetention;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRegisteredRetentionService extends CrudService<DatawareFinalRegisteredRetentionDao, DatawareFinalRegisteredRetention> {
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareFinalRegisteredRetention getRetentionByDate(Map<String, Object> map) {
        return dao.getRetentionByDate(map);
    }
}