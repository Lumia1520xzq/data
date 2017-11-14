package com.wf.data.dao.mycat;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.core.persistence.Page;
import com.wf.data.dao.entity.mycat.UicUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao(tableName = "uic_user")
public interface UicUserDao extends CrudDao<UicUser> {
	
	public UicUser getUserByThird(@Param("userSource") Integer userSource, @Param("thirdId") String thirdId);

	public UicUser getUserByLoginname(String loginname);

	public UicUser getByUserId(Long userId);

	public UicUser getByNickname(String nickname);
	
	public UicUser getByPhone(String phone);
	
	public List<UicUser> getByChannelId(Page<UicUser> page);

	public List<UicUser> findList2(Page<UicUser> page);

	public long count2(Page<UicUser> page);

	UicUser getUserByInvitationCode(String invitationCode);

	List<UicUser> getByParentInvitationCode(String parentInvitationCode);
}
