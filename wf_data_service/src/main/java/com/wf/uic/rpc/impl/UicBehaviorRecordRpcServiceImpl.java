package com.wf.uic.rpc.impl;

import com.wf.uic.common.utils.JsonResultUtils;
import com.wf.uic.dao.entity.mycat.UicBehaviorRecord;
import com.wf.uic.dao.entity.mycat.UicBehaviorType;
import com.wf.uic.rpc.UicBehaviorRecordRpcService;
import com.wf.uic.rpc.dto.JsonResult;
import com.wf.uic.rpc.dto.UicBehaviorRecordDto;
import com.wf.uic.service.MycatUicBehaviorRecordService;
import com.wf.uic.service.UicBehaviorTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UicBehaviorRecordRpcServiceImpl implements UicBehaviorRecordRpcService {

    @Autowired
    private MycatUicBehaviorRecordService mycatUicBehaviorRecordService;
    @Autowired
    private UicBehaviorTypeService uicBehaviorTypeService;

    @Override
    public JsonResult<String> save(UicBehaviorRecordDto uicBehaviorRecordDto) {

        UicBehaviorRecord uicBehaviorRecord = new UicBehaviorRecord();
        BeanUtils.copyProperties(uicBehaviorRecordDto, uicBehaviorRecord);

        UicBehaviorType uicBehaviorType = uicBehaviorTypeService.getByEventId(uicBehaviorRecord.getBehaviorEventId());
        if (uicBehaviorType != null) {
            String name = uicBehaviorType.getFullName();
            uicBehaviorRecord.setBehaviorName(name);
            mycatUicBehaviorRecordService.save(uicBehaviorRecord);
        }
        return JsonResultUtils.markSuccessResult();
    }
}
