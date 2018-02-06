package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareThirdBettingRecord;

import java.util.Map;

@MyBatisDao(tableName = "dataware_third_betting_record")
public interface DatawareThirdBettingRecordDao extends CrudDao<DatawareThirdBettingRecord> {

    DatawareThirdBettingRecord findBettingRecord(DatawareThirdBettingRecord record);

    DatawareThirdBettingRecord sumDataByConds(Map<String, Object> dataParam);

}
