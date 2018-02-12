package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.taurus.TaurusUserBettingLogDao;
import com.wf.data.dao.taurus.entity.TaurusUserBettingLog;
import com.wf.data.dao.tcard.TcardUserBettingLogDao;
import com.wf.data.dao.tcard.entity.TcardUserBettingLog;
import com.wf.data.dto.TcardDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author JoeH
 */
@Service
public class TaurusUserBettingLogService extends CrudService<TaurusUserBettingLogDao, TaurusUserBettingLog> {

    public Double getTableAmount(Map<String,Object> params){
        return dao.getTableAmount(params);
    }
    public Integer getUserCountByBettingType(Map<String,Object> params){
        return dao.getUserCountByBettingType(params);
    }
    public Integer getTablesByBettingType(Map<String,Object> params){
        return dao.getTablesByBettingType(params);
    }

}