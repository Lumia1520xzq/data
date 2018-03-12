package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_user_info")
public interface DatawareUserInfoDao extends CrudDao<DatawareUserInfo> {

    long getCountByTime(Map<String, Object> map);

    long deleteByDate(Map<String, Object> map);

    List<Long> getNewUserByDate(Map<String, Object> map);

    List<Long> getNewUserByTime(Map<String, Object> map);

    Long getHistoryUserByDate(Map<String, Object> map);

    int updateUserGroup(Map<String, Object> map);

    List<DatawareUserInfo> getBaseUserInfoLimit(Map<String, Object> params);

    DatawareUserInfo getByUserId(Long userId);
}
