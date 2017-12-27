
package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareConvertHour;
import com.wf.data.dao.trans.TransConvertDao;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.dao.trans.entity.TransConvert;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransConvertService extends CrudService<TransConvertDao, TransConvert> {

	public TransConvert getConvertByOrderSn(String orderSn) {
		return dao.getConvertByOrderSn(orderSn);
	}


	public List<TransConvert> findJSNotSuccessConvert() {
		Date date = DateUtils.addHours(new Date(),-24);
		return dao.findJSNotSuccessConvert(date);
	}

	public Double findSumRechargeByTime(Map<String,Object> params){
		return dao.findSumRechargeByTime(params);
	}
	
	public Double getRechargeNumByUserIdAndDate(Map<String,Object> params){
	    return dao.getRechargeNumByUserIdAndDate(params);
	}
	
	public Date getMinRechargeTimeByDate(Map<String,Object> params){
		return dao.getMinRechargeTimeByDate(params);
	}
	
	public List<Long> findRechargeUserIdsTillTime(String date){
		return dao.findRechargeUserIdsTillTime(date);
	}
	
	public List<Long> findUserIdsBySumRecharge(Map<String,Object> params){
		return dao.findUserIdsBySumRecharge(params);
	}
	
	public List<Long> getRechargeUserIdsByDay(Map<String,Object> params){
		return dao.getRechargeUserIdsByDay(params);
	}
	public Long getRechargeCountByDay(Map<String,Object> params) {
		return dao.getRechargeCountByDay(params);
	}
	
	public List<TransChangeNote> getRechargeSumByUserIdAndDate(Map<String,Object> params) {
		return dao.getRechargeSumByUserIdAndDate(params);
	}


	public Double findUserSumRecharge(Long userId){
		return dao.findUserSumRecharge(userId);
	}
	
	public double getIncomeAmount(Map<String,Object> params) {
		return dao.getIncomeAmount(params);
	}

	public List<DatawareConvertHour> findConvertList(Map<String,Object> map){
		return dao.findConvertList(map);
	}

}
