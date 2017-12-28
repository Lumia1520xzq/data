package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class DatawareUserInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private String nickname;
	private String thirdId;
	private Long channelId;
	private Date registeredTime;
	private String registeredHour;
	private String registeredDate;
	private Integer userGroup;

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Date getRegisteredTime() {
		return registeredTime;
	}

	public void setRegisteredTime(Date registeredTime) {
		this.registeredTime = registeredTime;
	}

	public String getRegisteredHour() {
		return registeredHour;
	}

	public void setRegisteredHour(String registeredHour) {
		this.registeredHour = registeredHour;
	}

	public String getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}
}