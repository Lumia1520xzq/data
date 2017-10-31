package com.wf.data.controller.request.register;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
/**
 * 手机注册发送验证码
 * @author fxy
 * @date 2017/10/25
 */
public class SendCodeRequest {
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
