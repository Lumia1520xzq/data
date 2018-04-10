package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalGameInfoDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/4/4
 */
@Service
public class DatawareFinalGameInfoService extends CrudService<DatawareFinalGameInfoDao, DatawareFinalGameInfo> {

    public List<DatawareFinalGameInfo> findInfoByDate(Map<String, Object> map) {
        return dao.findInfoByDate(map);
    }

    public List<DatawareFinalGameInfo> findListByDate(Map<String, Object> map) {
        return dao.findListByDate(map);
    }


    public List<String> findDateList(Map<String, Object> map) {
        return dao.findDateList(map);
    }

    public int getCountByDate(Map<String, Object> param) {
        return dao.getCountByDate(param);
    }

    public void deleteByDate(Map<String, Object> param) {
        dao.deleteByDate(param);
    }

    public DatawareFinalGameInfo getInfoByDateAndGameType(Map<String, Object> param) {
        return dao.getInfoByDateAndGameType(param);
    }
}
