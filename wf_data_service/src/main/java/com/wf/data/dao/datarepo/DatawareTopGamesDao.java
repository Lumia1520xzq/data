package com.wf.data.dao.datarepo;


import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareTopGames;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "dataware_top_games")
public interface DatawareTopGamesDao extends CrudDao<DatawareTopGames> {

    List<DatawareTopGames> getTop2Games(@Param("businessDate") String businessDate);
}
