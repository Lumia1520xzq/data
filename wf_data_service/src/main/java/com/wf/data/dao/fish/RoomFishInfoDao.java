package com.wf.data.dao.fish;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.data.entity.ReportGameInfo;
import com.wf.data.dao.fish.entity.RoomFishInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "room_fish_info")
public interface RoomFishInfoDao extends CrudDao<RoomFishInfo> {
    //投注信息
    public ReportGameInfo findBettingInfoByDate(Map<String, Object> params);

    public List<Long> findBettingUsersByDate(Map<String, Object> params);

    List<ReportFishBettingInfo> findFishDateByDate(Map<String, Object> map);
}
