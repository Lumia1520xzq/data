package com.wf.data.rpc.impl;

import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.DatawareUserLabelDao;
import com.wf.data.dao.datarepo.entity.DatawareUserLabel;
import com.wf.data.rpc.DatawareUserLabelService;
import com.wf.data.rpc.dto.UserLabelDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shihui
 * @date 2018/6/7
 */
public class DatawareUserLabelServiceImpl implements DatawareUserLabelService {

    @Autowired
    private DatawareUserLabelDao userLabelDao;

    @Override
    public UserLabelDto getUserLabelByUserId(long userId) {
        UserLabelDto dto = new UserLabelDto();
        DatawareUserLabel userLabel = userLabelDao.getLabelsByUserId(userId, DateUtils.getYesterdayDate());
        if (null != userLabel){
            BeanUtils.copyProperties(userLabel,dto);
        }
        return dto;
    }
}
