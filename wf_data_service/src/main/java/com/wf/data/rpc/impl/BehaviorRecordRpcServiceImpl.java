package com.wf.data.rpc.impl;

import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.mycatdata.entity.BehaviorRecord;
import com.wf.data.dao.data.entity.BehaviorType;
import com.wf.data.rpc.BehaviorRecordRpcService;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.BehaviorRecordDto;
import com.wf.data.service.BehaviorRecordService;
import com.wf.data.service.BehaviorTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

public class BehaviorRecordRpcServiceImpl implements BehaviorRecordRpcService {

    @Autowired
    private BehaviorRecordService behaviorRecordService;
    @Autowired
    private BehaviorTypeService behaviorTypeService;

    @Override
    public JsonResult<String> save(BehaviorRecordDto behaviorRecordDto) {

        try {
            BehaviorRecord behaviorRecord = new BehaviorRecord();
            BeanUtils.copyProperties(behaviorRecordDto, behaviorRecord);

            BehaviorType behaviorType = behaviorTypeService.getByEventId(behaviorRecord.getBehaviorEventId());
            if (behaviorType != null) {
                String name = behaviorType.getFullName();
                behaviorRecord.setBehaviorName(name);
                behaviorRecordService.save(behaviorRecord);
            }
            return JsonResultUtils.markSuccessResult();
        } catch (BeansException e) {
            e.printStackTrace();
            return JsonResultUtils.markErrorResult("");
        }
    }
}
