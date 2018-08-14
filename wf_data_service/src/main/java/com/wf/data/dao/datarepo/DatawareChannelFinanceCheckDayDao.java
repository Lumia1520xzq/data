package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareChannelFinanceCheckDay;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_channel_finance_check_day")
public interface DatawareChannelFinanceCheckDayDao extends CrudDao<DatawareChannelFinanceCheckDay> {

    List<DatawareChannelFinanceCheckDay> getLeafDailyList(Map<String, Object> map);
}
