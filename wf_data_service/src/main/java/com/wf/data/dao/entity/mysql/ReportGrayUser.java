package com.wf.data.dao.entity.mysql;


import com.wf.core.persistence.DataEntity;

public class ReportGrayUser extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private java.util.Date regTime;
	private java.util.Date grayTime;
	private Long regChannelId;
	private String ip;
	private Integer ipUserCount;
	private Double sumRecharge;
	private Double afterGrayRecharge;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public java.util.Date getRegTime() {
		return regTime;
	}
	public void setRegTime(java.util.Date regTime) {
		this.regTime = regTime;
	}
	public java.util.Date getGrayTime() {
		return grayTime;
	}
	public void setGrayTime(java.util.Date grayTime) {
		this.grayTime = grayTime;
	}
	public Long getRegChannelId() {
		return regChannelId;
	}
	public void setRegChannelId(Long regChannelId) {
		this.regChannelId = regChannelId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getIpUserCount() {
		return ipUserCount;
	}
	public void setIpUserCount(Integer ipUserCount) {
		this.ipUserCount = ipUserCount;
	}
	public Double getSumRecharge() {
		return sumRecharge;
	}
	public void setSumRecharge(Double sumRecharge) {
		this.sumRecharge = sumRecharge;
	}
	public Double getAfterGrayRecharge() {
		return afterGrayRecharge;
	}
	public void setAfterGrayRecharge(Double afterGrayRecharge) {
		this.afterGrayRecharge = afterGrayRecharge;
	}
	
}