package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;

public class TransConvert extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private String orderSn;
	private Double amount;
	private String thirdOrderSn;
	private Double thirdAmount;
	private Integer source;
	private Integer payType;
	private Integer status;
	private String tradeType;
	private Long channelId;
	private Integer auditVersion;

	public Integer getAuditVersion() {
		return auditVersion;
	}

	public void setAuditVersion(Integer auditVersion) {
		this.auditVersion = auditVersion;
	}

	//是否为沙盒测试订单
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getThirdOrderSn() {
		return thirdOrderSn;
	}
	public void setThirdOrderSn(String thirdOrderSn) {
		this.thirdOrderSn = thirdOrderSn;
	}
	public Double getThirdAmount() {
		return thirdAmount;
	}
	public void setThirdAmount(Double thirdAmount) {
		this.thirdAmount = thirdAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	
}