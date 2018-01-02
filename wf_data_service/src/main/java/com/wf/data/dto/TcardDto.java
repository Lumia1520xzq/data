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
}