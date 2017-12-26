package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.extjs.data.DataGrid;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.UicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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


	/**
	 * 获取所有的渠道
	 * @return
	 */
	@RequestMapping("/getAllChannels")
	public Object getAllChannels() {
		JSONObject json = getRequestJson();
		long start = json.getLongValue("start");
		long length = json.getLongValue("limit");
		if (length == 0L) {
			length = 20L;
		}

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

		Page<ChannelInfo> page = new Page<>(dto, start, length);
		page = channelInfoService.findPage(page);

		DataGrid<ChannelInfo> dataGrid = new DataGrid<>();
		dataGrid.setData(page.getData());
		dataGrid.setSuccess(true);
		dataGrid.setTotal(page.getiTotalDisplayRecords());
		return dataGrid;
	}


	@RequestMapping("/getParentChannels")
	public Object getParentChannels() {
		JSONObject json = getRequestJson();
		long start = json.getLongValue("start");
		long length = json.getLongValue("limit");
		if (length == 0L) {
			length = 20L;
		}

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
		Page<ChannelInfo> page = new Page<>(dto, start, length);
		page = channelInfoService.findPage(page);

		DataGrid<ChannelInfo> dataGrid = new DataGrid<>();
		dataGrid.setData(page.getData());
		dataGrid.setSuccess(true);
		dataGrid.setTotal(page.getiTotalDisplayRecords());
		return dataGrid;
	}


	@RequestMapping("/getChildChannels")
	public Object getChildChannels() {
		JSONObject json = getRequestJson();
		long start = json.getLongValue("start");
		long length = json.getLongValue("limit");
		if (length == 0L) {
			length = 20L;
		}

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
//		dto.setParentId(Long.parseLong(channelId));
		Page<ChannelInfo> page = new Page<>(dto, start, length);
		page = channelInfoService.findPage(page);

		DataGrid<ChannelInfo> dataGrid = new DataGrid<>();
		dataGrid.setData(page.getData());
		dataGrid.setSuccess(true);
		dataGrid.setTotal(page.getiTotalDisplayRecords());
		return dataGrid;
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
}
