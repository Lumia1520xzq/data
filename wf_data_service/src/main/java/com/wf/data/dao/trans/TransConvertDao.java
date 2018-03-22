package com.wf.data.dao.trans;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareConvertHour;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.dao.trans.entity.TransConvert;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "trans_convert")
public interface TransConvertDao extends CrudDao<TransConvert> {

	TransConvert getConvertByOrderSn(String orderSn);

	List<TransConvert> findJSNotSuccessConvert(Date date);
	
	Double findSumRechargeByTime(Map<String, Object> params);

	Double findUserSumRecharge(@Param("userId") Long userId);

	double getIncomeAmount(Map<String, Object> params);

	List<TransConvert> getConvertUserId(); //获取充值用户ID和首次充值时间

	Double getRechargeNumByUserIdAndDate(Map<String, Object> params);

	Date getMinRechargeTimeByDate(Map<String, Object> params);

	List<Long> findRechargeUserIdsTillTime(String date);

	List<Long> findUserIdsBySumRecharge(Map<String, Object> params);

	List<Long> getRechargeUserIdsByDay(Map<String, Object> params);

	Long getRechargeCountByDay(Map<String, Object> params);

	List<TransChangeNote> getRechargeSumByUserIdAndDate(Map<String, Object> params);

	List<DatawareConvertHour> findConvertList(Map<String,Object> map);

	Double sumDataByConds(Map<String, Object> dataParam);

	Long getCreateOrderUserId(Map<String, Object> dataParam);
}
