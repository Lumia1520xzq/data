package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareUserInfoDao;
import com.wf.data.dao.data.entity.DatawareUserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareUserInfoService extends CrudService<DatawareUserInfoDao, DatawareUserInfo> {

    public long getCountByTime(Map<String,Object> map){
        return dao.getCountByTime(map);
    }
}