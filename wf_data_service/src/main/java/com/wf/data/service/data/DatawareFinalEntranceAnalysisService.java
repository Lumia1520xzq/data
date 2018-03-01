package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalEntranceAnalysisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalEntranceAnalysisService extends CrudService<DatawareFinalEntranceAnalysisDao, DatawareFinalEntranceAnalysis> {

    public List<DatawareFinalEntranceAnalysis> getEntranceAnalysisByDate(Map<String, Object> eaparams) {
        return dao.getEntranceAnalysisByDate(eaparams);
    }

    public void deleteByDate(Map<String, Object> params) {
        dao.deleteByDate(params);
    }
}