package com.wf.uic.common.constants;


import com.wf.core.cache.CacheKey;
import com.wf.core.cache.CacheType;

/**
 * controller
 * @author Fe 2016年4月16日
 */
public enum ControllerCacheKey implements CacheKey {
	/**
	 * uic user
	 */
	MYCAT_UIC_USER_BY_ID,
	MYCAT_UIC_USER_BY_LOGINNAME,
	MYCAT_UIC_USER_BY_THIRD_ID,
	MYCAT_UIC_USER_BY_VISITOR_TOKEN,
	UIC_USER_BY_INVITATION_CODE,
	UIC_USER_BY_PARENT_INVITATION_CODE,
	UIC_USER_GROUP_BY_PARENT_INVITATION_CODE,
	UIC_USER_BEHAVIORTYPE_BY_EVENTID
	;
	private final String value;
	private ControllerCacheKey() {
		this.value = CacheType.CONTROLLER.getName() + '_' + name();
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
