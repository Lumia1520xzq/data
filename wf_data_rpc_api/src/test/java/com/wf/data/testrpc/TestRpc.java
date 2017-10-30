package com.wf.data.testrpc;

import com.wf.data.rpc.BehaviorRecordRpcService;
import com.wf.data.rpc.BuryingRpcService;
import com.wf.data.rpc.dto.BehaviorRecordDto;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.UicBuryingPointDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dubbo-consumer-test.xml"})
public class TestRpc {

    @Resource
    private BehaviorRecordRpcService behaviorRecordRpcService;

    @Resource
    private BuryingRpcService buryingRpcService;

    @Test
    public void test1() {

        BehaviorRecordDto behaviorRecordDto = new BehaviorRecordDto();
        behaviorRecordDto.setUserId(99009900L);
        behaviorRecordDto.setBehaviorName("ttt");
        behaviorRecordDto.setBehaviorEventId(29L);
        behaviorRecordDto.setChannelId(100000L);
        behaviorRecordDto.setParentChannelId(001L);

        JsonResult<String> result = behaviorRecordRpcService.save(behaviorRecordDto);

        System.out.println("result.getSuccess() : " + result.getSuccess());
    }

    @Test
    public void test2(){

        UicBuryingPointDto uicBuryingPointDto = new UicBuryingPointDto();
        uicBuryingPointDto.setUserId(99119911L);
        uicBuryingPointDto.setBuryingType(2);
        uicBuryingPointDto.setChannelId(100000L);
        uicBuryingPointDto.setGameType(0);
        uicBuryingPointDto.setCountNum(8);

        JsonResult result = buryingRpcService.save(uicBuryingPointDto);

        System.out.println("result.getSuccess() : " + result.getSuccess());
    }

    @Test
    public void test3(){

        UicBuryingPointDto dto =  buryingRpcService.findLastGameLoading(99119911L, 8);
        System.out.println(dto);

    }

    @Test
    public void test4(){
        List<UicBuryingPointDto> list = buryingRpcService.getUserLastPlayGame(99119911L, 8, 100000L) ;
        System.out.println(list);

    }

    @Test
    public void test5(){
        UicBuryingPointDto dto = buryingRpcService.getByGameTypeAndBuryingType(0, 2, 99119911L);
        System.out.println(dto);
    }

    @Test
    public void test6(){
        Date date = buryingRpcService.getLastLoginWealTime(99119911L, 2);
        System.out.println(date);
    }

}
