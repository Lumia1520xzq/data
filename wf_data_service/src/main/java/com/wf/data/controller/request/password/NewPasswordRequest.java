package com.wf.data.controller.request.password;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * 忘记密码-修改密码
 * @author fxy
 * @date 2017/10/25
 */
public class NewPasswordRequest {
    @Pattern(regexp = "1\\d{10}")
    @NotBlank(message = "手机号码不能为空")
    private String username;
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 16, message = "长度为6-16个字符")
    private String newPassword;
    private String smsCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
