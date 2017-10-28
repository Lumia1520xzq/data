package com.wf.uic.rpc;

import com.wf.uic.rpc.dto.JsonResult;
import com.wf.uic.rpc.dto.UicBuryingPointDto;

public interface BuryingRpcService {

    /**
     * 保存埋点
     */
    public JsonResult save(UicBuryingPointDto uicBuryingPointDto);
}
