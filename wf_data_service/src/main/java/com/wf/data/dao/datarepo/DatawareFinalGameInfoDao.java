package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;

@MyBatisDao(tableName = "dataware_final_game_info")
public interface DatawareFinalGameInfoDao extends CrudDao<DatawareFinalGameInfo> {

}
