package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalChannelConversion extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long registeredCount;
	private Long dauCount;
	private Long dauRegistered;
	private Long dauOlder;
	private Long gamedauCount;
	private Long gamedauRegistered;
	private Long gamedauOlder;
	private Long bettingCount;
	private Long bettingRegistered;
	private Long bettingOlder;
	private Long rechargeCount;
	private Long rechargeRegistered;
	private Long rechargeOlder;
	private Double payRate;
	private Double payRegisteredRate;
	private Double payOlderRate;
	private Double dauRegisteredRate;
	private Double gamedauRate;
	private Double gamedauRegisteredRate;
	private Double gamedauOlderRate;
	private Double bettingRate;
	private Double bettingRegisteredRate;
	private Double bettingOlderRate;
	private Double rechargeRate;
	private Double rechargeRegisteredRate;
	private Double rechargeOlderRate;
	private Double registeredRate;
	private String channelName;
	private Long parentId;
	private Long channelId;
	private String businessDate;

	public Long getRegisteredCount() {
		return registeredCount;
	}

	public void setRegisteredCount(Long registeredCount) {
		this.registeredCount = registeredCount;
	}

	public Long getDauCount() {
		return dauCount;
	}

	public void setDauCount(Long dauCount) {
		this.dauCount = dauCount;
	}

	public Long getDauRegistered() {
		return dauRegistered;
	}

	public void setDauRegistered(Long dauRegistered) {
		this.dauRegistered = dauRegistered;
	}

	public Long getDauOlder() {
		return dauOlder;
	}

	public void setDauOlder(Long dauOlder) {
		this.dauOlder = dauOlder;
	}

	public Long getGamedauCount() {
		return gamedauCount;
	}

	public void setGamedauCount(Long gamedauCount) {
		this.gamedauCount = gamedauCount;
	}

	public Long getGamedauRegistered() {
		return gamedauRegistered;
	}

	public void setGamedauRegistered(Long gamedauRegistered) {
		this.gamedauRegistered = gamedauRegistered;
	}

	public Long getGamedauOlder() {
		return gamedauOlder;
	}

	public void setGamedauOlder(Long gamedauOlder) {
		this.gamedauOlder = gamedauOlder;
	}

	public Long getBettingCount() {
		return bettingCount;
	}

	public void setBettingCount(Long bettingCount) {
		this.bettingCount = bettingCount;
	}

	public Long getBettingRegistered() {
		return bettingRegistered;
	}

	public void setBettingRegistered(Long bettingRegistered) {
		this.bettingRegistered = bettingRegistered;
	}

	public Long getBettingOlder() {
		return bettingOlder;
	}

	public void setBettingOlder(Long bettingOlder) {
		this.bettingOlder = bettingOlder;
	}

	public Long getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Long rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public Long getRechargeRegistered() {
		return rechargeRegistered;
	}

	public void setRechargeRegistered(Long rechargeRegistered) {
		this.rechargeRegistered = rechargeRegistered;
	}

	public Long getRechargeOlder() {
		return rechargeOlder;
	}

	public void setRechargeOlder(Long rechargeOlder) {
		this.rechargeOlder = rechargeOlder;
	}

	public Double getPayRate() {
		return payRate;
	}

	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}

	public Double getPayRegisteredRate() {
		return payRegisteredRate;
	}

	public void setPayRegisteredRate(Double payRegisteredRate) {
		this.payRegisteredRate = payRegisteredRate;
	}

	public Double getPayOlderRate() {
		return payOlderRate;
	}

	public void setPayOlderRate(Double payOlderRate) {
		this.payOlderRate = payOlderRate;
	}

	public Double getDauRegisteredRate() {
		return dauRegisteredRate;
	}

	public void setDauRegisteredRate(Double dauRegisteredRate) {
		this.dauRegisteredRate = dauRegisteredRate;
	}

	public Double getGamedauRate() {
		return gamedauRate;
	}

	public void setGamedauRate(Double gamedauRate) {
		this.gamedauRate = gamedauRate;
	}

	public Double getGamedauRegisteredRate() {
		return gamedauRegisteredRate;
	}

	public void setGamedauRegisteredRate(Double gamedauRegisteredRate) {
		this.gamedauRegisteredRate = gamedauRegisteredRate;
	}

	public Double getGamedauOlderRate() {
		return gamedauOlderRate;
	}

	public void setGamedauOlderRate(Double gamedauOlderRate) {
		this.gamedauOlderRate = gamedauOlderRate;
	}

	public Double getBettingRate() {
		return bettingRate;
	}

	public void setBettingRate(Double bettingRate) {
		this.bettingRate = bettingRate;
	}

	public Double getBettingRegisteredRate() {
		return bettingRegisteredRate;
	}

	public void setBettingRegisteredRate(Double bettingRegisteredRate) {
		this.bettingRegisteredRate = bettingRegisteredRate;
	}

	public Double getBettingOlderRate() {
		return bettingOlderRate;
	}

	public void setBettingOlderRate(Double bettingOlderRate) {
		this.bettingOlderRate = bettingOlderRate;
	}

	public Double getRechargeRate() {
		return rechargeRate;
	}

	public void setRechargeRate(Double rechargeRate) {
		this.rechargeRate = rechargeRate;
	}

	public Double getRechargeRegisteredRate() {
		return rechargeRegisteredRate;
	}

	public void setRechargeRegisteredRate(Double rechargeRegisteredRate) {
		this.rechargeRegisteredRate = rechargeRegisteredRate;
	}

	public Double getRechargeOlderRate() {
		return rechargeOlderRate;
	}

	public void setRechargeOlderRate(Double rechargeOlderRate) {
		this.rechargeOlderRate = rechargeOlderRate;
	}

	public Double getRegisteredRate() {
		return registeredRate;
	}

	public void setRegisteredRate(Double registeredRate) {
		this.registeredRate = registeredRate;
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

	@Override
	public String toString() {
		return "DatawareFinalChannelConversion{" +
				"registeredCount=" + registeredCount +
				", dauCount=" + dauCount +
				", dauRegistered=" + dauRegistered +
				", dauOlder=" + dauOlder +
				", gamedauCount=" + gamedauCount +
				", gamedauRegistered=" + gamedauRegistered +
				", gamedauOlder=" + gamedauOlder +
				", bettingCount=" + bettingCount +
				", bettingRegistered=" + bettingRegistered +
				", bettingOlder=" + bettingOlder +
				", rechargeCount=" + rechargeCount +
				", rechargeRegistered=" + rechargeRegistered +
				", rechargeOlder=" + rechargeOlder +
				", payRate=" + payRate +
				", payRegisteredRate=" + payRegisteredRate +
				", payOlderRate=" + payOlderRate +
				", dauRegisteredRate=" + dauRegisteredRate +
				", gamedauRate=" + gamedauRate +
				", gamedauRegisteredRate=" + gamedauRegisteredRate +
				", gamedauOlderRate=" + gamedauOlderRate +
				", bettingRate=" + bettingRate +
				", bettingRegisteredRate=" + bettingRegisteredRate +
				", bettingOlderRate=" + bettingOlderRate +
				", rechargeRate=" + rechargeRate +
				", rechargeRegisteredRate=" + rechargeRegisteredRate +
				", rechargeOlderRate=" + rechargeOlderRate +
				", registeredRate=" + registeredRate +
				", channelName='" + channelName + '\'' +
				", parentId=" + parentId +
				", channelId=" + channelId +
				", businessDate='" + businessDate + '\'' +
				'}';
	}
}