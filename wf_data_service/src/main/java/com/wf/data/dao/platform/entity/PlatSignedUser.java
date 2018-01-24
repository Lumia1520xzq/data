package com.wf.data.dao.platform.entity;


import com.wf.core.persistence.DataEntity;

public class PlatSignedUser extends DataEntity {
	private static final long serialVersionUID = -1;
	private Integer days;
	private Long userId;
	private Long amount;
	private Integer status;
	private Long channelId;

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
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