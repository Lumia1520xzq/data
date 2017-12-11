package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DataIpInfoDao;
import com.wf.data.dao.data.entity.DataIpInfo;
import org.springframework.stereotype.Service;

/**
 * @author: lcs
 * @date: 2017/12/04
 */

@Service
public class DataIpInfoService extends CrudService<DataIpInfoDao,DataIpInfo> {

    public void updateIpCount(DataIpInfo info){
        DataIpInfo entity = dao.getDataIpInfo(info);
        if(null == entity){
            entity = new DataIpInfo();
            entity.setChannelId(info.getChannelId());
            entity.setIp(info.getIp());
            entity.setIpCount(0l);
            entity.setLoginDate(info.getLoginDate());
            super.save(entity);
        }
        info.setId(entity.getId());
        dao.updateIpCount(info);
    }
}
