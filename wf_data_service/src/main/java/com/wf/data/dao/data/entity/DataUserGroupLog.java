package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class DataUserGroupLog extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long groupTypeId;
	private String remark;
	private Long channelId;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupTypeId() {
		return groupTypeId;
	}

	public void setGroupTypeId(Long groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}