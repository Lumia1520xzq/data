package com.wf.data.rpc.dto;

import java.io.Serializable;

/**
 * @author shihui
 * @date 2018/6/7
 */
public class UserLabelDto implements Serializable {

    private static final long serialVersionUID = -1;

    private String userId;
    private String prediction;
    private String user_level;
    private String R;
    private String F;
    private String M;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getR() {
        return R;
    }

    public void setR(String r) {
        R = r;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }

    public String getM() {
        return M;
    }

    public void setM(String m) {
        M = m;
    }
}
