package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalChannelRetention extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long dau;
	private Long newUsers;
	private Double usersRate;
	private Double dayRetention;
	private Double usersDayRetention;
	private String channelName;
	private Long parentId;
	private Long channelId;
	private String businessDate;


	public Long getDau() {
		return dau;
	}

	public void setDau(Long dau) {
		this.dau = dau;
	}

	public Long getNewUsers() {
		return newUsers;
	}

	public void setNewUsers(Long newUsers) {
		this.newUsers = newUsers;
	}

	public Double getUsersRate() {
		return usersRate;
	}

	public void setUsersRate(Double usersRate) {
		this.usersRate = usersRate;
	}

	public Double getDayRetention() {
		return dayRetention;
	}

	public void setDayRetention(Double dayRetention) {
		this.dayRetention = dayRetention;
	}

	public Double getUsersDayRetention() {
		return usersDayRetention;
	}

	public void setUsersDayRetention(Double usersDayRetention) {
		this.usersDayRetention = usersDayRetention;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
}