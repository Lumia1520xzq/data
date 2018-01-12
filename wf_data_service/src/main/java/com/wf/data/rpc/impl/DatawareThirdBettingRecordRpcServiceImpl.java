package com.wf.data.rpc.impl;

import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.data.entity.DatawareThirdBettingRecord;
import com.wf.data.rpc.DatawareThirdBettingRecordRpcService;
import com.wf.data.rpc.dto.DatawareThirdBettingRecordDto;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.service.data.DatawareThirdBettingRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DatawareThirdBettingRecordRpcServiceImpl implements DatawareThirdBettingRecordRpcService {
    @Autowired
    private DatawareThirdBettingRecordService datawareThirdBettingRecordService;


    @Override
    public JsonResult<String> save(DatawareThirdBettingRecordDto bettingRecordDto) {
        try {
            DatawareThirdBettingRecord record = new DatawareThirdBettingRecord();
            BeanUtils.copyProperties(bettingRecordDto, record);
            datawareThirdBettingRecordService.saveRecord(record);
            return JsonResultUtils.markSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultUtils.markErrorResult("");
        }

    }

}
