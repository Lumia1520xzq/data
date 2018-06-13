package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.DatawareUserLabelDao;
import com.wf.data.dao.datarepo.entity.DatawareUserLabel;
import org.springframework.stereotype.Service;

/**
 * @author shihui
 * @date 2018/6/7
 */
@Service
public class DatawareUserLabelService extends CrudService<DatawareUserLabelDao, DatawareUserLabel> {

    //获取用户标签
    public DatawareUserLabel getUserLabelByUserId(long userId) {
        DatawareUserLabel userLabel = dao.getLabelsByUserId(userId, DateUtils.getYesterdayDate());
        if (userLabel == null){
            userLabel = new DatawareUserLabel();
        }
        return userLabel;
    }
}
