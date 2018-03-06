package com.wf.data.dao.mycattrans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mycattrans.entity.TransDeal;

@MyBatisDao(tableName = "trans_deal")
public interface TransDealDao extends CrudDao<TransDeal> {

}
