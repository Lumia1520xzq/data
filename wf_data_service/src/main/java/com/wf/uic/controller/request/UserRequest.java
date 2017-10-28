package com.wf.uic.controller.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 只包含用户名
 */
public class UserRequest extends VisitorRequest{
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
