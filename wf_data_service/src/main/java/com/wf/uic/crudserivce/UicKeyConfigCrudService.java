package com.wf.uic.crudserivce;

import com.wf.core.cache.CacheKey;
import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.dao.mysql.UicKeyConfigDao;
import com.wf.uic.dao.entity.mysql.UicKeyConfig;
import org.springframework.stereotype.Service;

/**
 * KEY配置
 */
@Service
public class UicKeyConfigCrudService extends CrudService<UicKeyConfigDao, UicKeyConfig> {
	/**
	 * 根据类型和渠道获取KEY信息
	 * @param type
	 * @param channelId
	 * @return
	 */
	public UicKeyConfig getKeyInfo(final Integer type, final Long channelId){
		return cacheHander.cache(UicCacheKey.UIC_KEY_CONFIG_CHANNEL_ID_INFO.key(type, channelId),
				()->dao.getKeyInfoByChannelId(type, channelId), CacheKey.DAY_2);
	}
	
	/**
	 * 根据类型和渠道获取KEY信息
	 * @param type
	 * @param appId
	 * @return
	 */
	public UicKeyConfig getKeyInfo(final Integer type,final String appId){
		return cacheHander.cache(UicCacheKey.UIC_KEY_CONFIG_INFO.key(type, appId),
				()->dao.getKeyInfo(type, appId),CacheKey.DAY_2);
	}
}
