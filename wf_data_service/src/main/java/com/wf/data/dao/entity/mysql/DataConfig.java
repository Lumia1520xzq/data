package com.wf.data.dao.entity.mysql;


import com.wf.core.persistence.DataEntity;

public class DataConfig extends DataEntity {
	private static final long serialVersionUID = -1;
	private String name;
	private String value;
	private String remark;
	private Long channelId;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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