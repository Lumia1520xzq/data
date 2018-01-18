package com.wf.data.dto;

import java.io.Serializable;

/**
 * 游戏数据日报表dto
 * @author JoeH
 */
public class DailyReportDto implements Serializable{

	private static final long serialVersionUID = -5143124113247873284L;
	private Long parentId;
	private String channelName;
	private String startTime;
	private String endTime;
	private String gameName;

	private Integer gameType;
	private String searchDate;
	private Integer dau;
	private String importRate;
	private Integer bettingUserCount;
	private Integer bettingCount;
	private Double bettingAmount;
	private Double returnAmount;
	private Double amountGap;
	private String bettingRate;
	private String returnRate;
	private Double arpu;
	private Double asp;
	private Double avgBettingCount;
	private Integer newUserCount;
	private String newUserRate;
	private String newUserRemain;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public Integer getDau() {
		return dau;
	}

	public void setDau(Integer dau) {
		this.dau = dau;
	}

	public String getImportRate() {
		return importRate;
	}

	public void setImportRate(String importRate) {
		this.importRate = importRate;
	}

	public Integer getBettingUserCount() {
		return bettingUserCount;
	}

	public void setBettingUserCount(Integer bettingUserCount) {
		this.bettingUserCount = bettingUserCount;
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

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Double getAmountGap() {
		return amountGap;
	}

	public void setAmountGap(Double amountGap) {
		this.amountGap = amountGap;
	}

	public String getBettingRate() {
		return bettingRate;
	}

	public void setBettingRate(String bettingRate) {
		this.bettingRate = bettingRate;
	}

	public String getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}

	public Double getArpu() {
		return arpu;
	}

	public void setArpu(Double arpu) {
		this.arpu = arpu;
	}

	public Double getAsp() {
		return asp;
	}

	public void setAsp(Double asp) {
		this.asp = asp;
	}

	public Double getAvgBettingCount() {
		return avgBettingCount;
	}

	public void setAvgBettingCount(Double avgBettingCount) {
		this.avgBettingCount = avgBettingCount;
	}

	public Integer getNewUserCount() {
		return newUserCount;
	}

	public void setNewUserCount(Integer newUserCount) {
		this.newUserCount = newUserCount;
	}

	public String getNewUserRate() {
		return newUserRate;
	}

	public void setNewUserRate(String newUserRate) {
		this.newUserRate = newUserRate;
	}

	public String getNewUserRemain() {
		return newUserRemain;
	}

	public void setNewUserRemain(String newUserRemain) {
		this.newUserRemain = newUserRemain;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
}