package com.wf.data.controller.request.center;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改昵称
 * @author fxy
 * @date 2017/10/26
 */
public class UpdateNicknameRequest {
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
