package com.wf.data.dao.appuic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.appuic.entity.AppUicChannelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "channel_info")
public interface AppUicChannelInfoDao extends CrudDao<AppUicChannelInfo> {

    List<AppUicChannelInfo> findMainChannel();

    List<AppUicChannelInfo> findSubChannel(Long parentId);

    List<AppUicChannelInfo> findAll();

    String findChannelNameById(Map<String, Object> params);

    List<Long> findSubChannelIds(@Param("parentId") Long parentId);
}
