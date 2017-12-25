package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.ReportChangeNoteDao;
import com.wf.data.dao.data.entity.DatawareBettingLogHour;
import com.wf.data.dao.data.entity.ReportChangeNote;
import com.wf.data.dao.data.entity.ReportGameInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportChangeNoteService extends CrudService<ReportChangeNoteDao, ReportChangeNote> {
	
	public Long getCathecticNum(Map<String,Object> params) {
		return dao.getCathecticNum(params);
	}
	
	public ReportGameInfo findCathecticListByDate(Map<String,Object> params) {
		return dao.findCathecticListByDate(params);
	}
	
	public Integer getCathecticUserNum(Map<String,Object> params) {
	    return dao.getCathecticUserNum(params);
	}
	
	public List<Long> findBettingUsersByDate(Map<String,Object> params){
		return dao.findBettingUsersByDate(params);
	}
	
	public Long getCathecticProfit(Map<String,Object> params) {
		return dao.getCathecticProfit(params);
	}
	
	public Integer getCathecticGames(Map<String,Object> params) {
		return dao.getCathecticGames(params);
	}


	public List<DatawareBettingLogHour> getGameBettingRecord(Map<String, Object> params){
		return dao.getGameBettingRecord(params);
	}

}
