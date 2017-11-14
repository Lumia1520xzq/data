package com.wf.data.dao.mysql;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.entity.mysql.ReportGrayUser;
import org.apache.ibatis.annotations.Param;

@MyBatisDao(tableName = "report_gray_user")
public interface ReportGrayUserDao extends CrudDao<ReportGrayUser> {

	ReportGrayUser getByUserId(@Param("userId") Long userId);

}
