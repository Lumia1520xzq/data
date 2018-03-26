package com.wf.data.dao.trans;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.trans.entity.PayAgentMerchant;

@MyBatisDao(tableName = "pay_type_merchant")
public interface PayAgentMerchantDao extends CrudDao<PayAgentMerchant> {

}
