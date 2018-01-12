package com.wf.data.rpc;

import com.wf.data.rpc.dto.DatawareThirdBettingRecordDto;
import com.wf.data.rpc.dto.JsonResult;

/**
 * @author: lcs
 * @date: 2017/11/15
 */
public interface DatawareThirdBettingRecordRpcService {


    /**
     * 保存第三方投注记录
     * @param bettingRecordDto
     * @return
     */
    JsonResult<String> save(DatawareThirdBettingRecordDto bettingRecordDto);
}
