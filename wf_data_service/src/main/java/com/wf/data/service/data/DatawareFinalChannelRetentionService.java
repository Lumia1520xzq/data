package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalChannelRetentionDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelRetention;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelRetentionService extends CrudService<DatawareFinalChannelRetentionDao, DatawareFinalChannelRetention> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelRetention findByDate(Map<String,Object> params) {
        return dao.findByDate(params);
    }

}