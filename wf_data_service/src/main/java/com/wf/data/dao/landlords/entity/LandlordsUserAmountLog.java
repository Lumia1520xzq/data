package com.wf.data.dao.landlords.entity;


import com.wf.core.persistence.DataEntity;

public class LandlordsUserAmountLog extends DataEntity {
	private static final long serialVersionUID = -1;
	private Integer deskType;
	private Long deskId;
	private Integer phase;
	private Long userId;
	private Integer type;
	private Long amount;
	private Long returnAmount;
	private Long channelId;
	private Integer status;

	public Integer getDeskType() {
		return deskType;
	}

	public void setDeskType(Integer deskType) {
		this.deskType = deskType;
	}

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}