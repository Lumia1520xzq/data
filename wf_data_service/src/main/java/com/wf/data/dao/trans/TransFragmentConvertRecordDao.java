package com.wf.data.dao.trans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.trans.entity.TransFragmentConvertRecord;

import java.util.Map;

@MyBatisDao(tableName = "trans_fragment_convert_record")
public interface TransFragmentConvertRecordDao extends CrudDao<TransFragmentConvertRecord> {

    Double getFragmentCostByDate(Map<String,Object> map);

}
