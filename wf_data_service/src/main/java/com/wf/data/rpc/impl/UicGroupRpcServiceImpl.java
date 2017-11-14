package com.wf.data.rpc.impl;

import com.google.common.collect.Lists;
import com.wf.data.dao.entity.mysql.UicGroup;
import com.wf.data.rpc.UicGroupRpcService;
import com.wf.data.rpc.dto.UicGroupDto;
import com.wf.data.service.UicGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jijie.chen
 */
@Service
public class UicGroupRpcServiceImpl implements UicGroupRpcService {

    @Autowired
    private UicGroupService uicGroupService;

    @Override
    public List<Long> findGroupUsers(String groupIds) {
        return uicGroupService.findGroupUsers(groupIds);
    }

    @Override
    public List<UicGroupDto> getInGroupByUserId(Long userId, List<Long> groupIds) {
        List<UicGroupDto> list = Lists.newArrayList();
        List<UicGroup> uicGroups = uicGroupService.getInGroupByUserId(userId, groupIds);
        if (null != uicGroups && uicGroups.size() > 0) {
            for (UicGroup uicGroup : uicGroups) {
                UicGroupDto dto = new UicGroupDto();
                BeanUtils.copyProperties(uicGroup, dto);
                list.add(dto);
            }
        }
        return list;
    }
}