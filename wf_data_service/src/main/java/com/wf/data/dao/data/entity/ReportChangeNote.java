package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class ReportChangeNote extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long gameType;
	private Long channelId;
	private Long groupId;
	private Double bettingAmount;
	private Double resultAmount;


	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGameType() {
		return gameType;
	}
	public void setGameType(Long gameType) {
		this.gameType = gameType;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Double getBettingAmount() {
		return bettingAmount;
	}
	public void setBettingAmount(Double bettingAmount) {
		this.bettingAmount = bettingAmount;
	}
	public Double getResultAmount() {
		return resultAmount;
	}
	public void setResultAmount(Double resultAmount) {
		this.resultAmount = resultAmount;
	}
	
}