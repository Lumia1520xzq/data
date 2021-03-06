package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalChannelConversionDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelConversion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelConversionService extends CrudService<DatawareFinalChannelConversionDao, DatawareFinalChannelConversion> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public List<DatawareFinalChannelConversion> getByChannelAndDate(Map<String, Object> params) {
        return dao.getByChannelAndDate(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}