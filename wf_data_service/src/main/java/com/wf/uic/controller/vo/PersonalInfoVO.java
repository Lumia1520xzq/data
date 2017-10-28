package com.wf.uic.controller.vo;

/**
 * 手机绑定信息
 * @author fxy
 */
public class PersonalInfoVO {
    private Long userId;
    private String loginname;
    private Integer userType;
    private String nickname;
    private boolean nicknameFlag;
    private String headImg;
    private String phone;
    private Double useAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isNicknameFlag() {
        return nicknameFlag;
    }

    public void setNicknameFlag(boolean nicknameFlag) {
        this.nicknameFlag = nicknameFlag;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(Double useAmount) {
        this.useAmount = useAmount;
    }
}