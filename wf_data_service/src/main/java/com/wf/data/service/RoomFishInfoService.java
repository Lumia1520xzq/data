
package com.wf.data.service;


import com.wf.core.service.CrudService;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.data.entity.ReportGameInfo;
import com.wf.data.dao.mycatfish.entity.RoomFishInfo;
import com.wf.data.dao.mycatfish.RoomFishInfoDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomFishInfoService extends CrudService<RoomFishInfoDao, RoomFishInfo> {

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
