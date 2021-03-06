package com.wf.data.dao.mycattrans;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mycattrans.entity.TransAccount;

import java.util.Map;

@MyBatisDao(tableName = "trans_account")
public interface TransAccountDao extends CrudDao<TransAccount> {
    Double getUseAmountByUserId(Map<String,Object> params);
}
