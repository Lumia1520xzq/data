package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.entity.mysql.ReportFishBettingInfo;
import com.wf.data.dao.entity.mysql.RoomFishInfo;
import com.wf.data.dao.mysql.RoomFishInfoDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomFishInfoService extends CrudService<RoomFishInfoDao, RoomFishInfo> {

    public List<ReportFishBettingInfo> findFishDateByDate(Map<String,Object> map){
        return dao.findFishDateByDate(map);
    }
}
