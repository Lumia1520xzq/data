package com.wf.uic.controller.request.center;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 手机号
 * @author fxy
 * @2017/10/26
 */
public class PhoneRequest {
    @NotBlank(message = "请输入手机号码")
    @Pattern(regexp = "1\\d{10}", message = "手机号码错误！")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
