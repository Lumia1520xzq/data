package com.wf.data.dao.mycatdata;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mycatdata.entity.BehaviorRecord;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "uic_behavior_record")
public interface BehaviorRecordDao extends CrudDao<BehaviorRecord> {

    /**
     * 获取用户id
     * @param dauParams
     * @return
     */
    List<Long> getUserIdsByEntrance(Map<String, Object> dauParams);
}
