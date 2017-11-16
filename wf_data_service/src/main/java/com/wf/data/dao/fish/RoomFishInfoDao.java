package com.wf.data.dao.fish;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.fish.entity.RoomFishInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "room_fish_info")
public interface RoomFishInfoDao extends CrudDao<RoomFishInfo> {

    List<ReportFishBettingInfo> findFishDateByDate(Map<String, Object> map);
}
