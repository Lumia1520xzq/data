package com.wf.uic.controller.request.callback;

import org.hibernate.validator.constraints.NotBlank;

public class CheetahClearRequest {
    @NotBlank(message = "用户ID不能为空")
    private String userID;
    @NotBlank(message = "Token不能为空")
    private String token;
    private String platformCode;
    private String appVersion;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
