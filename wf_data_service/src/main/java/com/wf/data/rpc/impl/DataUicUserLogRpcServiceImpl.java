package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.dao.uic.entity.UicUserLog;
import com.wf.data.rpc.DataUicUserLogRpcService;
import com.wf.data.rpc.dto.DataUicUserLogDto;
import com.wf.data.service.UicUserLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataUicUserLogRpcServiceImpl implements DataUicUserLogRpcService {

    @Autowired
    private UicUserLogService uicUserLogService;

    @Override
    public List<String> findIpByIpCount(DataUicUserLogDto logDto) {
        UicUserLog log = new UicUserLog();
        BeanUtils.copyProperties(logDto, log);
        List<String> ipList = uicUserLogService.findIpByIpCount(log);
        return ipList;
    }


    @Override
    public List<DataUicUserLogDto> findUserLogByIp(DataUicUserLogDto logDto) {
        List<DataUicUserLogDto> list = Lists.newArrayList();
        UicUserLog log = new UicUserLog();
        BeanUtils.copyProperties(logDto, log);

        List<UicUserLog> uicUserLogs = uicUserLogService.findUserLogByIp(log);
        if (null != uicUserLogs && uicUserLogs.size() > 0) {
            for (UicUserLog item : uicUserLogs) {
                DataUicUserLogDto dto = new DataUicUserLogDto();
                BeanUtils.copyProperties(dto, item);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public List<String> findIpByUserId(Long userId) {
        return uicUserLogService.findIpByUserId(userId);
    }

    @Override
    public DataUicUserLogDto getUserCountByIp(List<String> ips) {
        UicUserLog log = uicUserLogService.getUserCountByIp(ips);
        if(null != log){
            DataUicUserLogDto dto = new DataUicUserLogDto();
            BeanUtils.copyProperties(dto, log);
            return dto;
        }
        return null;
    }

}