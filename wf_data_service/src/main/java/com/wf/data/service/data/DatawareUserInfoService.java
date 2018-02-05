package com.wf.data.service.data;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareUserInfoDao;
import com.wf.data.dao.data.entity.DatawareUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareUserInfoService extends CrudService<DatawareUserInfoDao, DatawareUserInfo> {
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public long deleteByDate(Map<String, Object> map) {
        return dao.deleteByDate(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getNewUserByDate(Map<String, Object> map) {
        return dao.getNewUserByDate(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public List<Long> getNewUserByTime(Map<String, Object> map) {
        return dao.getNewUserByTime(map);
    }

    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public Long getHistoryUserByDate(Map<String, Object> map) {
        return dao.getHistoryUserByDate(map);
    }

    public int updateUserGroup(Map<String, Object> map) {
        return dao.updateUserGroup(map);
    }
}