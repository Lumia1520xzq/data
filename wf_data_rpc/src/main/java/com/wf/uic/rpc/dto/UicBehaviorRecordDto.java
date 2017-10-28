package com.wf.uic.rpc.dto;

import java.io.Serializable;
import java.util.Date;

public class UicBehaviorRecordDto implements Serializable{
	private Long userId;
	private Long behaviorEventId;
	private Long channelId;
	private Long parentChannelId;
	private String behaviorName;

	private Date createTime;    // 创建日期
	private Date updateTime;    // 更新日期
	private int deleteFlag;    // 删除标记（0：正常；1：删除；2：审核）

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBehaviorEventId() {
		return behaviorEventId;
	}

	public void setBehaviorEventId(Long behaviorEventId) {
		this.behaviorEventId = behaviorEventId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getParentChannelId() {
		return parentChannelId;
	}

	public void setParentChannelId(Long parentChannelId) {
		this.parentChannelId = parentChannelId;
	}

	public String getBehaviorName() {
		return behaviorName;
	}

	public void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}