package com.wf.data.dao.mall;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.ActivityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "activity_info")
public interface ActivityInfoDao extends CrudDao<ActivityInfo> {

    /**
     * 获取单个对象
     * @param activityType
     * @param channelId
     * @return
     */
    ActivityInfo getByActivityTypeAndChannelId(@Param("activityType") Integer activityType, @Param("channelId") Long channelId);

    /**
     * 获取id
     * @param activityType
     * @param channelId
     * @return
     */
    List<Long> getListByActivityTypeAndChannelId(@Param("activityType") Integer activityType, @Param("channelId") Long channelId);

    /**
     * 根据ChannelId获取id
     * @param map
     * @return
     */
    List<Long> getListByChannelId(Map<String,Object> map);

    /**
     * 根据ChannelId获取list
     * @param map
     * @return
     */
    List<ActivityInfo> listByChannelId(Map<String,Object> map);

    /**
     * 根据activity_type获取list
     * @param map
     * @return
     */
    List<ActivityInfo> listByActivityType(Map<String, Object> map);
}
