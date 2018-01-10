package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareThirdBettingRecordDao;
import com.wf.data.dao.data.entity.DatawareThirdBettingRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lcs
 */
@Service
public class DatawareThirdBettingRecordService extends CrudService<DatawareThirdBettingRecordDao, DatawareThirdBettingRecord> {

    @Async
    public void saveRecord(DatawareThirdBettingRecord entity) {

        DatawareThirdBettingRecord record = dao.findBettingRecord(entity);
        if (null != record) {
            record.setUserCount(entity.getUserCount());
            record.setBettingCount(entity.getBettingCount());
            record.setBettingAmount(entity.getBettingAmount());
            record.setResultAmount(entity.getResultAmount());
            record.setResultRate(entity.getResultRate());
            record.setBettingArpu(entity.getBettingArpu());
            record.setBettingAsp(entity.getBettingAsp());
            super.save(record);
        } else {
            super.save(entity);
        }

    }
}