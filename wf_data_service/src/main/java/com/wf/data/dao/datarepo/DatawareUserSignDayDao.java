package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserSignDay;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_user_sign_day")
public interface DatawareUserSignDayDao extends CrudDao<DatawareUserSignDay> {

    long getCountByTime(Map<String, Object> map);

    int deleteByDate(Map<String, Object> params);

    Long getSignedCountByTime(Map<String, Object> params);

    int updateUserGroup(Map<String, Object> map);

    List<Long> getSignedUserIds(Map<String, Object> signParams);
}
