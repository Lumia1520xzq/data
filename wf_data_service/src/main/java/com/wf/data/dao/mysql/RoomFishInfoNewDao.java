package com.wf.data.dao.mysql;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.ReportFishBettingInfo;
import com.wf.data.dao.entity.mysql.ReportGameInfo;
import com.wf.data.dao.entity.mysql.RoomFishInfoNew;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "room_fish_info_new")
public interface RoomFishInfoNewDao extends CrudDao<RoomFishInfoNew> {
	//投注信息
	public ReportGameInfo findBettingInfoByDate(Map<String, Object> params);

	public List<Long> findBettingUsersByDate(Map<String, Object> params);

	List<ReportFishBettingInfo> findFishDateByDate(Map<String, Object> map);
}
