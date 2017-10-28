package com.wf.uic.rpc;

import com.wf.uic.rpc.dto.JsonResult;
import com.wf.uic.rpc.dto.UicBehaviorRecordDto;

public interface UicBehaviorRecordRpcService {

    /**
     * 保存埋点
     */
    JsonResult<String> save(UicBehaviorRecordDto uicBehaviorRecordDto);
}
