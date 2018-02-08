package com.wf.data.service.data;


import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareGameBettingInfoHourDao;
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DatawareGameBettingInfoHourService extends CrudService<DatawareGameBettingInfoHourDao,DatawareGameBettingInfoHour>{

    public List<DatawareGameBettingInfoHour> getByDateAndHour(Map<String,Object> params){
        return dao.getByDateAndHour(params);
    }
}

