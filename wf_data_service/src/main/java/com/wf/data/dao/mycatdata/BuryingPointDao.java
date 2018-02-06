package com.wf.data.dao.mycatdata;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointHour;
import com.wf.data.dao.mycatdata.entity.BuryingPoint;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisDao(tableName = "uic_burying_point")
public interface BuryingPointDao extends CrudDao<BuryingPoint> {
	/**
	 * 用户最近耍的游戏，前2条
	 * @param params
	 * @return
	 */
	List<BuryingPoint> getUserLastPlayGame(Map<Object, Object> params);
	/**
	 * 获取用户指定游戏指定埋点类型的记录
	 * @param gameType
	 * @param buryingType
	 * @param userId
	 * @return
	 */
	BuryingPoint getByGameTypeAndBuryingType(@Param("gameType") Integer gameType, @Param("buryingType") Integer buryingType, @Param("userId") Long userId);

	Date getLastLoginWealTime(@Param("userId") Long userId, @Param("buryingType") int buryingType);

	/**
	 * 获取用户进入某游戏的最后时间
	 * @param userId
	 * @param gameType
	 * @return
	 */
	BuryingPoint findLastGameLoading(@Param("userId") Long userId, @Param("gameType") Integer gameType);
	
	/********************************************************************
	 * 未用userid查询的需要优化的逻辑
	 ******************************************************************** 
	 ********************************************************************/

	List<DatawareBuryingPointHour> findBuryingHourList(Map<String,Object> map);
}
