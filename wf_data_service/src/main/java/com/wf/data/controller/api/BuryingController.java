package com.wf.data.controller.api;

import com.wf.core.web.base.BaseController;
import com.wf.data.dao.entity.mycat.UicBuryingPoint;
import com.wf.data.service.MycatUicBuryingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 埋点内部接口
 * Created by chris on 2017/4/1.
 */
@RequestMapping("/ms/uic/burying")
@RestController
public class BuryingController extends BaseController {

    @Autowired
    private MycatUicBuryingPointService mycatUicBuryingPointService;

    /**
     * 保存埋点
     *
     * @param uicBuryingPoint
     */
    @RequestMapping("save")
    public void save(@RequestBody UicBuryingPoint uicBuryingPoint) {
    	mycatUicBuryingPointService.save(uicBuryingPoint);
    }
    
    @RequestMapping(value = "findLastGameLoading/{userId}/{gameType}", method = RequestMethod.GET)
    public UicBuryingPoint findLastGameLoading(@PathVariable Long userId, @PathVariable Integer gameType) {
        return mycatUicBuryingPointService.findLastGameLoading(userId, gameType);
    }
    
    @RequestMapping(value = "getUserLastPlayGame/{userId}/{num}/{channelId}", method = RequestMethod.GET)
    public List<UicBuryingPoint> getUserLastPlayGame(@PathVariable Long userId, @PathVariable Integer num, @PathVariable Long channelId) {
        return mycatUicBuryingPointService.getUserLastPlayGame(userId, num, channelId);
    }
    
    @RequestMapping(value = "getByGameTypeAndBuryingType/{gameType}/{buryingType}/{userId}", method = RequestMethod.GET)
    public UicBuryingPoint getByGameTypeAndBuryingType(@PathVariable Integer gameType, @PathVariable Integer buryingType, @PathVariable Long userId) {
        return mycatUicBuryingPointService.getByGameTypeAndBuryingType(gameType, buryingType, userId);
    }
    
    @RequestMapping(value = "getLastLoginWealTime/{userId}/{buryingType}", method = RequestMethod.GET)
    public Date getLastLoginWealTime(@PathVariable Long userId,@PathVariable Integer buryingType){
    	return mycatUicBuryingPointService.getLastLoginWealTime(userId,buryingType);
    }
    
    
    
}
