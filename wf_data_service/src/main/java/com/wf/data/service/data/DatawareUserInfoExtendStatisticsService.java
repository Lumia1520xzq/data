package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareUserInfoExtendStatisticsDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendStatistics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareUserInfoExtendStatisticsService extends CrudService<DatawareUserInfoExtendStatisticsDao, DatawareUserInfoExtendStatistics> {

    public void deleteAll() {
        dao.deleteAll();
    }

    public DatawareUserInfoExtendStatistics getByUserId(Map<String, Object> baseParam) {
        return dao.getByUserId(baseParam);
    }

    public List<DatawareUserInfoExtendStatistics> getProfitByUserId(Map<String, Object> baseParam) {
        return dao.getProfitByUserId(baseParam);
    }
}