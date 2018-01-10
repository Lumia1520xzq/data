package com.wf.data.dao.mall.entity;


import com.wf.core.persistence.DataEntity;

import java.util.List;

public class InventoryPhyAwardsSendlog extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long awardsEntity;
	private Long phyAwardsId;
	private Long userId;
	private String receiverName;
	private String receiverMobile;
	private String receiverAddress;
	private Integer receiveStatus;
	private String receiveRemark;
	private Long businessId;
	private Long activityId;
	private List<Long> activityIds;
	private Integer activityType;
	private Long parentId; //主渠道 
	private String callbackQueue;
	//管理端属性
	private String phyAwardsName;
	private Integer phyAwardsType;
	private String activityName;
	private String userName;
	private String beginDate;
	private String endDate;
	private Long goldAmount;
	private Double rmbAmount;

	public Double getRmbAmount() {
		return rmbAmount;
	}

	public void setRmbAmount(Double rmbAmount) {
		this.rmbAmount = rmbAmount;
	}

	public Long getGoldAmount() {
		return goldAmount;
	}

	public void setGoldAmount(Long goldAmount) {
		this.goldAmount = goldAmount;
	}

	public Long getAwardsEntity() {
		return awardsEntity;
	}
	public void setAwardsEntity(Long awardsEntity) {
		this.awardsEntity = awardsEntity;
	}
	public Long getPhyAwardsId() {
		return phyAwardsId;
	}
	public void setPhyAwardsId(Long phyAwardsId) {
		this.phyAwardsId = phyAwardsId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public Integer getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public String getReceiveRemark() {
		return receiveRemark;
	}
	public void setReceiveRemark(String receiveRemark) {
		this.receiveRemark = receiveRemark;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public String getCallbackQueue() {
		return callbackQueue;
	}
	public void setCallbackQueue(String callbackQueue) {
		this.callbackQueue = callbackQueue;
	}
	public String getPhyAwardsName() {
		return phyAwardsName;
	}
	public void setPhyAwardsName(String phyAwardsName) {
		this.phyAwardsName = phyAwardsName;
	}
	public Integer getPhyAwardsType() {
		return phyAwardsType;
	}
	public void setPhyAwardsType(Integer phyAwardsType) {
		this.phyAwardsType = phyAwardsType;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<Long> getActivityIds() {
		return activityIds;
	}
	public void setActivityIds(List<Long> activityIds) {
		this.activityIds = activityIds;
	}
	
}