package com.wf.data.rpc.impl;

import com.wf.data.rpc.DataConfigRpcService;
import com.wf.data.rpc.dto.DataConfigDto;
import com.wf.data.service.DataConfigService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: lcs
 * @date: 2017/11/15
 */
public class DataConfigRpcServiceImpl implements DataConfigRpcService{
    @Autowired
    private DataConfigService dataConfigService;

    @Override
    public DataConfigDto findByName(String name) {
        return DataConfigDto.toDto(dataConfigService.findByName(name));
    }

    @Override
    public DataConfigDto findByNameAndChannel(String name, Long channel) {
        return DataConfigDto.toDto(dataConfigService.findByNameAndChannel(name,channel));
    }

    @Override
    public String getStringValueByName(String name) {
        return dataConfigService.getStringValueByName(name);
    }


    @Override
    public boolean getBooleanValueByName(String name) {
        return false;
    }


    @Override
    public double getDoubleValueByName(String name) {
        return 0;
    }

    @Override
    public float getFloatValueByName(String name) {
        return 0;
    }


    @Override
    public int getIntValueByName(String name) {
        return 0;
    }


    @Override
    public long getLongValueByName(String name) {
        return 0;
    }

}
