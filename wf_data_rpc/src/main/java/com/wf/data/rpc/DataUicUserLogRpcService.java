package com.wf.data.rpc;

import com.wf.data.rpc.dto.DataUicUserLogDto;

import java.util.List;

public interface DataUicUserLogRpcService {

    List<String> findIpByIpCount(DataUicUserLogDto log);

    List<DataUicUserLogDto> findUserLogByIp(DataUicUserLogDto log);

    List<String> findIpByUserId(Long userId);

    DataUicUserLogDto getUserCountByIp(List<String> ips);
}
