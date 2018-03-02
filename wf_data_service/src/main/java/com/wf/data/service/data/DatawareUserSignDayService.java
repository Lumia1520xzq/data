package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareUserSignDayDao;
import com.wf.data.dao.datarepo.entity.DatawareUserSignDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareUserSignDayService extends CrudService<DatawareUserSignDayDao, DatawareUserSignDay> {
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public Long getSignedCountByTime(Map<String, Object> params) {
        return dao.getSignedCountByTime(params);
    }

    public int updateUserGroup(Map<String, Object> map) {
        return dao.updateUserGroup(map);
    }

    public List<Long> getSignedUserIds(Map<String, Object> signParams) {
        return dao.getSignedUserIds(signParams);
    }
}