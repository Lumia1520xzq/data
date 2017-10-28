package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.ConfigCacheKey;
import com.wf.uic.dao.entity.mysql.UicConfig;
import com.wf.uic.dao.mysql.UicConfigDao;
import org.springframework.stereotype.Service;

/**
 * 配置项Service
 * @author fxy
 */
@Service
public class ConfigCrudService extends CrudService<UicConfigDao, UicConfig> {

    /**
     * 清除Redis缓存
     * @param entity
     */
    @Override
    public void clearCache(UicConfig entity) {
        cacheHander.delete(ConfigCacheKey.SYS_CONFIG_BY_NAME.key(entity.getName()));
    }

    /**
     * 根据name获取配置项
     *
     * @param name
     * @return null / sysConfig
     */
    public UicConfig findByName(final String name) {
        return cacheHander.cache(ConfigCacheKey.SYS_CONFIG_BY_NAME.key(name), () -> dao.findByName(name));
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @param defValue 默认值(String/int/double/float/long)
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValueByName(String name, T defValue) {
        try {
            UicConfig config = findByName(name);
            if (config == null) {
                return defValue;
            } else {
                if (defValue instanceof Integer) {
                    return (T) Integer.valueOf(config.getValue());
                } else if (defValue instanceof Double) {
                    return (T) Double.valueOf(config.getValue());
                } else if (defValue instanceof Float) {
                    return (T) Float.valueOf(config.getValue());
                } else if (defValue instanceof Long) {
                    return (T) Long.valueOf(config.getValue());
                } else if (defValue instanceof Boolean) {
                    return (T) Boolean.valueOf(config.getValue());
                } else if (defValue instanceof String) {
                    return (T) config.getValue();
                } else {
                    return defValue;
                }
            }
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 根据name获取配置项的值
     *
     * @return null / sysConfig
     */
    public String getStringValueByName(String name) {
        return getValueByName(name, "");
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return Boolean
     */
    public boolean getBooleanValueByName(String name) {
        return getValueByName(name, Boolean.FALSE);
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0d / double
     */
    public double getDoubleValueByName(String name) {
        return getValueByName(name, 0d);
    }

    public double getDoubleValueByName(String name, double defValue) {
        return getValueByName(name, defValue);
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / float
     */
    public float getFloatValueByName(String name) {
        return getValueByName(name, 0f);
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / int
     */
    public int getIntValueByName(String name) {
        return getValueByName(name, 0);
    }

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / long
     */
    public long getLongValueByName(String name) {
        return getValueByName(name, 0L);
    }
}
