package com.wf.data.service.platform;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
import com.wf.data.dao.platform.PlatUserCheckLotteryLogDao;
import com.wf.data.dao.platform.entity.PlatUserCheckLotteryLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 *
 */
@Service
public class PlatUserCheckLotteryLogService extends CrudService<PlatUserCheckLotteryLogDao, PlatUserCheckLotteryLog> {

    public List<DatawareUserSignDay> findSignList(Map<String, Object> params){
        return dao.findSignList(params);
    }

}