package com.wf.data.controller.api;

import com.wf.data.common.DataBaseController;
import com.wf.data.controller.request.BuryingPointRequest;
import com.wf.data.dao.mycatdata.entity.BuryingPoint;
import com.wf.data.service.BuryingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * @author zy 2016-12-16
 */
@CrossOrigin(maxAge = 3600)
@RequestMapping("/data/api/burying")
@RestController
public class BuryingController extends DataBaseController {
    @Autowired
    private BuryingPointService dataBuryingPointService;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    /**
     * 设置埋点
     * GameTypeContents
     * BuryingPointContents
     * @param request
     * @return
     */
    @RequestMapping(value = "point", method = RequestMethod.POST)
    public Object point(@Valid@RequestBody BuryingPointRequest request) {
        Long userId = null;

        try {
            userId = getUserIdNoError();
        } catch (Exception e) {
        }

        BuryingPoint point = new BuryingPoint();
        point.setUserId(userId);
        point.setGameType(request.getGameType());
        point.setBuryingType(request.getBuryingType());
        point.setChannelId(getChannelId());
        dataBuryingPointService.save(point);
//        rabbitTemplate.convertAndSend("monitor_rick_buryingpoint", uicBuryingPoint);
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
    @RequestMapping("firstLoad")
    public Object buryingPointReturnStatus(@Valid@RequestBody BuryingPointRequest request) {

        Long userId = null;
        BuryingPoint point = null;
        try {
            userId = getUserId();
            point = dataBuryingPointService.getByGameTypeAndBuryingType(request.getGameType(), request.getBuryingType(), userId);
        } catch (Exception e) {
            logger.error("buryingPoint/firstLoad获取游戏埋点数据失败,{}",e);
        }


        BuryingPoint dataBuryingPoint = new BuryingPoint();
        dataBuryingPoint.setUserId(userId);
        dataBuryingPoint.setGameType(request.getGameType());
        dataBuryingPoint.setBuryingType(request.getBuryingType());
        dataBuryingPoint.setChannelId(getChannelId());
        dataBuryingPointService.save(dataBuryingPoint);
        if(point != null){
            return data(false);
        }else{
            return data(true);
        }
    }


}
