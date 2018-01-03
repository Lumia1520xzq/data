package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

import java.util.List;

public class DatawareConvertHour extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Integer rechargeCount;
	private Double thirdAmount;
	private Integer bizType;
	private Long channelId;
	private String convertHour;
	private String convertDate;
	private Long parentId;
	private List<Long> userList;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


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

	public Integer getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Integer rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public Double getThirdAmount() {
		return thirdAmount;
	}

	public void setThirdAmount(Double thirdAmount) {
		this.thirdAmount = thirdAmount;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getConvertHour() {
		return convertHour;
	}

	public void setConvertHour(String convertHour) {
		this.convertHour = convertHour;
	}

	public String getConvertDate() {
		return convertDate;
	}

	public void setConvertDate(String convertDate) {
		this.convertDate = convertDate;
	}
}