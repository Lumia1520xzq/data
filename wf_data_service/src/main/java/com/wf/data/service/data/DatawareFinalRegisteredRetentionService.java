package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalRegisteredRetentionDao;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredRetention;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRegisteredRetentionService extends CrudService<DatawareFinalRegisteredRetentionDao, DatawareFinalRegisteredRetention> {

    public DatawareFinalRegisteredRetention getRetentionByDate(Map<String, Object> map) {
        return dao.getRetentionByDate(map);
    }
}