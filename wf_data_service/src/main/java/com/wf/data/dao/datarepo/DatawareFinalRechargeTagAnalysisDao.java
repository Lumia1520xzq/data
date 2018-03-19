package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;

@MyBatisDao(tableName = "dataware_final_recharge_tag_analysis")
public interface DatawareFinalRechargeTagAnalysisDao extends CrudDao<DatawareFinalRechargeTagAnalysis> {

}
