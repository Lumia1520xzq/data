package com.wf.uic.controller.request.password;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
/**
 * 密码修改-新密码
 * @author fxy
 * @date 2017/10/25
 */
public class UpdatePasswordRequest {

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 16, message = "长度为6-16个字符")
    private String newPassword;
    private String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
