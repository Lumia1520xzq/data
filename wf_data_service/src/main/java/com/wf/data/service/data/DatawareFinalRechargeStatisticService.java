package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalRechargeStatisticDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeStatistic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatawareFinalRechargeStatisticService extends CrudService<DatawareFinalRechargeStatisticDao, DatawareFinalRechargeStatistic> {
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public List<DatawareFinalRechargeStatistic> getListByChannelAndDate(Map<String, Object> params) {
        return dao.getListByChannelAndDate(params);
    }

    public DatawareFinalRechargeStatistic findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

}