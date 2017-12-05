package com.wf.data.common.constants;


import com.wf.core.cache.CacheKey;

/**
 * controller
 * @author Fe 2016年4月16日
 */
public enum DataCacheKey implements CacheKey {
	/**
	 * 渠道信息
	 */
	CHANNEL_INFO,
	UIC_USER_BEHAVIORTYPE_BY_EVENTID,

	DATA_CONFIG_BY_NAME,

	DATA_IP_USER,

	DATA_RISK_FLAG,

	;
	private final String value;
	private DataCacheKey() {
		this.value = "DATA:" + name();
	}
	@Override
	public String key() {
		return value;
	}
	
	public String key(Object...params) {
		StringBuilder key = new StringBuilder(value);
		if (params != null && params.length > 0) {
			for (Object param : params) {
				key.append('-');
				key.append(String.valueOf(param));
			}
		}
		return key.toString();
	}
}
