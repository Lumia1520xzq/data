package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.dao.uic.entity.UicGroup;
import com.wf.data.rpc.DataUicGroupRpcService;
import com.wf.data.rpc.dto.DataUicGroupDto;
import com.wf.data.service.UicGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jijie.chen
 */
@Service
public class DataUicGroupRpcServiceImpl implements DataUicGroupRpcService {

    @Autowired
    private UicGroupService uicGroupService;

    @Override
    public List<Long> findGroupUsers(List<String> groupIdList) {
        return uicGroupService.findGroupUsers(groupIdList);
    }

    @Override
    public List<DataUicGroupDto> getInGroupByUserId(Long userId, List<Long> groupIds) {
        List<DataUicGroupDto> list = Lists.newArrayList();
        List<UicGroup> uicGroups = uicGroupService.getInGroupByUserId(userId, groupIds);
        if (null != uicGroups && uicGroups.size() > 0) {
            for (UicGroup uicGroup : uicGroups) {
                DataUicGroupDto dto = new DataUicGroupDto();
                BeanUtils.copyProperties(uicGroup, dto);
                list.add(dto);
            }
        }
        return list;
    }
}