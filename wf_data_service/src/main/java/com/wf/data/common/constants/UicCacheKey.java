package com.wf.data.common.constants;

import com.wf.core.cache.CacheKey;
import com.wf.core.cache.CacheType;

/**
 *
 * @author zk
 * @date 2017/10/20
 */
public enum UicCacheKey implements CacheKey {

    /**
     * 根据token获取用户信息
     */
    OAUTH2_TOKEN_INFO,

    ;
    private final String value;

    UicCacheKey() {
        this.value = CacheType.CONTROLLER.getName() + ":PLAT:OAUTH:" + name();
    }

    @Override
    public String key() {
        return value;
    }

    public String key(Object... params) {
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
