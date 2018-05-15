package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.BigDataDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "bigdata_dict")
public interface BigDataDictDao extends CrudDao<BigDataDict> {

    List<String> findTypeList(BigDataDict dict);

    BigDataDict getDictByValue(@Param("type") String type, @Param("value") int value);

    List<BigDataDict> findByCond(BigDataDict dict);

    List<BigDataDict> findListByType(@Param("type") String type);
}
