package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.mycattrans.TransDealDao;
import com.wf.data.dao.mycattrans.entity.TransDeal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TransDealService extends CrudService<TransDealDao, TransDeal> {

    public List<DatawareBettingLogHour> getGameBettingRecord(Map<String,Object> params){
        return dao.getGameBettingRecord(params);
    }

}
