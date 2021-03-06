package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalChannelInfoAllDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dto.MonthlyDataDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelInfoAllService extends CrudService<DatawareFinalChannelInfoAllDao, DatawareFinalChannelInfoAll> {
    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public List<DatawareFinalChannelInfoAll> getListByChannelAndDate(Map<String, Object> params) {
        return dao.getListByChannelAndDate(params);
    }

    public DatawareFinalChannelInfoAll findByDate(Map<String, Object> params) {
        return dao.findByDate(params);
    }

    public int deleteByDate(Map<String, Object> params) {
        return dao.deleteByDate(params);
    }

    public List<MonthlyDataDto> findMonthSumData(Map<String, Object> params) {
        return dao.findMonthSumData(params);
    }

    public int updateUserLtv(Map<String, Object> params) {
        return dao.updateUserLtv(params);
    }

    public DatawareFinalChannelInfoAll getInfoByChannel(Map<String, Object> params) {
        return dao.getInfoByChannel(params);
    }

}