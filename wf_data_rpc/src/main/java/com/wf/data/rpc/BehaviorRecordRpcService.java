package com.wf.data.rpc;

import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.BehaviorRecordDto;

/**
 * @author Ping
 *
 * -- 新的埋点
 */
public interface BehaviorRecordRpcService {

    /**
     * 保存埋点
     * @param behaviorRecordDto
     * @return
     */
    JsonResult<String> save(BehaviorRecordDto behaviorRecordDto);
}
