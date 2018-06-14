package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareTagUserLogDao;
import com.wf.data.dao.datarepo.entity.DatawareTagUserLog;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DatawareTagUserLogService extends CrudService<DatawareTagUserLogDao, DatawareTagUserLog> {

    public int deleteByDate(Map<String, Object> param) {
        return dao.deleteByDate(param);
    }

}
