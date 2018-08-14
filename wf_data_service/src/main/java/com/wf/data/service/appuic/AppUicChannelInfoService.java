package com.wf.data.service.appuic;

import com.wf.core.cache.CacheKey;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.appuic.AppUicChannelInfoDao;
import com.wf.data.dao.appuic.entity.AppUicChannelInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JoeH
 */
@Service
public class AppUicChannelInfoService extends CrudService<AppUicChannelInfoDao, AppUicChannelInfo> {

    /**
     * 渠道对象（缓存一天）
     */
    @Override
    public AppUicChannelInfo get(final Long id) {
        return cacheHander.cache(DataCacheKey.DATA_APPUIC_CHANNEL_INFO_BY_ID.key(id), () -> dao.get(id), CacheKey.DAY_1);
    }


    public List<AppUicChannelInfo> findMainChannel() {
        return cacheHander.cache(DataCacheKey.DATA_APPUIC_CHANNEL_INFO_ALL.key(), () -> dao.findMainChannel(), CacheKey.MINUTE_30);
    }

    public List<Long> findMainChannelIds() {
        List<Long> list = new ArrayList<>();
        List<AppUicChannelInfo> mainChannels = findMainChannel();
        if (CollectionUtils.isNotEmpty(mainChannels)) {
            for (AppUicChannelInfo mainChannel : mainChannels) {
                list.add(mainChannel.getId());
            }
        }
        return list;
    }


    public List<AppUicChannelInfo> findAll() {
        return dao.findAll();
    }


    public List<AppUicChannelInfo> findSubChannel(final Long channelId) {
        return dao.findSubChannel(channelId);
    }

    /**
     * 清空缓存
     */
    @Override
    protected void clearCache(AppUicChannelInfo entity) {
        cacheHander.delete(DataCacheKey.DATA_APPUIC_CHANNEL_INFO_BY_ID.key(entity.getId()));
    }

    public String findChannelNameById(Map<String, Object> params) {
        return dao.findChannelNameById(params);
    }

    public List<Long> findSubChannelIds(Long parentId) {
        return dao.findSubChannelIds(parentId);
    }

    public List<AppUicChannelInfo> listSubChannel(Map<String, Object> params) {
        return dao.listSubChannel(params);
    }
}
