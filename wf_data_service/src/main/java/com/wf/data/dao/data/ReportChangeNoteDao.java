package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.ReportChangeNote;
import com.wf.data.dao.data.entity.ReportGameInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "report_change_note")
public interface ReportChangeNoteDao extends CrudDao<ReportChangeNote> {
	//投注笔数
	Long getCathecticNum(Map<String, Object> params);

	// 投注人数,投注笔数,投注流水,返奖流水
	ReportGameInfo findCathecticListByDate(Map<String, Object> params);

	//投注用户数
	Integer getCathecticUserNum(Map<String, Object> params);

	List<Long> findBettingUsersByDate(Map<String, Object> params);

	Long getCathecticProfit(Map<String, Object> params);

	Integer getCathecticGames(Map<String, Object> params);
}
