package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_entrance_analysis")
public interface DatawareFinalEntranceAnalysisDao extends CrudDao<DatawareFinalEntranceAnalysis> {

    List<DatawareFinalEntranceAnalysis> getEntranceAnalysisByDate(Map<String, Object> eaparams);

    void deleteByDate(Map<String, Object> params);
}
