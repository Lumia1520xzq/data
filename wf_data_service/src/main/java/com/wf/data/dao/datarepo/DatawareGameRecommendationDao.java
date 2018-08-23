package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareGameRecommendation;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "dataware_game_recommendation")
public interface DatawareGameRecommendationDao extends CrudDao<DatawareGameRecommendation> {

    List<DatawareGameRecommendation> getRecommendationGameIdByUser
            (@Param("userId") long userId,
             @Param("businessDate") String businessDate);
}
