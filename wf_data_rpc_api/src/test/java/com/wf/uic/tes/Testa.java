package com.wf.uic.tes;

import com.alibaba.druid.support.json.JSONUtils;
import com.wf.uic.rpc.dto.LoginDto;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

public class Testa {


    @Test
    public void test1() {
        LoginDto loginDto = new LoginDto(200, "chenyiping", "message");

        LoginDto loginDto2 = null;

        BeanUtils.copyProperties(loginDto, loginDto2);

        JSONUtils.toJSONString(loginDto2);
    }
}
