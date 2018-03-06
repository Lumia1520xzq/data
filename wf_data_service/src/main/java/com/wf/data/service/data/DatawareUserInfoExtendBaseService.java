package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareUserInfoExtendBaseDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DatawareUserInfoExtendBaseService extends CrudService<DatawareUserInfoExtendBaseDao, DatawareUserInfoExtendBase> {

    public long getAllCount() {
        return dao.getAllCount();
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}

