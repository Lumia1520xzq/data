package com.wf.data.rpc.impl;

import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.entity.mycat.UicBehaviorRecord;
import com.wf.data.dao.entity.mysql.UicBehaviorType;
import com.wf.data.rpc.BehaviorRecordRpcService;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.BehaviorRecordDto;
import com.wf.data.service.MycatUicBehaviorRecordService;
import com.wf.data.service.UicBehaviorTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

public class BehaviorRecordRpcServiceImpl implements BehaviorRecordRpcService {

    @Autowired
    private MycatUicBehaviorRecordService mycatUicBehaviorRecordService;
    @Autowired
    private UicBehaviorTypeService uicBehaviorTypeService;

    @Override
    public JsonResult<String> save(BehaviorRecordDto behaviorRecordDto) {

        try {
            UicBehaviorRecord uicBehaviorRecord = new UicBehaviorRecord();
            BeanUtils.copyProperties(behaviorRecordDto, uicBehaviorRecord);

            UicBehaviorType uicBehaviorType = uicBehaviorTypeService.getByEventId(uicBehaviorRecord.getBehaviorEventId());
            if (uicBehaviorType != null) {
                String name = uicBehaviorType.getFullName();
                uicBehaviorRecord.setBehaviorName(name);
                mycatUicBehaviorRecordService.save(uicBehaviorRecord);
            }
            return JsonResultUtils.markSuccessResult();
        } catch (BeansException e) {
            e.printStackTrace();
            return JsonResultUtils.markErrorResult("");
        }
    }
}
