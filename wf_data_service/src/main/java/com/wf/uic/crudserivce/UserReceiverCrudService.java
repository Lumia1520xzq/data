package com.wf.uic.crudserivce;

import com.wf.core.db.DataSource;
import com.wf.core.db.DataSourceContext;
import com.wf.core.service.CrudService;
import com.wf.uic.dao.entity.mysql.UserReceiver;
import com.wf.uic.dao.mysql.UserReceiverDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserReceiverCrudService extends CrudService<UserReceiverDao, UserReceiver> {

    @Transactional(readOnly = true)
    @DataSource(name = DataSourceContext.DATA_SOURCE_READ)
    public UserReceiver getByUserId(Long userId) {
        return dao.getByUserId(userId);
    }
}
