package com.wf.data.dao.taurus.entity;


import com.wf.core.persistence.DataEntity;

public class TaurusUserBettingLog extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long deskId;
	private Integer phase;
	private Long userId;
	private Integer bettingType;
	private Long amount;
	private Long returnAmount;
	private Integer resultStatus;
	private Integer status;
	private Long channelId;

	public Long getDeskId() {
		return deskId;
	}

	public void setDeskId(Long deskId) {
		this.deskId = deskId;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getBettingType() {
		return bettingType;
	}

	public void setBettingType(Integer bettingType) {
		this.bettingType = bettingType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Long returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Integer getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(Integer resultStatus) {
		this.resultStatus = resultStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}