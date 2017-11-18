package com.wf.data.rpc;

import com.wf.data.rpc.dto.DataReportGrayUserDto;

import java.util.List;

/**
 * @author: lcs
 * @date: 2017/11/15
 */
public interface DataReportGrayUserRpcService {

    List<DataReportGrayUserDto> findList(DataReportGrayUserDto entity, int maxResult);

    boolean delete(DataReportGrayUserDto entity);
}
