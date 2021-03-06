package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareFinalRegisteredArpuDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRegisteredArpu;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRegisteredArpuService extends CrudService<DatawareFinalRegisteredArpuDao, DatawareFinalRegisteredArpu> {
    public DatawareFinalRegisteredArpu getArpuByDate(Map<String, Object> map) {
        return dao.getArpuByDate(map);
    }

}