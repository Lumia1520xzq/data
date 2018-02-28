package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;

@MyBatisDao(tableName = "dataware_final_entrance_analysis")
public interface DatawareFinalEntranceAnalysisDao extends CrudDao<DatawareFinalEntranceAnalysis> {

}
