package com.wf.uic.controller.request.center;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 包含手机号和验证码
 * @author fxy
 */
public class PhoneBindRequest {

    @NotBlank(message = "请输入手机号码")
    @Pattern(regexp = "1\\d{10}", message = "手机号码错误！")
    private String phone;
    @NotBlank(message = "请输入验证码")
    private String smsCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
