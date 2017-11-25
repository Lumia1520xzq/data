package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.uic.UicUserLogDao;
import com.wf.data.dao.uic.entity.UicUserLog;
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


    public String getLastIpByUserId(Long userId){
    	return dao.getLastIpByUserId(userId);
    }
    public UicUserLog getUserCountByIp(List<String> ips) {
    	return dao.getUserCountByIp(ips);
    }


    public Integer getUserCount(UicUserLog log){
        return dao.getUserCount(log);
    }
}
