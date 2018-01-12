package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DatawareFinalChannelCostDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelCost;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalChannelCostService extends CrudService<DatawareFinalChannelCostDao, DatawareFinalChannelCost> {


    public long getCountByTime(Map<String, Object> map) {
        return dao.getCountByTime(map);
    }

    public DatawareFinalChannelCost findByDate(Map<String,Object> params){
        return dao.findByDate(params);
    }

}