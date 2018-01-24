package com.wf.data.service.platform;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.DatawareUserSignDay;
import com.wf.data.dao.platform.PlatSignedUserDao;
import com.wf.data.dao.platform.entity.PlatSignedUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class PlatSignedUserService extends CrudService<PlatSignedUserDao, PlatSignedUser> {

    public List<DatawareUserSignDay> findListFromSignedUser(Map<String, Object> params) {
        return dao.findListFromSignedUser(params);
    }

}