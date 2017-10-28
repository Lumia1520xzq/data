package com.wf.uic.controller.request.callback;

import com.wf.uic.controller.request.VisitorRequest;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 澳客请求接口
 *
 * @author chengsheng.liu
 * @date 2017年7月18日
 */
public class OkoooRequest extends VisitorRequest {

    @NotBlank(message = "Token不能为空")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
