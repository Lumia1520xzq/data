package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserLabel;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "dataware_user_label")
public interface DatawareUserLabelDao extends CrudDao<DatawareUserLabel> {
    DatawareUserLabel getLabelsByUserId(@Param("userId") long userId, @Param("businessDate") String businessDate);
}
