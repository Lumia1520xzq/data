package com.wf.data.dao.mycatuic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.datarepo.entity.DatawareUserInfo;
import com.wf.data.dao.mycatuic.entity.UicUser;

import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author fxy
 * @date 2017/10/28
 */
@MyBatisDao(tableName = "uic_user")
public interface UicUserDao extends CrudDao<UicUser> {

    List<DatawareUserInfo> findUserInfoByTime(Map<String, Object> map);

    List<String> getThirdIdList(Map<String, Object> map);

    long getCountByDate(Map<String, Object> map);


}
