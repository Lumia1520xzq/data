package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareUserSignDayDao;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareUserSignDayService extends CrudService<DatawareUserSignDayDao, DatawareUserSignDay> {

    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}