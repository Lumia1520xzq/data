package com.wf.uic.controller.request.callback;

import java.util.Arrays;

import com.wf.core.utils.encrypt.MD5Util;
import com.wf.uic.common.hander.LotteryHander;
import org.json.JSONObject;

public class CheetahClearBean {
    public static final String NO_TOKEN = MD5Util.getUpperMD5("NotToken");
    private String timeStamp = Long.toString(System.currentTimeMillis());
    private JSONObject body = new JSONObject();
    private String token = NO_TOKEN;
    private String userType = "0";
    private String userId = "0";
    private String platformCode = "";      //平台编码(Iphone，Android)
    private String appVersion = "";        //app版本号(比如3.8.2)

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
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

    public JSONObject getBody() {
        return body;
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

    public String getSingData() {
        String[] strs = new String[5];
        strs[0] = userId;
        strs[1] = userType;
        strs[2] = token;
        strs[3] = timeStamp;
        strs[4] = LotteryHander.getProjectKey();
        for (int i = 0; i < strs.length; i++) {
            strs[i] = strs[i].toLowerCase();
        }
        Arrays.sort(strs);
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s);
        }
        return MD5Util.getUpperMD5(sb.toString());
    }

    @Override
    public String toString() {
        return "\r\nhead: timeStamp=" + timeStamp + ", token=" + token + ", userType="
                + userType + ", userId=" + userId + ", platformCode=" + getPlatformCode()
                + ", appVersion=" + getAppVersion() + ", singData=" + getSingData() + "\r\nbody: " + body;
    }
}
