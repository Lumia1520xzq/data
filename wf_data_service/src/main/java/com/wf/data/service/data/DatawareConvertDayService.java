package com.wf.data.service.data;

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


    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelInfoAll getConvertByDate(Map<String, Object> map) {
        return dao.getConvertByDate(map);
    }

    public List<Long> getUserIdByDate(Map<String, Object> map) {
        return dao.getUserIdByDate(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }


    public Double getHistoryConvertByDate(Map<String, Object> params) {
        return dao.getHistoryConvertByDate(params);
    }
}