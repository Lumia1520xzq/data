package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.entity.mycat.UicBuryingPoint;
import com.wf.data.rpc.BuryingRpcService;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.UicBuryingPointDto;
import com.wf.data.service.MycatUicBuryingPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

public class BuryingRpcServiceImpl implements BuryingRpcService {
    @Autowired
    private MycatUicBuryingPointService mycatUicBuryingPointService;

    @Override
    public JsonResult save(UicBuryingPointDto uicBuryingPointDto) {

        UicBuryingPoint uicBuryingPoint = new UicBuryingPoint();
        BeanUtils.copyProperties(uicBuryingPointDto, uicBuryingPoint);
        mycatUicBuryingPointService.save(uicBuryingPoint);

        return JsonResultUtils.markSuccessResult();
    }

    @Override
    public UicBuryingPointDto findLastGameLoading(Long userId, Integer gameType) {
        UicBuryingPoint point = mycatUicBuryingPointService.findLastGameLoading(userId, gameType);

        if (null != point) {
            UicBuryingPointDto dto = new UicBuryingPointDto();
            BeanUtils.copyProperties(point, dto);
            return dto;
        }
        return null;
    }

    @Override
    public List<UicBuryingPointDto> getUserLastPlayGame(Long userId, Integer num, Long channelId) {
        List<UicBuryingPointDto> list = Lists.newArrayList();
        List<UicBuryingPoint> result = mycatUicBuryingPointService.getUserLastPlayGame(userId, num, channelId);
        if (null != result && result.size() > 0) {
            for (UicBuryingPoint item : result) {
                UicBuryingPointDto dto = new UicBuryingPointDto();
                BeanUtils.copyProperties(item, dto);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public UicBuryingPointDto getByGameTypeAndBuryingType(Integer gameType, Integer buryingType, Long userId) {
        UicBuryingPoint point = mycatUicBuryingPointService.getByGameTypeAndBuryingType(gameType, buryingType, userId);

        if (null != point) {
            UicBuryingPointDto pointDto = new UicBuryingPointDto();
            BeanUtils.copyProperties(point,pointDto);
            return pointDto;
        }
        return null;
    }

    @Override
    public Date getLastLoginWealTime(Long userId, Integer buryingType) {
        return mycatUicBuryingPointService.getLastLoginWealTime(userId,buryingType);
    }

}
