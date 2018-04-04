package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalGameInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long parentId;
	private Long channelId;
	private Integer gameType;
	private Long dau;
	private Long bettingUserCount;
	private Double bettingConversion;
	private Double bettingAmount;
	private Double returnAmount;
	private Double diffAmount;
	private Double returnRate;
	private Long bettingCount;
	private Double bettingArpu;
	private Double bettingAsp;
	private Double oneDayRetention;
	private Double threeDayRetention;
	private Double sevenDayRetention;
	private Long newUserCount;
	private Long newUserBettingUserCount;
	private Double newUserBettingConversion;
	private Double newUserReturnAmount;
	private Double newUserBettingAmount;
	private Double newUserDiffAmount;
	private Double newUserReturnRate;
	private Long newUserBettingCount;
	private Double newUserBettingArpu;
	private Double newUserBettingAsp;
	private Double newUserOneDayRetention;
	private Double newUserThreeDayRetention;
	private Double newUserSevenDayRetention;
	private Double importRate;
	private Long totalUserCount;
	private String businessDate;


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

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public Long getDau() {
		return dau;
	}

	public void setDau(Long dau) {
		this.dau = dau;
	}

	public Long getBettingUserCount() {
		return bettingUserCount;
	}

	public void setBettingUserCount(Long bettingUserCount) {
		this.bettingUserCount = bettingUserCount;
	}

	public Double getBettingConversion() {
		return bettingConversion;
	}

	public void setBettingConversion(Double bettingConversion) {
		this.bettingConversion = bettingConversion;
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

	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public Double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}

	public Long getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Long bettingCount) {
		this.bettingCount = bettingCount;
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

	public Double getOneDayRetention() {
		return oneDayRetention;
	}

	public void setOneDayRetention(Double oneDayRetention) {
		this.oneDayRetention = oneDayRetention;
	}

	public Double getThreeDayRetention() {
		return threeDayRetention;
	}

	public void setThreeDayRetention(Double threeDayRetention) {
		this.threeDayRetention = threeDayRetention;
	}

	public Double getSevenDayRetention() {
		return sevenDayRetention;
	}

	public void setSevenDayRetention(Double sevenDayRetention) {
		this.sevenDayRetention = sevenDayRetention;
	}

	public Long getNewUserCount() {
		return newUserCount;
	}

	public void setNewUserCount(Long newUserCount) {
		this.newUserCount = newUserCount;
	}

	public Long getNewUserBettingUserCount() {
		return newUserBettingUserCount;
	}

	public void setNewUserBettingUserCount(Long newUserBettingUserCount) {
		this.newUserBettingUserCount = newUserBettingUserCount;
	}

	public Double getNewUserBettingConversion() {
		return newUserBettingConversion;
	}

	public void setNewUserBettingConversion(Double newUserBettingConversion) {
		this.newUserBettingConversion = newUserBettingConversion;
	}

	public Double getNewUserReturnAmount() {
		return newUserReturnAmount;
	}

	public void setNewUserReturnAmount(Double newUserReturnAmount) {
		this.newUserReturnAmount = newUserReturnAmount;
	}

	public Double getNewUserBettingAmount() {
		return newUserBettingAmount;
	}

	public void setNewUserBettingAmount(Double newUserBettingAmount) {
		this.newUserBettingAmount = newUserBettingAmount;
	}

	public Double getNewUserDiffAmount() {
		return newUserDiffAmount;
	}

	public void setNewUserDiffAmount(Double newUserDiffAmount) {
		this.newUserDiffAmount = newUserDiffAmount;
	}

	public Double getNewUserReturnRate() {
		return newUserReturnRate;
	}

	public void setNewUserReturnRate(Double newUserReturnRate) {
		this.newUserReturnRate = newUserReturnRate;
	}

	public Long getNewUserBettingCount() {
		return newUserBettingCount;
	}

	public void setNewUserBettingCount(Long newUserBettingCount) {
		this.newUserBettingCount = newUserBettingCount;
	}

	public Double getNewUserBettingArpu() {
		return newUserBettingArpu;
	}

	public void setNewUserBettingArpu(Double newUserBettingArpu) {
		this.newUserBettingArpu = newUserBettingArpu;
	}

	public Double getNewUserBettingAsp() {
		return newUserBettingAsp;
	}

	public void setNewUserBettingAsp(Double newUserBettingAsp) {
		this.newUserBettingAsp = newUserBettingAsp;
	}

	public Double getNewUserOneDayRetention() {
		return newUserOneDayRetention;
	}

	public void setNewUserOneDayRetention(Double newUserOneDayRetention) {
		this.newUserOneDayRetention = newUserOneDayRetention;
	}

	public Double getNewUserThreeDayRetention() {
		return newUserThreeDayRetention;
	}

	public void setNewUserThreeDayRetention(Double newUserThreeDayRetention) {
		this.newUserThreeDayRetention = newUserThreeDayRetention;
	}

	public Double getNewUserSevenDayRetention() {
		return newUserSevenDayRetention;
	}

	public void setNewUserSevenDayRetention(Double newUserSevenDayRetention) {
		this.newUserSevenDayRetention = newUserSevenDayRetention;
	}

	public Double getImportRate() {
		return importRate;
	}

	public void setImportRate(Double importRate) {
		this.importRate = importRate;
	}

	public Long getTotalUserCount() {
		return totalUserCount;
	}

	public void setTotalUserCount(Long totalUserCount) {
		this.totalUserCount = totalUserCount;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
}