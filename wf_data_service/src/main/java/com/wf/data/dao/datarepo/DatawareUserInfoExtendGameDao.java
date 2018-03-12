package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendGame;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@MyBatisDao(tableName = "dataware_user_info_extend_game")
public interface DatawareUserInfoExtendGameDao extends CrudDao<DatawareUserInfoExtendGame> {
    DatawareUserInfoExtendGame getByUserIdAndGameType(Map<String,Object> params);

    void updateNewUserFlag(@Param("month")String month);

    Long getAllCount();

    void deleteAll();

    void updateBettingData(Map<String,Object> params);
}
