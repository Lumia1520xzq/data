package com.wf.uic.controller.request.oauth;


import com.wf.uic.controller.request.VisitorRequest;

import javax.validation.constraints.NotNull;

/**
 * app qq登录
 * @author jijie.chen
 * @date 2017年5月3日
 */
public class QQLoginRequest extends VisitorRequest {
	@NotNull(message="nickname不能为空")
	private String nickname;
	@NotNull(message="headImg不能为空")
	private String headImg;
	@NotNull(message="openid不能为空")
	private String openid;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
