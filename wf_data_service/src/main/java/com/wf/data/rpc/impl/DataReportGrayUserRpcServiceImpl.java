package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.common.utils.JsonResultUtils;
import com.wf.data.dao.entity.mysql.ReportGrayUser;
import com.wf.data.dao.entity.mysql.UicGroup;
import com.wf.data.dao.entity.mysql.UicUserLog;
import com.wf.data.rpc.DataReportGrayUserRpcService;
import com.wf.data.rpc.dto.DataReportGrayUserDto;
import com.wf.data.rpc.dto.DataUicGroupDto;
import com.wf.data.rpc.dto.JsonResult;
import com.wf.data.service.ReportGrayUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: lcs
 * @date: 2017/11/15
 */
public class DataReportGrayUserRpcServiceImpl implements DataReportGrayUserRpcService {

    @Autowired
    private ReportGrayUserService reportGrayUserService;

    @Override
    public List<DataReportGrayUserDto> findList(DataReportGrayUserDto entity, int maxResult) {
        List<DataReportGrayUserDto> list = Lists.newArrayList();
        ReportGrayUser grayuser = new ReportGrayUser();
        BeanUtils.copyProperties(entity, grayuser);

        List<ReportGrayUser> reportGrayUserList = reportGrayUserService.findList(grayuser, maxResult);
        if (null != reportGrayUserList && reportGrayUserList.size() > 0) {
            for (ReportGrayUser user : reportGrayUserList) {
                DataReportGrayUserDto dto = new DataReportGrayUserDto();
                BeanUtils.copyProperties(user, dto);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public boolean delete(DataReportGrayUserDto entity) {
        try {
            ReportGrayUser grayuser = new ReportGrayUser();
            BeanUtils.copyProperties(entity, grayuser);
            reportGrayUserService.delete(grayuser);
            return true;
        } catch (BeansException e) {
            e.printStackTrace();
            return false;
        }
    }
}
