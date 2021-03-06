package com.wf.data.dao.datarepo;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dto.MonthlyDataDto;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_final_channel_info_all")
public interface DatawareFinalChannelInfoAllDao extends CrudDao<DatawareFinalChannelInfoAll> {

    long getCountByTime(Map<String, Object> map);

    List<DatawareFinalChannelInfoAll> getListByChannelAndDate(Map<String, Object> params);

    DatawareFinalChannelInfoAll findByDate(Map<String, Object> params);

    List<MonthlyDataDto> findMonthSumData(Map<String, Object> params);

    int deleteByDate(Map<String, Object> params);

    int updateUserLtv(Map<String, Object> params);

    DatawareFinalChannelInfoAll getInfoByChannel(Map<String, Object> params);
}
