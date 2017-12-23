package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareBettingLogHourDao;
import com.wf.data.dao.data.entity.DatawareBettingLogDay;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import org.springframework.stereotype.Service;

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

    public List<DatawareBettingLogDay> findBettingList(Map<String, Object> params) {
        return dao.findBettingList(params);
    }
}