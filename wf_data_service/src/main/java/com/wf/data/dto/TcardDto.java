package com.wf.data.dto;

import java.io.Serializable;

public class TcardDto implements Serializable{
	
	private static final long serialVersionUID = -1;
	
	private Long parentId;
	private Long channelId;
	private String startTime;
	private String endTime;

	private String searchDate;
	private String channelName;
	private Integer dauCount;
	private Integer userCount;
	private Double bettingAmount;
	private Double resultAmount;
	private Double amountDiff;
	private Double tableAmount;
	private String conversionRate;
	private String returnRate1;
	private String returnRate2;
	private Double bettingArpu;
	private Double bettingAsp;

	private Integer lowBettingUser;
	private Integer midBettingUser;
	private Integer highBettingUser;
	private Double lowTableFee;
	private Double midTableFee;
	private Double highTableFee;
	private Integer lowTables;
	private Integer midTables;
	private Integer highTables;
	private Double lowAvgRounds;
	private Double midAvgRounds;
	private Double highAvgRounds;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getDauCount() {
		return dauCount;
	}

	public void setDauCount(Integer dauCount) {
		this.dauCount = dauCount;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
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

	public Double getAmountDiff() {
		return amountDiff;
	}

	public void setAmountDiff(Double amountDiff) {
		this.amountDiff = amountDiff;
	}

	public Double getTableAmount() {
		return tableAmount;
	}

	public void setTableAmount(Double tableAmount) {
		this.tableAmount = tableAmount;
	}

	public String getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getReturnRate1() {
		return returnRate1;
	}

	public void setReturnRate1(String returnRate1) {
		this.returnRate1 = returnRate1;
	}

	public String getReturnRate2() {
		return returnRate2;
	}

	public void setReturnRate2(String returnRate2) {
		this.returnRate2 = returnRate2;
	}

	public Double getBettingArpu() {
		return bettingArpu;
	}

	public void setBettingArpu(Double bettingArpu) {
		this.bettingArpu = bettingArpu;
	}

	public Double getBettingAsp() {
		return bettingAsp;
	}

	public void setBettingAsp(Double bettingAsp) {
		this.bettingAsp = bettingAsp;
	}

	public Integer getLowBettingUser() {
		return lowBettingUser;
	}

	public void setLowBettingUser(Integer lowBettingUser) {
		this.lowBettingUser = lowBettingUser;
	}

	public Integer getMidBettingUser() {
		return midBettingUser;
	}

	public void setMidBettingUser(Integer midBettingUser) {
		this.midBettingUser = midBettingUser;
	}

	public Integer getHighBettingUser() {
		return highBettingUser;
	}

	public void setHighBettingUser(Integer highBettingUser) {
		this.highBettingUser = highBettingUser;
	}

	public Double getLowTableFee() {
		return lowTableFee;
	}

	public void setLowTableFee(Double lowTableFee) {
		this.lowTableFee = lowTableFee;
	}

	public Double getMidTableFee() {
		return midTableFee;
	}

	public void setMidTableFee(Double midTableFee) {
		this.midTableFee = midTableFee;
	}

	public Double getHighTableFee() {
		return highTableFee;
	}

	public void setHighTableFee(Double highTableFee) {
		this.highTableFee = highTableFee;
	}

	public Integer getLowTables() {
		return lowTables;
	}

	public void setLowTables(Integer lowTables) {
		this.lowTables = lowTables;
	}

	public Integer getMidTables() {
		return midTables;
	}

	public void setMidTables(Integer midTables) {
		this.midTables = midTables;
	}

	public Integer getHighTables() {
		return highTables;
	}

	public void setHighTables(Integer highTables) {
		this.highTables = highTables;
	}

	public Double getLowAvgRounds() {
		return lowAvgRounds;
	}

	public void setLowAvgRounds(Double lowAvgRounds) {
		this.lowAvgRounds = lowAvgRounds;
	}

	public Double getMidAvgRounds() {
		return midAvgRounds;
	}

	public void setMidAvgRounds(Double midAvgRounds) {
		this.midAvgRounds = midAvgRounds;
	}

	public Double getHighAvgRounds() {
		return highAvgRounds;
	}

	public void setHighAvgRounds(Double highAvgRounds) {
		this.highAvgRounds = highAvgRounds;
	}
}