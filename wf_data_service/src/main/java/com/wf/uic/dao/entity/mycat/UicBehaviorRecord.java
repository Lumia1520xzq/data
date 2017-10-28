package com.wf.uic.dao.entity.mycat;


import com.wf.core.persistence.DataEntity;

public class UicBehaviorRecord extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long behaviorEventId;
	private Long channelId;
	private Long parentChannelId;
	private String behaviorName;
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
}