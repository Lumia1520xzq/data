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

	DATA_IP_LOCK,

	DATA_ACTIVE_EVENT,

	DATA_CHANNEL_INFO_BY_ID,

	DATA_UIC_USER_BY_ID,

	DATA_USER_GROUP,

	SYS_DICT_ALL_LIST,
	/**
	 * type类型的字典集合
	 */
	SYS_DICT_BY_TYPE,
	/**
	 * 指定值的字典缓存
	 */
	SYS_DICT_BY_VALUE,
	/**
	 * 字典的value:label 键值对
	 */
	SYS_DICT_BY_TYPE_MAP,
	/**
	 * 字典类型type的label集合
	 */
	SYS_DICT_TYPE_LABEL_LIST,

	DATA_DATAWARE_UIC_GROUP,

	PLAT_ACTIVITTY_INFO

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
