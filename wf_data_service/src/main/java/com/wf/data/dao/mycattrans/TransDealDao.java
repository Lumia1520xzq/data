package com.wf.data.dao.mycattrans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.mycattrans.entity.TransDeal;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "trans_deal")
public interface TransDealDao extends CrudDao<TransDeal> {
    List<DatawareBettingLogHour> getGameBettingRecord(Map<String,Object> params);
}
