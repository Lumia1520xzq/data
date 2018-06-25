package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.mycatdata.entity.BuryingPoint;
import com.wf.data.rpc.BuryingRpcService;
import com.wf.data.rpc.dto.BuryingPointDto;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.service.BuryingPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class BuryingRpcServiceImpl implements BuryingRpcService {
    @Autowired
    private BuryingPointService buryingPointService;

    /**
     * 保存埋点
     * @param buryingPointDto
     * @return
     */
    @Override
    public JsonResult save(BuryingPointDto buryingPointDto) {

        BuryingPoint point = new BuryingPoint();
        BeanUtils.copyProperties(buryingPointDto, point);
        buryingPointService.save(point);

        return JsonResultUtils.markSuccessResult();
    }

    /**
     * 获取最后一次游戏埋点
     * @param userId
     * @param gameType
     * @return
     */
    @Override
    public BuryingPointDto findLastGameLoading(Long userId, Integer gameType) {
        BuryingPoint point = buryingPointService.findLastGameLoading(userId, gameType);

        if (null != point) {
            BuryingPointDto dto = new BuryingPointDto();
            BeanUtils.copyProperties(point, dto);
            return dto;
        }
        return null;
    }

    /**
     * 获取list
     * @param userId
     * @param num
     * @param channelId
     * @return
     */
    @Override
    public List<BuryingPointDto> getUserLastPlayGame(Long userId, Integer num, Long channelId) {
        List<BuryingPointDto> list = Lists.newArrayList();
        List<BuryingPoint> result = buryingPointService.getUserLastPlayGame(userId, num, channelId);
        if (null != result && result.size() > 0) {
            for (BuryingPoint item : result) {
                BuryingPointDto dto = new BuryingPointDto();
                BeanUtils.copyProperties(item, dto);
                list.add(dto);
            }
        }
        return list;
    }

    /**
     * 根据条件获取买点信息
     * @param gameType
     * @param buryingType
     * @param userId
     * @return
     */
    @Override
    public BuryingPointDto getByGameTypeAndBuryingType(Integer gameType, Integer buryingType, Long userId) {
        BuryingPoint point = buryingPointService.getByGameTypeAndBuryingType(gameType, buryingType, userId);

        if (null != point) {
            BuryingPointDto pointDto = new BuryingPointDto();
            BeanUtils.copyProperties(point,pointDto);
            return pointDto;
        }
        return null;
    }

    /**
     * 获取最后一次埋点
     * @param userId
     * @param buryingType
     * @return
     */
    @Override
    public Date getLastLoginWealTime(Long userId, Integer buryingType) {
        return buryingPointService.getLastLoginWealTime(userId,buryingType);
    }

}
