package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class ReportFishBettingInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Integer fishConfigId;
	private Double amount;
	private Long bettingCount;
	private Double bettingAmount;
	private Double resultAmount;
	private String bettingDate;
	private Long channelId;
	private String createDate;


	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getFishConfigId() {
		return fishConfigId;
	}

	public void setFishConfigId(Integer fishConfigId) {
		this.fishConfigId = fishConfigId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getBettingDate() {
		return bettingDate;
	}

	public void setBettingDate(String bettingDate) {
		this.bettingDate = bettingDate;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}