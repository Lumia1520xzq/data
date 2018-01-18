package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareThirdBettingRecord;

@MyBatisDao(tableName = "dataware_third_betting_record")
public interface DatawareThirdBettingRecordDao extends CrudDao<DatawareThirdBettingRecord> {

    DatawareThirdBettingRecord findBettingRecord(DatawareThirdBettingRecord record);

}