package com.wf.data.service.landlords;

import com.wf.core.service.CrudService;
import com.wf.data.dao.landlords.LandlordsUserAmountLogDao;
import com.wf.data.dao.landlords.entity.LandlordsUserAmountLog;
import com.wf.data.dto.LandlordsDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 斗地主流水
 */
@Service
public class LandlordsUserAmountLogService extends CrudService<LandlordsUserAmountLogDao, LandlordsUserAmountLog> {

    public LandlordsDto getBettingInfo(Map<String, Object> map) {
        return dao.getBettingInfo(map);
    }
    /**
     * 获取桌费，减去返回桌费
     * @param map
     * @return
     */
    public Double getTableAmount(Map<String, Object> map) {
        return dao.getTableAmount(map);
    }
    /**
     * 获取道具金额
     * @param map
     * @return
     */
    public Double getToolsAmount(Map<String, Object> map) {
        return dao.getToolsAmount(map);
    }
    /**
     * 获取局数
     * @param map
     * @return
     */
    public Integer getGameTimes(Map<String, Object> map) {
        return dao.getGameTimes(map);
    }

    public List<LandlordsDto> getLandlordList(Map<String, Object> map) {
        return dao.getLandlordList(map);
    }
}
