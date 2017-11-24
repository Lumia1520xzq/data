package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.data.DataConfigDao;
import com.wf.data.dao.data.entity.DataConfig;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataConfigService extends CrudService<DataConfigDao, DataConfig> {
    @Override
    public DataConfig get(Long id) {
        return super.get(id);
    }

    @Override
    public List<DataConfig> findList(DataConfig DataConfig) {
        return super.findList(DataConfig);
    }

    @Override
    protected void clearCache(DataConfig entity) {
        cacheHander.delete(DataCacheKey.DATA_CONFIG_BY_NAME.key(entity.getName()));
    }

    /**
     * 根据name获取配置项
     * @param name
     * @return null / wapSysConfig
     */
    public DataConfig findByName(final String name) {
        return cacheHander.cache(DataCacheKey.DATA_CONFIG_BY_NAME.key(name), () -> dao.findByName(name));
    }

    /**
     * 根据name获取配置项的值
     * @param name
     * @param defValue 默认值(String/int/double/float/long)
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValueByName(String name, T defValue) {
        try {
            DataConfig config = findByName(name);
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
     * @param name
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


    /**
     * 根据name获取配置项
     *
     * @param name
     * @return null / BaseConfig
     */
    public DataConfig findByNameAndChannel(final String name,Long channel) {
        return cacheHander.cache(DataCacheKey.DATA_CONFIG_BY_NAME.key(name,channel), () -> dao.findByNameAndChannel(name,channel));
    }
}
