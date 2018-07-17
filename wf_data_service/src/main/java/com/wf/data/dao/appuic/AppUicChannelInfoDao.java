package com.wf.data.dao.appuic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.appuic.entity.AppUicChannelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "channel_info")
public interface AppUicChannelInfoDao extends CrudDao<AppUicChannelInfo> {

    /**
     * 获取所有主渠道
     * @return
     */
    List<AppUicChannelInfo> findMainChannel();

    /**
     * 获取子渠道详情
     * @param parentId
     * @return
     */
    List<AppUicChannelInfo> findSubChannel(Long parentId);

    /**
     * 获取所有渠道
     * @return
     */
    List<AppUicChannelInfo> findAll();

    /**
     * 获取渠道名称
     * @param params
     * @return
     */
    String findChannelNameById(Map<String, Object> params);

    /**
     * 捕鱼APP根据父渠道获取所有子渠道
     * @param parentId
     * @return
     */
    List<Long> findSubChannelIds(@Param("parentId") Long parentId);

    List<AppUicChannelInfo> listSubChannel(Map<String, Object> params);
}
