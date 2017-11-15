package com.wf.data.rpc.dto;


import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class DataConfigDto implements Serializable{
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


	@Override
	public String toString() {
		return "BaseConfigDTO{" +
				"name='" + name + '\'' +
				", value='" + value + '\'' +
				", channelId=" + channelId +
				'}';
	}

	public static DataConfigDto toDto(Object object) {
		if (object == null) {
			return null;
		}
		DataConfigDto dto = new DataConfigDto();
		BeanUtils.copyProperties(object, dto);
		return dto;
	}
}