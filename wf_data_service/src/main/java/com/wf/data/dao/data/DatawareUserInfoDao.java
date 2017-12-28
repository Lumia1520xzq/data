package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareUserInfo;

import java.util.Map;

@MyBatisDao(tableName = "dataware_user_info")
public interface DatawareUserInfoDao extends CrudDao<DatawareUserInfo> {

    long getCountByTime(Map<String,Object> map);
}
