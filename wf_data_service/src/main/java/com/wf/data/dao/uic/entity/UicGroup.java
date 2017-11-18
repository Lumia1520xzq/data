package com.wf.data.dao.uic.entity;


import com.wf.core.persistence.DataEntity;

public class UicGroup extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private String userData;
	private Long groupTypeId;
	private Long groupTypeParentId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public Long getGroupTypeId() {
		return groupTypeId;
	}
	public void setGroupTypeId(Long groupTypeId) {
		this.groupTypeId = groupTypeId;
	}
	public Long getGroupTypeParentId() {
		return groupTypeParentId;
	}
	public void setGroupTypeParentId(Long groupTypeParentId) {
		this.groupTypeParentId = groupTypeParentId;
	}
}