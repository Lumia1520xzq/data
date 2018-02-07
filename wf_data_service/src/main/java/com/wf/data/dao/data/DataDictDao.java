package com.wf.data.dao.data;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DataDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "data_dict")
public interface DataDictDao extends CrudDao<DataDict> {

    List<String> findTypeList(DataDict dict);

    DataDict getDictByValue(@Param("type") String type, @Param("value") int value);

    List<DataDict> findByCond(DataDict dict);

    List<DataDict> findListByType(@Param("type")String type);
}
