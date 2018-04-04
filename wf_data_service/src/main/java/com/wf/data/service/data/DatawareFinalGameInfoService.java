package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalGameInfoDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalGameInfoService extends CrudService<DatawareFinalGameInfoDao, DatawareFinalGameInfo> {

    public List<DatawareFinalGameInfo> findInfoByDate(Map<String, Object> map) {
        return dao.findInfoByDate(map);
    }


    public List<String> findDateList(Map<String, Object> map) {
        return dao.findDateList(map);
    }
}