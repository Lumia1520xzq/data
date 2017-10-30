package com.wf.api.controller.test;

import com.wf.data.controller.api.BuryingController;
import com.wf.data.dao.entity.mycat.UicBuryingPoint;
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
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:application/app-spring-uic.xml",
        "classpath:application/app-spring-mvc-uic.xml"})
public class MvcTest2 {

    @Resource
    private BuryingController buryingController;

    @Autowired
    ServletContext context;

    MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(buryingController).build();
    }

    @Test
    public void test1(){
        UicBuryingPoint uicBuryingPoint = new UicBuryingPoint();
        uicBuryingPoint.setUserId(99119911L);
        uicBuryingPoint.setBuryingType(2);
        uicBuryingPoint.setChannelId(100000L);
        uicBuryingPoint.setGameType(0);
        uicBuryingPoint.setCountNum(8);
        buryingController.save(uicBuryingPoint);
    }

    @Test
    public void test3(){

        UicBuryingPoint dto =  buryingController.findLastGameLoading(99119911L, 8);
        System.out.println(dto);

    }

    @Test
    public void test4(){
        List<UicBuryingPoint> list = buryingController.getUserLastPlayGame(99119911L, 8, 100000L) ;
        System.out.println(list);

    }

    @Test
    public void test5(){
        UicBuryingPoint dto = buryingController.getByGameTypeAndBuryingType(0, 2, 99119911L);
        System.out.println(dto);
    }

    @Test
    public void test6(){
        Date date = buryingController.getLastLoginWealTime(99119911L, 2);
        System.out.println(date);
    }
}
