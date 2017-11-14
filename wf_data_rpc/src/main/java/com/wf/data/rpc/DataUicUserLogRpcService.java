package com.wf.data.rpc;

import com.wf.data.rpc.dto.UicUserLogDto;

import java.util.List;

public interface DataUicUserLogRpcService {

    List<String> findIpByIpCount(UicUserLogDto log);

    List<UicUserLogDto> findUserLogByIp(UicUserLogDto log);

    List<String> findIpByUserId(Long userId);

    UicUserLogDto getUserCountByIp(List<String> ips);
}
