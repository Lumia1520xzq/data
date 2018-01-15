package com.wf.data.dao.data;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoAll;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_info_all")
public interface DatawareFinalChannelInfoAllDao extends CrudDao<DatawareFinalChannelInfoAll> {

    long getCountByTime(Map<String,Object> map);

    List<DatawareFinalChannelInfoAll> getListByChannelAndDate(Map<String,Object> params);

    DatawareFinalChannelInfoAll findByDate(Map<String,Object> params);
}
