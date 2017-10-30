package com.wf.data.rpc;

import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.rpc.dto.UicBuryingPointDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * @author Ping
 * 旧埋点
 */
public interface BuryingRpcService {

    /**
     * 保存埋点
     * @param uicBuryingPointDto
     * @return
     */
    JsonResult save(UicBuryingPointDto uicBuryingPointDto);


    UicBuryingPointDto findLastGameLoading(Long userId, Integer gameType);

    List<UicBuryingPointDto> getUserLastPlayGame(Long userId, Integer num, Long channelId) ;

    UicBuryingPointDto getByGameTypeAndBuryingType(Integer gameType, Integer buryingType, Long userId);

    Date getLastLoginWealTime(Long userId, Integer buryingType);

}
