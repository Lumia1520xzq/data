package com.wf.data.dao.platform;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserSignDay;
import com.wf.data.dao.platform.entity.PlatUserCheckLotteryLog;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "plat_user_check_lottery_log")
public interface PlatUserCheckLotteryLogDao extends CrudDao<PlatUserCheckLotteryLog> {

    List<DatawareUserSignDay> findSignList(Map<String,Object> map);
}
