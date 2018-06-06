package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeStatistic;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_recharge_statistic")
public interface DatawareFinalRechargeStatisticDao extends CrudDao<DatawareFinalRechargeStatistic> {

    long getCountByTime(Map<String, Object> map);

    List<DatawareFinalRechargeStatistic> getListByChannelAndDate(Map<String, Object> params);

    DatawareFinalRechargeStatistic findByDate(Map<String, Object> params);

    int deleteByDate(Map<String, Object> params);

}
