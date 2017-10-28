package com.wf.uic.crudserivce;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.cache.CacheData;
import com.wf.core.cache.CacheKey;
import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.dao.entity.mysql.UicWechatConfig;
import com.wf.uic.dao.mysql.UicWechatConfigDao;
import org.springframework.stereotype.Service;

/**
 * 公众号管理
 */
@Service
public class UicWeChatConfigCrudService extends CrudService<UicWechatConfigDao, UicWechatConfig> {
    @Override
    protected void clearCache(UicWechatConfig entity) {
        super.clearCache(entity);
        cacheHander.delete(UicCacheKey.UIC_WECHAT_CONFIG.key(entity.getChannelId()));
    }

    /**
     * 根据渠道获取公众号配置
     *
     * @param channel
     * @return
     */
    public UicWechatConfig getByChannel(final Long channel) {
        if (channel == null) {
            return null;
        }
        return cacheHander.cache(UicCacheKey.UIC_WECHAT_CONFIG.key(channel), new CacheData() {
            @Override
            public Object findData() {
                return dao.getByChannel(channel);
            }
        }, CacheKey.MINUTE_5);
    }

    public JSONObject getWechatInfo(final Long channel) {
        UicWechatConfig config = getByChannel(channel);
        if (config == null) {
            return null;
        }

        return JSONObject.parseObject(config.getAttach());
    }
}
