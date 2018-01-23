package com.wf.data.dao.base;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.base.entity.ChannelInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "channel_info")
public interface ChannelInfoDao extends CrudDao<ChannelInfo> {

    List<ChannelInfo> findMainChannel();

    List<ChannelInfo> findSubChannel(Long parentId);

    List<ChannelInfo> findAll();

    String findChannelNameById(Map<String, Object> params);
}
