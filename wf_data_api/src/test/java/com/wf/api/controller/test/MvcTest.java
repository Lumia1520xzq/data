package com.wf.api.controller.test;

import com.wf.data.controller.api.UicBehaviorRecordController;
import com.wf.data.dao.entity.mycat.UicBehaviorRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:application/app-spring-uic.xml",
        "classpath:application/app-spring-mvc-uic.xml"})
public class MvcTest {

    @Resource
    private UicBehaviorRecordController uicBehaviorRecordController;

    @Autowired
    ServletContext context;

    MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(uicBehaviorRecordController).build();
    }

    @Test
    public void test1(){
        UicBehaviorRecord behaviorRecord = new UicBehaviorRecord();
        behaviorRecord.setUserId(99009900L);
        behaviorRecord.setBehaviorName("ttt");
        behaviorRecord.setBehaviorEventId(29L);
        behaviorRecord.setChannelId(100000L);
        behaviorRecord.setParentChannelId(001L);
        uicBehaviorRecordController.save(behaviorRecord);
    }

}
