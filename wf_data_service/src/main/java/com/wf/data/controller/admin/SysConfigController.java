package com.wf.data.controller.admin;

import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.DataConfig;
import com.wf.data.service.DataConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 配置项管理
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/sysconfig")
public class SysConfigController extends ExtJsController {

	@Autowired
	private DataConfigService dataConfigService;

	/**
	 * 配置项列表
	 * @return
	 */
	@RequestMapping("/list")
	public Object list() {
		Page<DataConfig> page = getPage(DataConfig.class);
		return dataGrid(dataConfigService.findPage(page));
	}

	/**
	 * 保存配置项
	 * @return
	 */
	@RequestMapping("/save")
	public Object save() {
		DataConfig form = getForm(DataConfig.class);
		if (form == null) {
			return error("请求参数错误");
		}
		DataConfig platSysConfig = dataConfigService.findByNameAndChannel(form.getName(), form.getChannelId());
		if (form.getId() == null && platSysConfig != null) {
			return error("配置项已经存在");
		}

		if (form.getId() != null && platSysConfig != null && !form.getId().equals(platSysConfig.getId())) {
			return error("配置项已经存在");
		}
		if (platSysConfig != null) {
			form.setCreateTime(platSysConfig.getCreateTime());
		} else {
			form.setCreateTime(new Date());
		}
		form.setUpdateTime(new Date());
		dataConfigService.save(form);
		return success();
	}
}
