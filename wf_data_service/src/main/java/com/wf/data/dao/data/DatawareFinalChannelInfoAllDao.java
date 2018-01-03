package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;

@MyBatisDao(tableName = "dataware_final_channel_info_all")
public interface DatawareFinalChannelInfoAllDao extends CrudDao<DatawareFinalChannelInfoAll> {

}
