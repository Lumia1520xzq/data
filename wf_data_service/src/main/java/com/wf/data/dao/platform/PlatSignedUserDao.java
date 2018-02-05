package com.wf.data.dao.platform;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
import com.wf.data.dao.platform.entity.PlatSignedUser;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "plat_signed_user")
public interface PlatSignedUserDao extends CrudDao<PlatSignedUser> {

    List<DatawareUserSignDay> findListFromSignedUser(Map<String,Object> map);

    int findSignedUserCount(Map<String, Object> map);
}
