package com.wf.data.rpc;

import com.wf.data.rpc.dto.BuryingPointDto;
import com.wf.data.rpc.dto.JsonResult;

import java.util.Date;
import java.util.List;

/**
 * @author Ping
 * 旧埋点
 */
public interface BuryingRpcService {

    /**
     * 保存埋点
     * @param buryingPointDto
     * @return
     */
    JsonResult save(BuryingPointDto buryingPointDto);


    BuryingPointDto findLastGameLoading(Long userId, Integer gameType);

    List<BuryingPointDto> getUserLastPlayGame(Long userId, Integer num, Long channelId) ;

    BuryingPointDto getByGameTypeAndBuryingType(Integer gameType, Integer buryingType, Long userId);

    Date getLastLoginWealTime(Long userId, Integer buryingType);

}
