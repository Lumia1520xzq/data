package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.UicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用数据接口
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/common/data")
public class CommonDataController extends ExtJsController {

	@Autowired
	private ChannelInfoService channelInfoService;
	@Autowired
	private UicUserService uicUserService;
	@Autowired
	private DataConfigService dataConfigService;


	/**
	 * 获取所有的渠道
	 * @return
	 */
	@RequestMapping("/getAllChannels")
	public Object getAllChannels() {
		JSONObject json = getRequestJson();

		String keyword = null;
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			keyword = data.getString("data");
		}
		ChannelInfo dto = new ChannelInfo();
		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
			if (NumberUtils.isDigits(keyword)) {
				dto.setId(NumberUtils.toLong(keyword));
			} else {
				dto.setName(keyword);
			}
		}

		List<ChannelInfo> list = channelInfoService.findList(dto,1000);

		for (ChannelInfo cInfo :list) {
			cInfo.setName(cInfo.getName()+"("+cInfo.getId()+")");
		}
		return list;
	}


	@RequestMapping("/getParentChannels")
	public Object getParentChannels() {
		JSONObject json = getRequestJson();

		String keyword = null;
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			keyword = data.getString("data");
		}
		ChannelInfo dto = new ChannelInfo();
		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
			if (NumberUtils.isDigits(keyword)) {
				dto.setId(NumberUtils.toLong(keyword));
			} else {
				dto.setName(keyword);
			}
		}
		dto.setEnable(1);
		dto.setMainChannel(1L);
		List<ChannelInfo> list = channelInfoService.findList(dto,1000);

		for (ChannelInfo cInfo :list) {
			cInfo.setName(cInfo.getName()+"("+cInfo.getId()+")");
		}
		return list;
	}


	@RequestMapping("/getChildChannels")
	public Object getChildChannels() {
		JSONObject json = getRequestJson();

		String keyword = null;
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			keyword = data.getString("parentId");
		}
		ChannelInfo dto = new ChannelInfo();
		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
			if (NumberUtils.isDigits(keyword)) {
				dto.setParentId(Long.parseLong(keyword));
			} else {
				dto.setName(keyword);
			}
		}
		dto.setEnable(1);
		List<ChannelInfo> list = channelInfoService.findList(dto,1000);

		for (ChannelInfo cInfo :list) {
			cInfo.setName(cInfo.getName()+"("+cInfo.getId()+")");
		}

		return list;
	}


	@ResponseBody
	@RequestMapping(value = { "userList" })
	public Object findUserList(HttpServletRequest request) {
		UicUser user = new UicUser();
		JSONObject json = getRequestJson();

		String keyword = null;
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			keyword = data.getString("data");
		}

		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
			if (NumberUtils.isDigits(keyword)) {
				user.setId(NumberUtils.toLong(keyword));
			} else {
				user.setNickname(keyword);
			}
		}
		List<UicUser> list = uicUserService.findList(new Page<UicUser>(user));
		for (UicUser uicUser : list) {
			uicUser.setNickname(uicUser.getNickname()+"("+uicUser.getId()+")");
		}
		return list;
	}

	@RequestMapping("/getViewChannels")
	public Object getViewChannels() {
		JSONObject json = getRequestJson();
		String keyword = null;
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			keyword = data.getString("data");
		}
		ChannelInfo dto = new ChannelInfo();
		List<ChannelInfo> list = new ArrayList<>();
		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
			if (NumberUtils.isDigits(keyword)) {
				dto.setId(NumberUtils.toLong(keyword));
			} else {
				dto.setName(keyword);
			}
			dto.setEnable(1);
			dto.setMainChannel(1L);
			list = channelInfoService.findList(dto,1000);
		}else{
			String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
			if(StringUtils.isNotBlank(channelIdList)){
				List<String> channels = Arrays.asList(channelIdList.split(","));
				for(String channel:channels){
					ChannelInfo info = channelInfoService.get(Long.parseLong(channel));
					list.add(info);
				}
				ChannelInfo special = new ChannelInfo();
				special.setId(0L);
				special.setName("其他");
				list.add(special);
			}
		}
		for (ChannelInfo cInfo :list) {
			cInfo.setName(cInfo.getName()+"("+cInfo.getId()+")");
		}
		return list;
	}
}
