package com.wf.data.service.mall;


import com.wf.core.service.CrudService;
import com.wf.data.dao.mall.MallBizLogDao;
import com.wf.data.dao.mall.entity.MallBizLog;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 购买记录
 */
@Service
public class MallBizLogService extends CrudService<MallBizLogDao, MallBizLog> {

    public Double getSumRecharge(Map<String, Object> dataParam){
    	return dao.getSumRecharge(dataParam);
    }
}
