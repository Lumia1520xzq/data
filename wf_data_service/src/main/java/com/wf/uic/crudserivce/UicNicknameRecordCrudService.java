package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.dao.entity.mysql.UicNicknameRecord;
import com.wf.uic.dao.mysql.UicNicknameRecordDao;
import org.springframework.stereotype.Service;

/**
 * Created by gufei on 2017/8/3.
 */
@Service
public class UicNicknameRecordCrudService extends CrudService<UicNicknameRecordDao, UicNicknameRecord> {

}
