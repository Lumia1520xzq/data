/**
 * http://www.lbanma.com
 */
package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.common.constants.DictCacheKey;
import com.wf.uic.dao.mysql.UicDictDao;
import com.wf.uic.dao.entity.mysql.UicDict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典Service
 * @author fxy
 */
@Service
public class DictCrudService extends CrudService<UicDictDao, UicDict> {

    /**
     * 查询字段类型列表
     *
     * @return
     */
    public List<String> findTypeList() {
        return cacheHander.cache(DictCacheKey.SYS_DICT_ALL_LIST.key(),
                () -> dao.findTypeList(new UicDict()));
    }

    @Override
    protected void clearCache(UicDict dict) {
        cacheHander.delete(DictCacheKey.SYS_DICT_RSP_ALL.key());
        cacheHander.delete(DictCacheKey.SYS_DICT_ALL_LIST.key());
        cacheHander.delete(DictCacheKey.SYS_DICT_BY_TYPE.key(dict.getType()));
        cacheHander.delete(DictCacheKey.SYS_DICT_BY_TYPE_STRING.key(dict.getType()));
        cacheHander.delete(DictCacheKey.SYS_DICT_BY_TYPE_MAP.key(dict.getType()));
        cacheHander.delete(DictCacheKey.SYS_DICT_BY_VALUE.key(dict.getType(), dict.getValue()));
    }

    @Override
    public void save(UicDict dict) {
        super.save(dict);
    }

    @Override
    public void delete(UicDict dict) {
        super.delete(dict);
    }

    public UicDict getDict(final String type, final int value) {
        return cacheHander.cache(DictCacheKey.SYS_DICT_BY_VALUE.key(type, value), ()->{
            UicDict result = dao.getDictByValue(type, value);
            if (result == null) {
                logger.warn("Dict type,value = " + type + "," + value + " exception");
            }
            return result;
        });
    }

    public String getDictLabel(String type, int value) {
        UicDict dict = getDict(type, value);
        return dict == null? "":dict.getLabel();
    }

}
