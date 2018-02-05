package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
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

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> findUserId(DatawareBettingLogHour log) {
        return dao.findUserId(log);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> findUserIdByTime(DatawareBettingLogHour log) {
        return dao.findUserIdByTime(log);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<DatawareBettingLogDay> findBettingList(Map<String, Object> params) {
        return dao.findBettingList(params);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareBettingLogHour getSumByDateAndHour(Map<String,Object> params){
        return dao.getSumByDateAndHour(params);
    }
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public DatawareBettingLogHour getBettingByDate(Map<String,Object> params){
        return dao.getBettingByDate(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }
}