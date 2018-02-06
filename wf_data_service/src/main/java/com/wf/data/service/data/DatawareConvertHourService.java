package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareConvertHourDao;
import com.wf.data.dao.datarepo.entity.DatawareConvertDay;
import com.wf.data.dao.datarepo.entity.DatawareConvertHour;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoHour;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareConvertHourService extends CrudService<DatawareConvertHourDao, DatawareConvertHour> {
    public List<DatawareConvertDay> findConvertList(Map<String, Object> map) {
        return dao.findConvertList(map);
    }

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelInfoHour findRechargeByDate(Map<String, Object> map) {
        return dao.findRechargeByDate(map);
    }

    public DatawareFinalChannelInfoHour findRechargeByTime(Map<String, Object> map) {
        return dao.findRechargeByTime(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public Long findrechargeCountByDate(Map<String, Object> map) {
        return dao.findrechargeCountByDate(map);
    }
}