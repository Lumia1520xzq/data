package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareConvertDayDao;
import com.wf.data.dao.datarepo.entity.DatawareConvertDay;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareConvertDayService extends CrudService<DatawareConvertDayDao, DatawareConvertDay> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelInfoAll getConvertByDate(Map<String, Object> map) {
        return dao.getConvertByDate(map);
    }

    public DatawareFinalChannelInfoAll getRechargeByDate(Map<String, Object> map) {
        return dao.getRechargeByDate(map);
    }

    public List<Long> getUserIdByDate(Map<String, Object> map) {
        return dao.getUserIdByDate(map);
    }

    public List<Long> getUserIdList(Map<String, Object> map) {
        return dao.getUserIdList(map);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public Double getHistoryConvertByDate(Map<String, Object> params) {
        return dao.getHistoryConvertByDate(params);
    }

    public Double getRegisteredConvertByDate(Map<String, Object> params) {
        return dao.getRegisteredConvertByDate(params);
    }

    public Double getRechargeSumByDate(Map<String, Object> map) {
        return dao.getRechargeSumByDate(map);
    }

    public List<Long> getRechargeUserIdsByDate(Map<String, Object> map) {
        return dao.getRechargeUserIdsByDate(map);
    }

    public List<Long> getPayUserIdListByDate(Map<String, Object> payParams) {
        return dao.getPayUserIdListByDate(payParams);
    }

    public DatawareConvertDay getInfoByUser(Map<String, Object> baseParam) {
        return dao.getInfoByUser(baseParam);
    }

    public String getFirstRechargeTime(Map<String, Object> baseParam) {
        return dao.getFirstRechargeTime(baseParam);
    }

    public String getLastRechargeTime(Map<String, Object> baseParam) {
        return dao.getLastRechargeTime(baseParam);
    }

    public List<Long> getTotalAmountUserId(Map<String, Object> payParams) {
        return dao.getTotalAmountUserId(payParams);
    }

    public List<Long> getUsersByRechargeType(Map<String, Object> convertParams) {
        return dao.getUsersByRechargeType(convertParams);
    }

    public Long getNewRechargeCount(Map<String, Object> map) {
        return dao.getNewRechargeCount(map);
    }

    public Double getNewPayCovCycle(Map<String, Object> map) {
        return dao.getNewPayCovCycle(map);
    }

    public Double getRechargeRepRate(Map<String, Object> map) {
        return dao.getRechargeRepRate(map);
    }
}