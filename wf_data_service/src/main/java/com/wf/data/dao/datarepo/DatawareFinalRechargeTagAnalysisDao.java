package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;

import java.util.Map;

@MyBatisDao(tableName = "dataware_final_recharge_tag_analysis")
public interface DatawareFinalRechargeTagAnalysisDao extends CrudDao<DatawareFinalRechargeTagAnalysis> {

    DatawareFinalRechargeTagAnalysis getTagAnalysisDate(Map<String,Object> map);

    int deleteByDate(Map<String,Object> map);
}
