package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class DatawareUserInfoExtendGame extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Integer gameType;
	private String firstActiveTime;
	private Integer newUserFlag;
	private Double sumBettingAmount;
	private Long sumBettingCount;
	private Double sevenSumBettingAmount;
	private Long sevenSumBettingCount;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public String getFirstActiveTime() {
		return firstActiveTime;
	}

	public void setFirstActiveTime(String firstActiveTime) {
		this.firstActiveTime = firstActiveTime;
	}

	public Integer getNewUserFlag() {
		return newUserFlag;
	}

	public void setNewUserFlag(Integer newUserFlag) {
		this.newUserFlag = newUserFlag;
	}

	public Double getSumBettingAmount() {
		return sumBettingAmount;
	}

	public void setSumBettingAmount(Double sumBettingAmount) {
		this.sumBettingAmount = sumBettingAmount;
	}

	public Long getSumBettingCount() {
		return sumBettingCount;
	}

	public void setSumBettingCount(Long sumBettingCount) {
		this.sumBettingCount = sumBettingCount;
	}

	public Double getSevenSumBettingAmount() {
		return sevenSumBettingAmount;
	}

	public void setSevenSumBettingAmount(Double sevenSumBettingAmount) {
		this.sevenSumBettingAmount = sevenSumBettingAmount;
	}

	public Long getSevenSumBettingCount() {
		return sevenSumBettingCount;
	}

	public void setSevenSumBettingCount(Long sevenSumBettingCount) {
		this.sevenSumBettingCount = sevenSumBettingCount;
	}
}