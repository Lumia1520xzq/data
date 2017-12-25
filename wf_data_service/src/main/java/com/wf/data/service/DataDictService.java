package com.wf.data.service;

import com.wf.core.cache.CacheHander;
import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.data.DataDictDao;
import com.wf.data.dao.data.entity.DataDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataDictService extends CrudService<DataDictDao, DataDict> {
    @Autowired
    private CacheHander cacheHander;
    /**
     * 查询字典类型列表
     *
     * @return
     */
    public List<String> findTypeList() {
        return cacheHander.cache(DataCacheKey.SYS_DICT_ALL_LIST.key(), () -> dao.findTypeList(new DataDict()));
    }

    /**
     * 根据value获取字典项
     *
     * @param type
     * @param value
     * @return
     */
    public DataDict getDictByValue(final String type, final int value) {
        return cacheHander.cache(DataCacheKey.SYS_DICT_BY_VALUE.key(type, value), () -> {
            DataDict result = dao.getDictByValue(type, value);
            if (result == null) {
                logger.warn("Dict type,value = " + type + "," + value + " exception");
            }
            return result;
        });
    }

    /**
     * 获取type类型的字典列表
     *
     * @param type
     * @return
     */
    public List<DataDict> getDictList(final String type) {
        return cacheHander.cache(DataCacheKey.SYS_DICT_BY_TYPE.key(type), () -> {
            DataDict dict = new DataDict();
            dict.setType(type);
            return dao.findByCond(dict);
        });
    }


    @Override
    protected void clearCache(DataDict dict) {
        cacheHander.delete(DataCacheKey.SYS_DICT_ALL_LIST.key());
        cacheHander.delete(DataCacheKey.SYS_DICT_BY_TYPE.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_DICT_TYPE_LABEL_LIST.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_DICT_BY_TYPE_MAP.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_DICT_BY_VALUE.key(dict.getType(), dict.getValue()));
    }
}
