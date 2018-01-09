package com.wf.data.dao.mall;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mall.entity.ActivityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "activity_info")
public interface ActivityInfoDao extends CrudDao<ActivityInfo> {

    public ActivityInfo getByActivityTypeAndChannelId(@Param("activityType") Integer activityType, @Param("channelId") Long channelId);

    public List<Long> getListByActivityTypeAndChannelId(@Param("activityType") Integer activityType, @Param("channelId") Long channelId);

    List<Long> getListByChannelId(Map<String,Object> map);
}
