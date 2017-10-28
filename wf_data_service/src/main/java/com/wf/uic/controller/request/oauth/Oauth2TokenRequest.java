package com.wf.uic.controller.request.oauth;

import javax.validation.constraints.NotNull;

/**
 * Oauth2 token
 *
 * @author Fe 2016年4月16日
 */
public class Oauth2TokenRequest {
    @NotNull(message = "类型不能为空")
    private Integer type;
    @NotNull(message = "token不能为空")
    private String token;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
