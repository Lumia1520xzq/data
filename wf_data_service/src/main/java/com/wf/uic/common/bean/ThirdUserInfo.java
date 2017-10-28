package com.wf.uic.common.bean;

import java.io.Serializable;

public class ThirdUserInfo implements Serializable {
    private static final long serialVersionUID = 8733777484873196374L;
    private String userType;
    private String userUserId;
    private String token;
    private String platformCode;
    private String appVersion;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserUserId() {
        return userUserId;
    }

    public void setUserUserId(String userUserId) {
        this.userUserId = userUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}