package com.wf.data.dao.mycatuic;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.mycatuic.entity.UicUser;

/**
 * 用户信息
 * @author fxy
 * @date 2017/10/28
 */
@MyBatisDao(tableName = "uic_user")
public interface UicUserDao extends CrudDao<UicUser> {
}
