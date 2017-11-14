package com.wf.data.rpc;

import com.wf.data.rpc.dto.UicGroupDto;

import java.util.List;

/**
 * @author jijie.chen
 */
public interface DataUicGroupRpcService {
    /**
     * 根据组获取组中的所有用户， 多个组以逗号分隔
     *
     * @param groupIds
     * @return
     */
    List<Long> findGroupUsers(String groupIds);


    /**
     * 判断用户是否在指定的子组中
     *
     * @param userId
     * @param groupIds
     * @return
     */
    List<UicGroupDto> getInGroupByUserId(Long userId, List<Long> groupIds);


}