package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.dao.data.BigDataDictDao;
import com.wf.data.dao.data.entity.BigDataDict;
import com.wf.data.dao.data.entity.DataDict;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BigDataDictService extends CrudService<BigDataDictDao, BigDataDict> {

    /**
     * 查询字典类型列表
     *
     * @return
     */
    public List<String> findTypeList() {
        return cacheHander.cache(DataCacheKey.SYS_BIG_DATA_DICT_ALL_LIST.key(), () -> dao.findTypeList(new BigDataDict()));
    }

    /**
     * 根据value获取字典项
     *
     * @param type
     * @param value
     * @return
     */
    public DataDict getDictByValue(final String type, final int value) {
        return cacheHander.cache(DataCacheKey.SYS_BIG_DATA_DICT_BY_VALUE.key(type, value), () -> {
            BigDataDict result = dao.getDictByValue(type, value);
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
        return cacheHander.cache(DataCacheKey.SYS_BIG_DATA_DICT_BY_TYPE.key(type), () -> {
            BigDataDict dict = new BigDataDict();
            dict.setType(type);
            return dao.findByCond(dict);
        });
    }


    @Override
    protected void clearCache(BigDataDict dict) {
        cacheHander.delete(DataCacheKey.SYS_BIG_DATA_DICT_ALL_LIST.key());
        cacheHander.delete(DataCacheKey.SYS_BIG_DATA_DICT_BY_TYPE.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_BIG_DATA_DICT_TYPE_LABEL_LIST.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_BIG_DATA_DICT_BY_TYPE_MAP.key(dict.getType()));
        cacheHander.delete(DataCacheKey.SYS_BIG_DATA_DICT_BY_VALUE.key(dict.getType(), dict.getValue()));
    }

    public List<BigDataDict> findListByType(String type) {
        return dao.findListByType(type);
    }
}
