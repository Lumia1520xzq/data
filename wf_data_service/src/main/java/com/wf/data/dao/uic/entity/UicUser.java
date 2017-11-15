package com.wf.data.dao.uic.entity;


import com.wf.core.persistence.DataEntity;

public class UicUser extends DataEntity {
	private static final long serialVersionUID = -1;
	private String loginname;
	private String password;
	private String nickname;
	private String phone;
	private String headImg;
	private Integer userSource;
	private String thirdId;
	private String thirdHeadImg;
	private String thirdParams;
	private Integer soundFlag;
	private Integer status;
	private Long lastLoginChannelId;
	private Long regChannelId;
	private Long channelId;
	//用户所属组
	private String userGroup;
	private Long groupTypeId;
	private String userData;

	private String invitationCode;
	private String parentInvitationCode;
	private Double backAwardPercent;
	private String backAwardPercentStr;
	private String parentUser;

	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Integer getUserSource() {
		return userSource;
	}
	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}
	public String getThirdId() {
		return thirdId;
	}
	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}
	public Integer getSoundFlag() {
		return soundFlag;
	}
	public void setSoundFlag(Integer soundFlag) {
		this.soundFlag = soundFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getThirdHeadImg() {
		return thirdHeadImg;
	}
	public void setThirdHeadImg(String thirdHeadImg) {
		this.thirdHeadImg = thirdHeadImg;
	}
	public String getThirdParams() {
		return thirdParams;
	}
	public void setThirdParams(String thirdParams) {
		this.thirdParams = thirdParams;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public Long getLastLoginChannelId() {
		return lastLoginChannelId;
	}
	public void setLastLoginChannelId(Long lastLoginChannelId) {
		this.lastLoginChannelId = lastLoginChannelId;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Long getRegChannelId() {
		return regChannelId;
	}
	public void setRegChannelId(Long regChannelId) {
		this.regChannelId = regChannelId;
	}
	public Long getGroupTypeId() {
		return groupTypeId;
	}
	public void setGroupTypeId(Long groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getParentInvitationCode() {
		return parentInvitationCode;
	}

	public void setParentInvitationCode(String parentInvitationCode) {
		this.parentInvitationCode = parentInvitationCode;
	}

	public Double getBackAwardPercent() {
		return backAwardPercent;
	}

	public void setBackAwardPercent(Double backAwardPercent) {
		this.backAwardPercent = backAwardPercent;
	}

	public String getBackAwardPercentStr() {
		return this.backAwardPercent == null ? "--" : this.backAwardPercent * 100 + "%";
	}

	public void setBackAwardPercentStr(String backAwardPercentStr) {
		this.backAwardPercentStr = backAwardPercentStr;
	}

	public String getParentUser() {
		return parentUser;
	}

	public void setParentUser(String parentUser) {
		this.parentUser = parentUser;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	
}