package com.wf.uic.rpc.impl;

import com.wf.uic.common.utils.JsonResultUtils;
import com.wf.uic.dao.entity.mycat.UicBuryingPoint;
import com.wf.uic.rpc.BuryingRpcService;
import com.wf.uic.rpc.dto.JsonResult;
import com.wf.uic.rpc.dto.UicBuryingPointDto;
import com.wf.uic.service.MycatUicBuryingPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
}
