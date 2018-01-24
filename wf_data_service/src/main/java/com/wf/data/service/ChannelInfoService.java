package com.wf.data.service;

import com.wf.core.cache.CacheKey;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.base.ChannelInfoDao;
import com.wf.data.dao.base.entity.ChannelInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author  JoeH
 */
@Service
public class ChannelInfoService extends CrudService<ChannelInfoDao, ChannelInfo> {

    /**
     * 渠道对象（缓存一天）
     */
    @Override
    public ChannelInfo get(final Long id) {
        return cacheHander.cache(DataCacheKey.DATA_CHANNEL_INFO_BY_ID.key(id), () -> dao.get(id), CacheKey.DAY_1);
    }

    /**
     * Channel主键自己生成
     */
    @Override
    public void save(ChannelInfo entity) {
        if (dao.get(entity.getId()) == null) {
            if (entity.getCreateTime() == null) {
                entity.setCreateTime(new Date());
            }
            dao.insert(entity);
        } else {
            this.preUpdate(entity);
            dao.update(entity);
        }
        clearCache(entity);
    }

    public List<ChannelInfo> findMainChannel() {
        return dao.findMainChannel();
    }

    public List<Long> findMainChannelIds() {
        List<Long> list = new ArrayList<>();
        List<ChannelInfo> mainChannels = findMainChannel();
        if (CollectionUtils.isNotEmpty(mainChannels)) {
           for(ChannelInfo mainChannel:mainChannels){
                list.add(mainChannel.getId());
           }
        }
        return list;
    }


    public List<ChannelInfo> findAll() {
        return dao.findAll();
    }


    public List<ChannelInfo> findSubChannel(final Long channelId) {
        return dao.findSubChannel(channelId);
    }

    /**
     * 清空缓存
     */
    @Override
    protected void clearCache(ChannelInfo entity) {
        cacheHander.delete(DataCacheKey.DATA_CHANNEL_INFO_BY_ID.key(entity.getId()));
    }

    public String findChannelNameById(Map<String, Object> params) {
        return dao.findChannelNameById(params);
    }

    public List<Long> findSubChannelIds(Long parentId) {
        return dao.findSubChannelIds(parentId);
    }
}
