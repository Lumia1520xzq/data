package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendBase;

@MyBatisDao(tableName = "dataware_user_info_extend_base")
public interface DatawareUserInfoExtendBaseDao extends CrudDao<DatawareUserInfoExtendBase> {
    long getAllCount();

    void deleteAll();

    String getEarliestRegisteDate();

    void updateUserGroupAndNewFlag(String monthStr);

    DatawareUserInfoExtendBase getByUserId(Long userId);
}
