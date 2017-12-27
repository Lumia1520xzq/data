package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareBuryingPointDayDao;
import com.wf.data.dao.data.entity.DatawareBuryingPointDay;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareBuryingPointDayService extends CrudService<DatawareBuryingPointDayDao, DatawareBuryingPointDay> {

    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }
}