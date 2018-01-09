package com.wf.data.service.mall;


import com.wf.core.cache.CacheData;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.mall.ActivityInfoDao;
import com.wf.data.dao.mall.entity.ActivityInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityInfoService extends CrudService<ActivityInfoDao, ActivityInfo> {

    /**
     * 根据活动类型，渠道号获取活动
     *
     * @param activityType
     * @param channelId
     * @return
     */
    public ActivityInfo getByActivityTypeAndChannelId(final Integer activityType, final Long channelId) {
        return cacheHander.cache(DataCacheKey.PLAT_ACTIVITTY_INFO.key(activityType, channelId), new CacheData() {
            @Override
            public Object findData() {
                return dao.getByActivityTypeAndChannelId(activityType, channelId);
            }
        });
    }

    public List<Long> getListByActivityTypeAndChannelId(Integer activityType, Long channelId) {
        return dao.getListByActivityTypeAndChannelId(activityType, channelId);
    }


    public List<Long> getListByChannelId(Map<String,Object> map) {
        return dao.getListByChannelId(map);
    }
}
