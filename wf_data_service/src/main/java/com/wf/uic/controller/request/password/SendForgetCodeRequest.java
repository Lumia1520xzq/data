package com.wf.uic.controller.request.password;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 忘记密码-发送验证码
 * @author fxy
 * @date 2017/10/25
 */
public class SendForgetCodeRequest {
    @Pattern(regexp = "1\\d{10}")
    @NotBlank(message = "手机号码不能为空")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
