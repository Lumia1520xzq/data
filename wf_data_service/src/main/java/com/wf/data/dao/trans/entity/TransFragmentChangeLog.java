package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;

import java.util.List;

public class TransFragmentChangeLog extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long fragmentId;
	private Long phyAwardsId;
	private Integer changeType;
	private Integer changeNum;
	private Integer businessType;
	private String remark;
	private Long activityId;
	private Long businessId;
	private Long channelId;
	private Integer sourceType;

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
	private String thirdId;


	public List<Long> getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(List<Long> activityIds) {
		this.activityIds = activityIds;
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

	public Long getGoldAmount() {
		return goldAmount;
	}

	public void setGoldAmount(Long goldAmount) {
		this.goldAmount = goldAmount;
	}

	public Double getRmbAmount() {
		return rmbAmount;
	}

	public void setRmbAmount(Double rmbAmount) {
		this.rmbAmount = rmbAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(Long fragmentId) {
		this.fragmentId = fragmentId;
	}

	public Long getPhyAwardsId() {
		return phyAwardsId;
	}

	public void setPhyAwardsId(Long phyAwardsId) {
		this.phyAwardsId = phyAwardsId;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
}