package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.dao.tcard.TcardUserBettingLogDao;
import com.wf.data.dao.tcard.entity.TcardUserBettingLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jijie.chen
 *
 */
@Service
public class TcardUserBettingLogService extends CrudService<TcardUserBettingLogDao, TcardUserBettingLog> {

    public List<DatawareBettingLogHour> getGameBettingRecord(Map<String, Object> params){
        return dao.getGameBettingRecord(params);
    }

    public List<DatawareBettingLogHour> getBettingAndAward(Map<String, Object> params){
        return dao.getBettingAndAward(params);
    }

}