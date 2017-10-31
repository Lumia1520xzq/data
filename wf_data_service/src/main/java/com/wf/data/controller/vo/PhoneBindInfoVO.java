package com.wf.data.controller.vo;

/**
 * 用户个人信息
 * @author fxy
 */
public class PhoneBindInfoVO {
    private boolean existsFlag;
    private String phone;

    public boolean isExistsFlag() {
        return existsFlag;
    }

    public void setExistsFlag(boolean existsFlag) {
        this.existsFlag = existsFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}