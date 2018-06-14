package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointDay;

import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "dataware_burying_point_day")
public interface DatawareBuryingPointDayDao extends CrudDao<DatawareBuryingPointDay> {

    long getCountByTime(Map<String, Object> map);

    Integer getGameDau(Map<String, Object> map);

    Long getDauByChannel(Map<String, Object> map);

    List<Long> getUserIdListByChannel(Map<String, Object> map);

    List<Long> getHistoryDauIds(Map<String, Object> map);

    List<Long> getGameDauIds(Map<String, Object> map);

    int deleteByDate(Map<String, Object> params);

    String getFirstActiveDate(Map<String, Object> userParam);

    String getLastActiveDate(Map<String, Object> userParam);

    Integer getActiveDatesByUser(Map<String, Object> userParam);

    long getUserPointCount(Map<String, Object> userParam);

    List<Long> getUserIdByDates(Map<String, Object> map);

    List<Long> getActiveUsersInFifteenDay(Map<String, Object> activeUserParam);

    String getMinActiveDate(Map<String, Object> params);

    List<Long> getGameNewUserList(Map<String, Object> newUserParam);

    List<Long> getNotActiveUsersByDateAndChannel(Map<String, Object> map);

    List<Long> getOldPredictionLostUsers(Map<String, Object> map);
}
