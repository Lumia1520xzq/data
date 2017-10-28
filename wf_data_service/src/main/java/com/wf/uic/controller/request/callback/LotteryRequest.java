package com.wf.uic.controller.request.callback;

import com.wf.uic.controller.request.VisitorRequest;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 金山彩票请求
 *
 * @author Fe 2016年12月8日
 */
public class LotteryRequest extends VisitorRequest {
    @NotBlank(message = "用户ID不能为空")
    private String userID;
    @NotBlank(message = "用户类型不能为空")
    private String userType;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
