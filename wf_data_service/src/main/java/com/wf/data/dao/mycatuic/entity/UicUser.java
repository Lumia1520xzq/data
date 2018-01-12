package com.wf.data.dao.mycatuic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wf.core.persistence.DataEntity;

/**
 * 用户信息
 * @author fxy
 * @date 2017/10/13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UicUser extends DataEntity {
	private static final long serialVersionUID = -1;
	private String loginname;
	private String password;
	private String nickname;
	private String phone;
	private String headImg;
	private Integer userSource;
	private String sourceNickname;
	private String thirdId;
	private String thirdHeadImg;
	private String thirdParams;
	private Integer soundFlag;
	private Integer status;
	private String visitorToken;
	private Long lastLoginChannelId;
	private Long regChannelId;
	private Long regParentChannel;
	private String invitationCode;
	private String parentInvitationCode;
	private Double backAwardPercent;


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

	public String getSourceNickname() {
		return sourceNickname;
	}

	public void setSourceNickname(String sourceNickname) {
		this.sourceNickname = sourceNickname;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
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

	public String getVisitorToken() {
		return visitorToken;
	}

	public void setVisitorToken(String visitorToken) {
		this.visitorToken = visitorToken;
	}

	public Long getLastLoginChannelId() {
		return lastLoginChannelId;
	}

	public void setLastLoginChannelId(Long lastLoginChannelId) {
		this.lastLoginChannelId = lastLoginChannelId;
	}

	public Long getRegChannelId() {
		return regChannelId;
	}

	public void setRegChannelId(Long regChannelId) {
		this.regChannelId = regChannelId;
	}

	public Long getRegParentChannel() {
		return regParentChannel;
	}

	public void setRegParentChannel(Long regParentChannel) {
		this.regParentChannel = regParentChannel;
	}
}