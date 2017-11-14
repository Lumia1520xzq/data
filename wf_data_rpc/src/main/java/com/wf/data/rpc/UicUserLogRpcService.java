package com.wf.data.rpc;

import com.wf.data.rpc.dto.UicUserLogDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UicUserLogRpcService {

    List<String> findIpByIpCount(UicUserLogDto log);

    List<UicUserLogDto> findUserLogByIp(UicUserLogDto log);

    List<String> findIpByUserId(Long userId);

    UicUserLogDto getUserCountByIp(List<String> ips);
}
