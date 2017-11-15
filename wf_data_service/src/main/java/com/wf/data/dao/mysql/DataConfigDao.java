package com.wf.data.dao.mysql;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.DataConfig;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "data_config")
public interface DataConfigDao extends CrudDao<DataConfig> {
    /**
     * 根据name获取config
     * @param name
     * @return
     */
    DataConfig findByName(String name);


    DataConfig findByNameAndChannel(@Param("name")String name, @Param("channelId") Long channelId);
}
