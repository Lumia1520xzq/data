package com.wf.data.rpc.dto;


import java.io.Serializable;
import java.util.Date;

public class DataUicGroupDto implements Serializable {
	private static final long serialVersionUID = -1;
	private Long userId;
	private String userData;
	private Long groupTypeId;
	private Long groupTypeParentId;

	private Date createTime;    // 创建日期
	private Date updateTime;    // 更新日期
	private int deleteFlag;    // 删除标记（0：正常；1：删除；2：审核）

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