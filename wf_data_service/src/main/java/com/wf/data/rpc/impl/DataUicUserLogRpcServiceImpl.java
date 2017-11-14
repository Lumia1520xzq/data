package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.dao.entity.mysql.UicUserLog;
import com.wf.data.rpc.DataUicUserLogRpcService;
import com.wf.data.rpc.dto.UicUserLogDto;
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
    public List<String> findIpByIpCount(UicUserLogDto logDto) {
        UicUserLog log = new UicUserLog();
        BeanUtils.copyProperties(logDto, log);
        List<String> ipList = uicUserLogService.findIpByIpCount(log);
        return ipList;
    }


    @Override
    public List<UicUserLogDto> findUserLogByIp(UicUserLogDto logDto) {
        List<UicUserLogDto> list = Lists.newArrayList();
        UicUserLog log = new UicUserLog();
        BeanUtils.copyProperties(logDto, log);

        List<UicUserLog> uicUserLogs = uicUserLogService.findUserLogByIp(log);
        if (null != uicUserLogs && uicUserLogs.size() > 0) {
            for (UicUserLog item : uicUserLogs) {
                UicUserLogDto dto = new UicUserLogDto();
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
    public UicUserLogDto getUserCountByIp(List<String> ips) {
        UicUserLog log = uicUserLogService.getUserCountByIp(ips);
        if(null != log){
            UicUserLogDto dto = new UicUserLogDto();
            BeanUtils.copyProperties(dto, log);
            return dto;
        }
        return null;
    }

}
