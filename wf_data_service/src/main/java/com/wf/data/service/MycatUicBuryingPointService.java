package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.MapUtils;
import com.wf.data.dao.entity.mycat.UicBuryingPoint;
import com.wf.data.dao.mycat.MycatUicBuryingPointDao;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MycatUicBuryingPointService extends CrudService<MycatUicBuryingPointDao, UicBuryingPoint> {
	
	@Async
	@Override
	public void save(UicBuryingPoint entity) {
		if(entity != null && entity.getUserId() == null){
			entity.setUserId(0L);
		}
		super.save(entity);
	}
	
	/**
	 * 异步埋点
	 * @param gameType  GameTypeContents
	 * @param buryingType  BuryingPointContents
	 * @param userId
	 * @param channelId
	 */
	@Async
	public void save(Integer gameType, Integer buryingType, Long userId, Long channelId) {
		UicBuryingPoint point = new UicBuryingPoint();
		point.setUserId(userId);
		point.setGameType(gameType);
		point.setBuryingType(buryingType);
		point.setChannelId(channelId);
		super.save(point);
	}
	
	/**
	 * 用户指定渠道最后一次埋点记录
	 * @param userId
	 * @param gameNum
	 * @param channelId
	 * @return
	 */
	public List<UicBuryingPoint> getUserLastPlayGame(Long userId, Integer gameNum, Long channelId ) {
		Calendar calendar = Calendar.getInstance();
		String end = DateUtils.formatDateTime(DateUtils.getDayEndTime(calendar.getTime()));
		calendar.add(Calendar.DATE, -7);
		String begin = DateUtils.formatDateTime(DateUtils.getDayStartTime(calendar.getTime()));
		return dao.getUserLastPlayGame(MapUtils.toMap("userId", userId, "gameNum", gameNum, "begin", begin, "end", end, "channelId", channelId));
	}
	
	/**
	 * 获取该类型的一条埋点记录
	 * @param gameType
	 * @param buryingType
	 * @return
	 */
	public UicBuryingPoint getByGameTypeAndBuryingType(Integer gameType, Integer buryingType, Long userId) {
		return dao.getByGameTypeAndBuryingType(gameType, buryingType, userId);
	}
	
	/**
	 * 获取该类型的一条埋点记录
	 * @param userId
	 * @param gameType
	 * @return
	 */
	public UicBuryingPoint findLastGameLoading(Long userId, Integer gameType) {
		return dao.findLastGameLoading(userId,gameType);
	}
	
	/**
	 * 指定用户指定埋点类型最后一次埋点时间
	 * @param userId
	 * @param buryingType
	 * @return
	 */
	public Date getLastLoginWealTime(Long userId, int buryingType) {
		return dao.getLastLoginWealTime(userId, buryingType);
	}
}
