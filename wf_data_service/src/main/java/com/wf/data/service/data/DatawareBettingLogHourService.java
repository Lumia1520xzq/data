package com.wf.data.service.data;


import org.springframework.stereotype.Service;
import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareBettingLogHourDao;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;

import java.util.List;
import java.util.Map;

/**
 * @author jijie.chen
 */
@Service
public class DatawareBettingLogHourService extends CrudService<DatawareBettingLogHourDao, DatawareBettingLogHour> {

    public void updateUserGroup(DatawareBettingLogHour logHour) {
        dao.updateUserGroup(logHour);
    }

    public List<Long> findUserId(DatawareBettingLogHour log) {
        return dao.findUserId(log);
    }

    public List<Long> findUserIdByTime(DatawareBettingLogHour log) {
        return dao.findUserIdByTime(log);
    }

    public List<DatawareBettingLogDay> findBettingList(Map<String, Object> params) {
        return dao.findBettingList(params);
    }

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareBettingLogHour getSumByDateAndHour(Map<String, Object> params) {
        return dao.getSumByDateAndHour(params);
    }

    public DatawareBettingLogHour getBettingByDate(Map<String, Object> params) {
        return dao.getBettingByDate(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public DatawareBettingLogHour getNewUserBettingInfoByDate(Map<String, Object> params) {
        return dao.getNewUserBettingInfoByDate(params);
    }
}