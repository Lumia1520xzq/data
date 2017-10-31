package com.wf.data.controller.request.oauth;

import com.wf.data.controller.request.VisitorRequest;

import javax.validation.constraints.NotNull;

public class WechatAccessTokenRequest extends VisitorRequest {
	@NotNull(message = "code不能为空")
	private String code;
	private String appId;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
