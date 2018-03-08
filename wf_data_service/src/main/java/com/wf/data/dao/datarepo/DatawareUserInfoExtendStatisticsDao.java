package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendStatistics;

@MyBatisDao(tableName = "dataware_user_info_extend_statistics")
public interface DatawareUserInfoExtendStatisticsDao extends CrudDao<DatawareUserInfoExtendStatistics> {

    void deleteAll();
}
