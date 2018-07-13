package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareThirdBettingRecordDao;
import com.wf.data.dao.datarepo.entity.DatawareThirdBettingRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareThirdBettingRecordService extends CrudService<DatawareThirdBettingRecordDao, DatawareThirdBettingRecord> {

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
            record.setDau(entity.getDau());
            record.setNewUserSecondRetention(entity.getNewUserSecondRetention());
            record.setAllSecondRetention(entity.getAllSecondRetention());
            record.setNewUserThreeDayRetention(entity.getNewUserThreeDayRetention());
            record.setThreeDayRetention(entity.getThreeDayRetention());

            super.save(record);
        } else {
            super.save(entity);
        }

    }

    public DatawareThirdBettingRecord sumDataByConds(Map<String, Object> dataParam) {
        return dao.sumDataByConds(dataParam);
    }
}