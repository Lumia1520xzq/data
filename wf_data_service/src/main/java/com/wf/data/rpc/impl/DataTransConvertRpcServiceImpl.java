package com.wf.data.rpc.impl;

import com.wf.data.rpc.DataTransConvertRpcService;
import com.wf.data.service.TransConvertService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: chengsheng.liu
 * Date: 2017-11-14
 * Time: 21:44
 */
public class DataTransConvertRpcServiceImpl implements DataTransConvertRpcService{

    @Autowired
    private TransConvertService transConvertService;

    @Override
    public Double findUserSumRecharge(Long userId) {
        return transConvertService.findUserSumRecharge(userId);
    }
}
