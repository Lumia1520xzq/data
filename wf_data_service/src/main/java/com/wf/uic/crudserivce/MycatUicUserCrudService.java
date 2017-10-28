package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.core.utils.file.FastDFSUtils;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.dao.mycat.UicUserDao;
import com.wf.uic.dao.entity.mysql.UicNicknameRecord;
import com.wf.uic.dao.entity.mycat.UicUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 用户信息操作
 */
@Service
public class MycatUicUserCrudService extends CrudService<UicUserDao, UicUser> {
	@Autowired
	private UicNicknameRecordCrudService uicNicknameRecordCrudService;

	@Async
	public void updateHeadImg(UicUser user) {
		if (StringUtils.isNotBlank(user.getThirdHeadImg())) {
			try {
				user.setHeadImg(FastDFSUtils.roundedImage(user.getThirdHeadImg()));
				super.save(user);
			} catch (Exception e) {
				logger.error("更新头像失败：" + user.getThirdHeadImg(), e);
			}
		}
	}

	/**
	 * 根据条件获取对象
	 * @param params
	 * @return
	 */
	public UicUser getByWhere(UicUser params){
		return dao.getByWhere(params);
	}

	public UicUser getByUserId(Long userId) {
		return get(userId);
	}

	@Override
	public UicUser get(final Long id) {
		return cacheHander.cache(UicCacheKey.MYCAT_UIC_USER_BY_ID.key(id), ()-> dao.get(id));
	}

	/**
	 * *****************************************************************
	 * ********************未根据user_id查询******************************
	 * *****************************************************************
	 */
	public UicUser getUserByLoginname(final String loginname) {
		return cacheHander.cache(UicCacheKey.MYCAT_UIC_USER_BY_LOGINNAME.key(loginname),
				() -> dao.getUserByLoginname(loginname));
	}

	public UicUser getUserByThird(final Integer userSource, final String thirdId) {
		return cacheHander.cache(UicCacheKey.MYCAT_UIC_USER_BY_THIRD_ID.key(userSource, thirdId),
				()->dao.getUserByThird(userSource, thirdId));
	}

	public UicUser getVisitor(final Long channel, final String visitorToken) {
		return cacheHander.cache(UicCacheKey.MYCAT_UIC_USER_BY_VISITOR_TOKEN.key(channel, visitorToken),
				()->dao.getVisitor(visitorToken, channel));
	}

	public UicUser getUserByNickname(String nickName){
		return dao.getByNickname(nickName);
	}

	public UicUser getUserBySourceNickname(String sourceNickname){
		return dao.getUserBySourceNickname(sourceNickname);
	}

	public UicUser getByPhone(String phone) {
		return dao.getByPhone(phone);
	}

	/**
	 * 昵称是否是电话号码或wx***
	 * */
	public boolean isPhoneNumber(UicUser user) {
		String nickName = user.getNickname();
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))([\\*\\d]{8})$";
		if (nickName.startsWith("wx")){
			return true;
		} else if (nickName.matches(regex)) {
			return true;
		}
		return false;
	}

	public void concatNickname(UicUser user) {
		UicNicknameRecord uicNicknameRecord = new UicNicknameRecord();
		uicNicknameRecord.setSourceNickname(user.getSourceNickname());

		int index = uicNicknameRecordCrudService.findList(uicNicknameRecord,10000) == null ? 1 : uicNicknameRecordCrudService.findList(uicNicknameRecord,10000).size() + 1;

		user.setNickname(user.getSourceNickname() + "_" + index);

		uicNicknameRecord.setChannelId(user.getRegChannelId());
		uicNicknameRecord.setNickname(user.getNickname());
		uicNicknameRecord.setUserId(user.getId());
		uicNicknameRecordCrudService.save(uicNicknameRecord);
	}
}
