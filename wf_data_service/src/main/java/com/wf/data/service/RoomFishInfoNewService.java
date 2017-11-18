
package com.wf.data.service;


import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.data.entity.ReportGameInfo;
import com.wf.data.dao.fish.entity.RoomFishInfoNew;
import com.wf.data.dao.fish.RoomFishInfoNewDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomFishInfoNewService extends CrudService<RoomFishInfoNewDao, RoomFishInfoNew> {

    public ReportGameInfo findBettingInfoByDate(Map<String, Object> params) {
        return dao.findBettingInfoByDate(params);
    }

    public List<Long> findBettingUsersByDate(Map<String, Object> params) {
        return dao.findBettingUsersByDate(params);
    }


    public List<ReportFishBettingInfo> findFishDateByDate(Map<String,Object> map){
        return dao.findFishDateByDate(map);
    }
}
