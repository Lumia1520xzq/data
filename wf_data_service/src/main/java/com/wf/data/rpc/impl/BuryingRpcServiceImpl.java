package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.mycat.entity.BuryingPoint;
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

    @Override
    public JsonResult save(BuryingPointDto buryingPointDto) {

        BuryingPoint point = new BuryingPoint();
        BeanUtils.copyProperties(buryingPointDto, point);
        buryingPointService.save(point);

        return JsonResultUtils.markSuccessResult();
    }

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

    @Override
    public Date getLastLoginWealTime(Long userId, Integer buryingType) {
        return buryingPointService.getLastLoginWealTime(userId,buryingType);
    }

}
