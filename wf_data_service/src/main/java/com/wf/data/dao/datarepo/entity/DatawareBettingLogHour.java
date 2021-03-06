package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

import java.util.List;

public class DatawareBettingLogHour extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Integer gameType;
	private String gameName;
	private Integer bettingCount;
	private Double bettingAmount;
	private Double resultAmount;
	private Integer userGroup;
	private Long channelId;
	private String bettingHour;
	private String bettingDate;
	private Long parentId;
	//投注人数
	private Integer bettingUserCount;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	private List<Long> userList;

	public List<Long> getUserList() {
		return userList;
	}

	public void setUserList(List<Long> userList) {
		this.userList = userList;
	}

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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Integer bettingCount) {
		this.bettingCount = bettingCount;
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

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getBettingHour() {
		return bettingHour;
	}

	public void setBettingHour(String bettingHour) {
		this.bettingHour = bettingHour;
	}

	public String getBettingDate() {
		return bettingDate;
	}

	public void setBettingDate(String bettingDate) {
		this.bettingDate = bettingDate;
	}

	public Integer getBettingUserCount() {
		return bettingUserCount;
	}

	public void setBettingUserCount(Integer bettingUserCount) {
		this.bettingUserCount = bettingUserCount;
	}

	@Override
	public String toString() {
		return "DatawareBettingLogHour{" +
				"userId=" + userId +
				", gameType=" + gameType +
				", gameName='" + gameName + '\'' +
				", bettingCount=" + bettingCount +
				", bettingAmount=" + bettingAmount +
				", resultAmount=" + resultAmount +
				", userGroup=" + userGroup +
				", channelId=" + channelId +
				", bettingHour='" + bettingHour + '\'' +
				", bettingDate='" + bettingDate + '\'' +
				", parentId=" + parentId +
				", bettingUserCount=" + bettingUserCount +
				", userList=" + userList +
				'}';
	}
}