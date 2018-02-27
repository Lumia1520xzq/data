package com.wf.data.dao.datarepo.entity;


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
	private Double sevenRetention;

	private String dayDauRate;
	private String weekDauRate;

	private String dayRechargeAmountRate;
	private String weekRechargeAmountRate;

	private String dayRechargeCountRate;
	private String weekRechargeCountRate;


	private String dayNewUsersRate;
	private String weekNewUsersRate;


	private String dayUserCountRate;
	private String weekUserCountRate;



	private String dayBettingRate;
	private String weekBettingRate;

	private String dayDauPayRate;
	private String weekDauPayRate;


	private String dayBettingPayRate;
	private String weekBettingPayRate;


	private String dayUserBettingRate;
	private String weekUserBettingRate;


	private String dayBettingAmountRate;
	private String weekBettingAmountRate;


	private String dayResultRate;
	private String weekResultRate;


	private String dayPayArpuRate;
	private String weekPayArpuRate;
	private String dayPayArppuRate;
	private String weekPayArppuRate;

	private String dayUsersDayRetentionRate;
	private String weekUsersDayRetentionRate;
	private String dayDayRetentionRate;
	private String weekDayRetentionRate;
	private String dayUsersRate;
	private String weekUsersRate;

	private String dayTotalCost;
	private String weekTotalCost;
	private String dayCostRate;
	private String weekCostRate;

	private String daySevenRetentionRate;
	private String weekSevenRetentionRate;

	private Long hisRegistered;
	private Double hisRecharge;
	private Double userLtv;

	public void init(DatawareFinalChannelInfoAll info) {
		if(null == info){
			return;
		}
		info.setBettingAmount(0.0);
		info.setResultAmount(0.0);
	}

	public Long getHisRegistered() {
		return hisRegistered;
	}

	public void setHisRegistered(Long hisRegistered) {
		this.hisRegistered = hisRegistered;
	}

	public Double getHisRecharge() {
		return hisRecharge;
	}

	public void setHisRecharge(Double hisRecharge) {
		this.hisRecharge = hisRecharge;
	}

	public Double getUserLtv() {
		return userLtv;
	}

	public void setUserLtv(Double userLtv) {
		this.userLtv = userLtv;
	}

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

	public String getDayDauRate() {
		return dayDauRate;
	}

	public void setDayDauRate(String dayDauRate) {
		this.dayDauRate = dayDauRate;
	}

	public String getWeekDauRate() {
		return weekDauRate;
	}

	public void setWeekDauRate(String weekDauRate) {
		this.weekDauRate = weekDauRate;
	}

	public String getDayRechargeAmountRate() {
		return dayRechargeAmountRate;
	}

	public void setDayRechargeAmountRate(String dayRechargeAmountRate) {
		this.dayRechargeAmountRate = dayRechargeAmountRate;
	}

	public String getWeekRechargeAmountRate() {
		return weekRechargeAmountRate;
	}

	public void setWeekRechargeAmountRate(String weekRechargeAmountRate) {
		this.weekRechargeAmountRate = weekRechargeAmountRate;
	}

	public String getDayRechargeCountRate() {
		return dayRechargeCountRate;
	}

	public void setDayRechargeCountRate(String dayRechargeCountRate) {
		this.dayRechargeCountRate = dayRechargeCountRate;
	}

	public String getWeekRechargeCountRate() {
		return weekRechargeCountRate;
	}

	public void setWeekRechargeCountRate(String weekRechargeCountRate) {
		this.weekRechargeCountRate = weekRechargeCountRate;
	}

	public String getDayNewUsersRate() {
		return dayNewUsersRate;
	}

	public void setDayNewUsersRate(String dayNewUsersRate) {
		this.dayNewUsersRate = dayNewUsersRate;
	}

	public String getWeekNewUsersRate() {
		return weekNewUsersRate;
	}

	public void setWeekNewUsersRate(String weekNewUsersRate) {
		this.weekNewUsersRate = weekNewUsersRate;
	}

	public String getDayUserCountRate() {
		return dayUserCountRate;
	}

	public void setDayUserCountRate(String dayUserCountRate) {
		this.dayUserCountRate = dayUserCountRate;
	}

	public String getWeekUserCountRate() {
		return weekUserCountRate;
	}

	public void setWeekUserCountRate(String weekUserCountRate) {
		this.weekUserCountRate = weekUserCountRate;
	}

	public String getDayBettingRate() {
		return dayBettingRate;
	}

	public void setDayBettingRate(String dayBettingRate) {
		this.dayBettingRate = dayBettingRate;
	}

	public String getWeekBettingRate() {
		return weekBettingRate;
	}

	public void setWeekBettingRate(String weekBettingRate) {
		this.weekBettingRate = weekBettingRate;
	}

	public String getDayDauPayRate() {
		return dayDauPayRate;
	}

	public void setDayDauPayRate(String dayDauPayRate) {
		this.dayDauPayRate = dayDauPayRate;
	}

	public String getWeekDauPayRate() {
		return weekDauPayRate;
	}

	public void setWeekDauPayRate(String weekDauPayRate) {
		this.weekDauPayRate = weekDauPayRate;
	}

	public String getDayBettingPayRate() {
		return dayBettingPayRate;
	}

	public void setDayBettingPayRate(String dayBettingPayRate) {
		this.dayBettingPayRate = dayBettingPayRate;
	}

	public String getWeekBettingPayRate() {
		return weekBettingPayRate;
	}

	public void setWeekBettingPayRate(String weekBettingPayRate) {
		this.weekBettingPayRate = weekBettingPayRate;
	}

	public String getDayUserBettingRate() {
		return dayUserBettingRate;
	}

	public void setDayUserBettingRate(String dayUserBettingRate) {
		this.dayUserBettingRate = dayUserBettingRate;
	}

	public String getWeekUserBettingRate() {
		return weekUserBettingRate;
	}

	public void setWeekUserBettingRate(String weekUserBettingRate) {
		this.weekUserBettingRate = weekUserBettingRate;
	}

	public String getDayBettingAmountRate() {
		return dayBettingAmountRate;
	}

	public void setDayBettingAmountRate(String dayBettingAmountRate) {
		this.dayBettingAmountRate = dayBettingAmountRate;
	}

	public String getWeekBettingAmountRate() {
		return weekBettingAmountRate;
	}

	public void setWeekBettingAmountRate(String weekBettingAmountRate) {
		this.weekBettingAmountRate = weekBettingAmountRate;
	}

	public String getDayResultRate() {
		return dayResultRate;
	}

	public void setDayResultRate(String dayResultRate) {
		this.dayResultRate = dayResultRate;
	}

	public String getWeekResultRate() {
		return weekResultRate;
	}

	public void setWeekResultRate(String weekResultRate) {
		this.weekResultRate = weekResultRate;
	}

	public String getDayPayArpuRate() {
		return dayPayArpuRate;
	}

	public void setDayPayArpuRate(String dayPayArpuRate) {
		this.dayPayArpuRate = dayPayArpuRate;
	}

	public String getWeekPayArpuRate() {
		return weekPayArpuRate;
	}

	public void setWeekPayArpuRate(String weekPayArpuRate) {
		this.weekPayArpuRate = weekPayArpuRate;
	}

	public String getDayPayArppuRate() {
		return dayPayArppuRate;
	}

	public void setDayPayArppuRate(String dayPayArppuRate) {
		this.dayPayArppuRate = dayPayArppuRate;
	}

	public String getWeekPayArppuRate() {
		return weekPayArppuRate;
	}

	public void setWeekPayArppuRate(String weekPayArppuRate) {
		this.weekPayArppuRate = weekPayArppuRate;
	}

	public String getDayUsersDayRetentionRate() {
		return dayUsersDayRetentionRate;
	}

	public void setDayUsersDayRetentionRate(String dayUsersDayRetentionRate) {
		this.dayUsersDayRetentionRate = dayUsersDayRetentionRate;
	}

	public String getWeekUsersDayRetentionRate() {
		return weekUsersDayRetentionRate;
	}

	public void setWeekUsersDayRetentionRate(String weekUsersDayRetentionRate) {
		this.weekUsersDayRetentionRate = weekUsersDayRetentionRate;
	}

	public String getDayDayRetentionRate() {
		return dayDayRetentionRate;
	}

	public void setDayDayRetentionRate(String dayDayRetentionRate) {
		this.dayDayRetentionRate = dayDayRetentionRate;
	}

	public String getWeekDayRetentionRate() {
		return weekDayRetentionRate;
	}

	public void setWeekDayRetentionRate(String weekDayRetentionRate) {
		this.weekDayRetentionRate = weekDayRetentionRate;
	}

	public String getDayUsersRate() {
		return dayUsersRate;
	}

	public void setDayUsersRate(String dayUsersRate) {
		this.dayUsersRate = dayUsersRate;
	}

	public String getWeekUsersRate() {
		return weekUsersRate;
	}

	public void setWeekUsersRate(String weekUsersRate) {
		this.weekUsersRate = weekUsersRate;
	}

	public String getDayTotalCost() {
		return dayTotalCost;
	}

	public void setDayTotalCost(String dayTotalCost) {
		this.dayTotalCost = dayTotalCost;
	}

	public String getWeekTotalCost() {
		return weekTotalCost;
	}

	public void setWeekTotalCost(String weekTotalCost) {
		this.weekTotalCost = weekTotalCost;
	}

	public String getDayCostRate() {
		return dayCostRate;
	}

	public void setDayCostRate(String dayCostRate) {
		this.dayCostRate = dayCostRate;
	}

	public String getWeekCostRate() {
		return weekCostRate;
	}

	public void setWeekCostRate(String weekCostRate) {
		this.weekCostRate = weekCostRate;
	}

	public Double getSevenRetention() {
		return sevenRetention;
	}

	public void setSevenRetention(Double sevenRetention) {
		this.sevenRetention = sevenRetention;
	}

	public String getDaySevenRetentionRate() {
		return daySevenRetentionRate;
	}

	public void setDaySevenRetentionRate(String daySevenRetentionRate) {
		this.daySevenRetentionRate = daySevenRetentionRate;
	}

	public String getWeekSevenRetentionRate() {
		return weekSevenRetentionRate;
	}

	public void setWeekSevenRetentionRate(String weekSevenRetentionRate) {
		this.weekSevenRetentionRate = weekSevenRetentionRate;
	}
}