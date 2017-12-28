package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareConvertHourDao;
import com.wf.data.dao.data.entity.DatawareConvertDay;
import com.wf.data.dao.data.entity.DatawareConvertHour;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareConvertHourService extends CrudService<DatawareConvertHourDao, DatawareConvertHour> {

    public List<DatawareConvertDay> findConvertList(Map<String, Object> map) {
        return dao.findConvertList(map);
    }

    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }
}