package com.wf.data.controller.api;

import javax.validation.Valid;

import com.wf.data.common.DataBaseController;
import com.wf.data.controller.request.UicBuryingPointRequest;
import com.wf.data.dao.entity.mycat.UicBuryingPoint;
import com.wf.data.service.MycatUicBuryingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zy 2016-12-16
 */
@RequestMapping("/api/burying")
@RestController
public class BuryingController extends DataBaseController {
    @Autowired
    private MycatUicBuryingPointService uicBuryingPointService;
    /**
     * 设置埋点
     * GameTypeContents
     * BuryingPointContents
     * @param request
     * @return
     */
    @RequestMapping("point")
    public Object point(@Valid@RequestBody UicBuryingPointRequest request) {
        Long userId = null;

        try {
            userId = getUserId();
        } catch (Exception e) {
        }

        UicBuryingPoint uicBuryingPoint = new UicBuryingPoint();
        uicBuryingPoint.setUserId(userId);
        uicBuryingPoint.setGameType(request.getGameType());
        uicBuryingPoint.setBuryingType(request.getBuryingType());
        uicBuryingPoint.setChannelId(getChannelId());
        uicBuryingPointService.save(uicBuryingPoint);
        return SUCCESS;
    }

    /**
     * 设置埋点并返回改类型埋点是否首次
     * 用于判断用户首次进入游戏
     * GameTypeContents
     * BuryingPointContents
     * @param request
     * @return
     */
    @RequestMapping("buryingPoint/firstLoad")
    public Object buryingPointReturnStatus(@Valid@RequestBody UicBuryingPointRequest request) {

        Long userId = null;
        UicBuryingPoint point = null;
        try {
            userId = getUserId();
            point = uicBuryingPointService.getByGameTypeAndBuryingType(request.getGameType(), request.getBuryingType(), userId);
        } catch (Exception e) {
            logger.error("buryingPoint/firstLoad获取游戏埋点数据失败,{}",e);
        }


        UicBuryingPoint uicBuryingPoint = new UicBuryingPoint();
        uicBuryingPoint.setUserId(userId);
        uicBuryingPoint.setGameType(request.getGameType());
        uicBuryingPoint.setBuryingType(request.getBuryingType());
        uicBuryingPoint.setChannelId(getChannelId());
        uicBuryingPointService.save(uicBuryingPoint);
        if(point != null){
            return data(false);
        }else{
            return data(true);
        }
    }


}
