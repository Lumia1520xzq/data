package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalChannelRetentionDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelRetention;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelRetentionService extends CrudService<DatawareFinalChannelRetentionDao, DatawareFinalChannelRetention> {
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelRetention findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    public long deleteByDate(Map<String, Object> map) {
        return dao.deleteByDate(map);
    }

}