package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareUserSignDay;

import java.util.Map;

@MyBatisDao(tableName = "dataware_user_sign_day")
public interface DatawareUserSignDayDao extends CrudDao<DatawareUserSignDay> {

    long getCountByTime(Map<String,Object> map);
}
