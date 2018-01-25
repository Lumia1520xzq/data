package com.wf.data.dao.mall;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.MallBizLog;

import java.util.Map;

@MyBatisDao(tableName = "mall_biz_log")
public interface MallBizLogDao extends CrudDao<MallBizLog> {

    Double getSumRecharge(Map<String, Object> dataParams);

}
