package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.extjs.data.DataGrid;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.service.ChannelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用数据接口
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/common/data")
public class CommonDataController extends ExtJsController {

	@Autowired
	private ChannelInfoService channelInfoService;

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
}
