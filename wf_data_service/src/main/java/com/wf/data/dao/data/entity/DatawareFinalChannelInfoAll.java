package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalChannelInfoAll extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long dau;
	private Double rechargeAmount;
	private Long rechargeCount;
	private Long newUsers;
	private Long userCount;
	private Long bettingCount;
	private Double bettingAmount;
	private Double resultAmount;
	private Double resultRate;
	private Double bettingRate;
	private Double dauPayRate;
	private Double bettingPayRate;
	private Double userBettingRate;
	private Double payArpu;
	private Double payArppu;
	private String channelName;
	private Long parentId;
	private Long channelId;
	private String businessDate;
	private Long userBettingCount;
	private Double usersDayRetention;
	private Double dayRetention;
	private Double usersRate;
	private Double totalCost;
	private Double costRate;

	public Long getUserBettingCount() {
		return userBettingCount;
	}

	public void setUserBettingCount(Long userBettingCount) {
		this.userBettingCount = userBettingCount;
	}

	public Long getDau() {
		return dau;
	}

	public void setDau(Long dau) {
		this.dau = dau;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Long getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Long rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public Long getNewUsers() {
		return newUsers;
	}

	public void setNewUsers(Long newUsers) {
		this.newUsers = newUsers;
	}

	public Long getUserCount() {
		return userCount;
	}

	public void setUserCount(Long userCount) {
		this.userCount = userCount;
	}

	public Long getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Long bettingCount) {
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

	public Double getResultRate() {
		return resultRate;
	}

	public void setResultRate(Double resultRate) {
		this.resultRate = resultRate;
	}

	public Double getBettingRate() {
		return bettingRate;
	}

	public void setBettingRate(Double bettingRate) {
		this.bettingRate = bettingRate;
	}

	public Double getDauPayRate() {
		return dauPayRate;
	}

	public void setDauPayRate(Double dauPayRate) {
		this.dauPayRate = dauPayRate;
	}

	public Double getBettingPayRate() {
		return bettingPayRate;
	}

	public void setBettingPayRate(Double bettingPayRate) {
		this.bettingPayRate = bettingPayRate;
	}

	public Double getUserBettingRate() {
		return userBettingRate;
	}

	public void setUserBettingRate(Double userBettingRate) {
		this.userBettingRate = userBettingRate;
	}

	public Double getPayArpu() {
		return payArpu;
	}

	public void setPayArpu(Double payArpu) {
		this.payArpu = payArpu;
	}

	public Double getPayArppu() {
		return payArppu;
	}

	public void setPayArppu(Double payArppu) {
		this.payArppu = payArppu;
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

	public Double getUsersDayRetention() {
		return usersDayRetention;
	}

	public void setUsersDayRetention(Double usersDayRetention) {
		this.usersDayRetention = usersDayRetention;
	}

	public Double getDayRetention() {
		return dayRetention;
	}

	public void setDayRetention(Double dayRetention) {
		this.dayRetention = dayRetention;
	}

	public Double getUsersRate() {
		return usersRate;
	}

	public void setUsersRate(Double usersRate) {
		this.usersRate = usersRate;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}
}