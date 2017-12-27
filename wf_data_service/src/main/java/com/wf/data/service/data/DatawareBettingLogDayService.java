package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareBettingLogDayDao;
import com.wf.data.dao.data.entity.DatawareBettingLogDay;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author jijie.chen
 *
 */
@Service
public class DatawareBettingLogDayService extends CrudService<DatawareBettingLogDayDao, DatawareBettingLogDay> {

    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }

}