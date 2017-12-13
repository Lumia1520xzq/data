package com.wf.data.controller.admin.business;

import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.DataConfig;
import com.wf.data.service.DataConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户成本查询
 * @author: lcs
 * @date: 2017/12/13
 */
@RestController
@RequestMapping("/data/business/cost")
public class CostController extends ExtJsController {

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


}
