package com.wf.api.controller.test;

import com.alibaba.fastjson.JSON;
import com.wf.data.controller.request.BehaviorRequest;
import org.junit.Test;

public class Test1 {

    @Test
    public void t1(){
        BehaviorRequest r = new BehaviorRequest();
        r.setBehaviorEventId(1111L);
        System.out.println(JSON.toJSONString(r));
    }
}
