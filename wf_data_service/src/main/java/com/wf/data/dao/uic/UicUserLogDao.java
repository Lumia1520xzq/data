package com.wf.data.dao.uic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.uic.entity.UicUserLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_user_log")
public interface UicUserLogDao extends CrudDao<UicUserLog> {

    List<String> findIpByIpCount(UicUserLog log);

    List<UicUserLog> findUserLogByIp(UicUserLog log);
    
    List<String> findIpByUserId(@Param("userId") Long userId);
    
    UicUserLog getUserCountByIp(@Param("ips") List<String> ips);
}