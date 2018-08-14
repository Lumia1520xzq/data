package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareChannelFinanceCheckDayDao;
import com.wf.data.dao.datarepo.entity.DatawareChannelFinanceCheckDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatawareChannelFinanceCheckDayService extends CrudService<DatawareChannelFinanceCheckDayDao, DatawareChannelFinanceCheckDay> {

    public List<DatawareChannelFinanceCheckDay> getLeafDailyList(Map<String, Object> map) {
        return dao.getLeafDailyList(map);
    }
}
