package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.entity.mysql.UicUserLog;
import com.wf.data.dao.mysql.UicUserLogDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UicUserLogService extends CrudService<UicUserLogDao, UicUserLog> {
    public List<String> findIpByIpCount(UicUserLog log){
        return dao.findIpByIpCount(log);
    }
    public List<UicUserLog> findUserLogByIp(UicUserLog log){
        return dao.findUserLogByIp(log);
    }
    public List<String> findIpByUserId(Long userId){
    	return dao.findIpByUserId(userId);
    }
    public UicUserLog getUserCountByIp(List<String> ips) {
    	return dao.getUserCountByIp(ips);
    }
}
