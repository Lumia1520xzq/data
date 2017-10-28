package com.wf.uic.dao.entity.mycat;


import com.wf.core.persistence.DataEntity;

public class UicBuryingPoint extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Integer buryingType;
	private Integer gameType;
	private Long channelId;
	private Integer countNum;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getBuryingType() {
		return buryingType;
	}

	public void setBuryingType(Integer buryingType) {
		this.buryingType = buryingType;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getCountNum() {
		return countNum;
	}

	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}
	
	
	
}