package com.wf.data.rpc;

import com.wf.data.rpc.dto.UserLabelDto;

/**
 * @author shihui
 * @date 2018/6/7
 */
public interface DatawareUserLabelService {
    //获取用户标签
    UserLabelDto getUserLabelByUserId(long userId);
}
