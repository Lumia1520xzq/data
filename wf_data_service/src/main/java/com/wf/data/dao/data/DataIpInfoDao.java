package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataIpInfo;

@MyBatisDao(tableName = "data_ip_info")
public interface DataIpInfoDao extends CrudDao<DataIpInfo> {

    void updateIpCount(DataIpInfo info);

    DataIpInfo getDataIpInfo(DataIpInfo info);
}
