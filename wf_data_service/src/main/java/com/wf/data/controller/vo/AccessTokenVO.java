package com.wf.data.controller.vo;

/**
 * 返回access token
 *
 * @author Fe 2016年4月16日
 */
public class AccessTokenVO {
    private String accessToken;
    private String refreshToken;
    private Long expireIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
