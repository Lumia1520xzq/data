package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class DataIpInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private String ip;
	private Long ipCount;
	private Long channelId;
	private String loginDate;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getIpCount() {
		return ipCount;
	}

	public void setIpCount(Long ipCount) {
		this.ipCount = ipCount;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
}