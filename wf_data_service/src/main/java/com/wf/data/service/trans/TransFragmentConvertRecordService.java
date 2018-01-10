package com.wf.data.service.trans;


import com.wf.core.service.CrudService;
import com.wf.data.dao.trans.TransFragmentConvertRecordDao;
import com.wf.data.dao.trans.entity.TransFragmentConvertRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: lcs
 * @date: 2017/12/14
 */
@Service
public class TransFragmentConvertRecordService extends CrudService<TransFragmentConvertRecordDao, TransFragmentConvertRecord> {

    public Double getFragmentCostByDate(Map<String, Object> map) {
        return dao.getFragmentCostByDate(map);
    }
}
