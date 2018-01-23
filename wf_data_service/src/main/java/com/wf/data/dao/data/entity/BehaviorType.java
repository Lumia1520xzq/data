package com.wf.data.dao.data.entity;

import com.wf.core.persistence.DataEntity;

public class BehaviorType extends DataEntity {
	private static final long serialVersionUID = -1;
	private String name;
	private Long parentEventId;
	private Long eventId;
	private String fullName;
	private String behaviorCatagory;
	private Integer behaviorLevel;
	private Integer isShow;

	// formdata
	private String subEventId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentEventId() {
		return parentEventId;
	}

	public void setParentEventId(Long parentEventId) {
		this.parentEventId = parentEventId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBehaviorCatagory() {
		return behaviorCatagory;
	}

	public void setBehaviorCatagory(String behaviorCatagory) {
		this.behaviorCatagory = behaviorCatagory;
	}

	public Integer getBehaviorLevel() {
		return behaviorLevel;
	}

	public void setBehaviorLevel(Integer behaviorLevel) {
		this.behaviorLevel = behaviorLevel;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getSubEventId() {
		return subEventId;
	}

	public void setSubEventId(String subEventId) {
		this.subEventId = subEventId;
	}
}