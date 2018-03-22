package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareBettingLogDayDao;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dto.TcardDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareBettingLogDayService extends CrudService<DatawareBettingLogDayDao, DatawareBettingLogDay> {

    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public TcardDto getTcardBettingByday(Map<String, Object> params) {
        return dao.getTcardBettingByday(params);
    }

    public List<Long> getBettingUserIds(Map<String, Object> params) {
        return dao.getBettingUserIds(params);
    }

    public DatawareFinalChannelInfoAll getBettingByDate(Map<String, Object> params) {
        return dao.getBettingByDate(params);
    }

    public List<Long> getBettingUserIdByDate(Map<String, Object> params) {
        return dao.getBettingUserIdByDate(params);
    }


    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public List<Long> getBettingUserIdListByDate(Map<String, Object> bettingParams) {
        return dao.getBettingUserIdListByDate(bettingParams);
    }

    public DatawareBettingLogDay getInfoByUser(Map<String, Object> baseParam) {
        return dao.getInfoByUser(baseParam);
    }

    public Long getBettingCountByUser(Map<String, Object> baseParam) {
        return dao.getBettingCountByUser(baseParam);
    }

    public TcardDto getBettingByUserIdAndGameType(Map<String, Object> params) {
        return dao.getBettingByUserIdAndGameType(params);
    }

    public DatawareBettingLogDay getByUserIdAndGameType(Map<String, Object> params) {
        return dao.getByUserIdAndGameType(params);
    }

}