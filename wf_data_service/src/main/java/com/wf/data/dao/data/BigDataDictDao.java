package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.BigDataDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "bigdata_dict")
public interface BigDataDictDao extends CrudDao<BigDataDict> {

    /**
     * 获取所有type
     *
     * @param dict
     * @return
     */
    List<String> findTypeList(BigDataDict dict);

    /**
     * 根据type和value获取对象
     *
     * @param type
     * @param value
     * @return
     */
    BigDataDict getDictByValue(@Param("type") String type, @Param("value") int value);

    /**
     * 根据type获取字典信息
     * sort排序
     *
     * @param dict
     * @return
     */
    List<BigDataDict> findByCond(BigDataDict dict);

    /**
     * 根据type获取字典信息
     *
     * @param type
     * @return
     */
    List<BigDataDict> findListByType(@Param("type") String type);
}
