package com.wf.data.service.data;

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

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public List<DatawareFinalChannelInfoHour> getByDateAndHour(Map<String, Object> map){
        return dao.getByDateAndHour(map);
    }

    public List<DatawareFinalChannelInfoHour> getSumDataByDateAndHour(Map<String, Object> map){
        return dao.getSumDataByDateAndHour(map);
    }
}