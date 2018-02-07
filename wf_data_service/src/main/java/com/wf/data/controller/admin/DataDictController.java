package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.service.DataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 字典管理
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/dict")
public class DataDictController extends ExtJsController {

	@Autowired
	private DataDictService dataDictService;

	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list")
	public Object list() {
		Page<DataDict> page = getPage(DataDict.class);
		return dataGrid(dataDictService.findPage(page));
	}

	/**
	 * 保存
	 * @return
	 */
	@RequestMapping("/save")
	public Object save() {
		DataDict form = getForm(DataDict.class);
		if (form == null) {
			return error("请求参数错误");
		}
		DataDict platSysDict = dataDictService.get(form.getId());
		if (platSysDict != null) {
			form.setCreateTime(platSysDict.getCreateTime());
		} else {
			form.setCreateTime(new Date());
		}
		form.setUpdateTime(new Date());
		dataDictService.save(form);
		return success();
	}

	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/delete")
	public Object delete() {
		DataDict entity = getForm(DataDict.class);
		dataDictService.delete(entity);
		return success();
	}

	/**
	 * 根据类型获取列表
	 * @return
	 */
	@RequestMapping("/getListByType")
	public Object getListByType() {
		JSONObject json = getRequestJson();
		String type = "";
		JSONObject data = json.getJSONObject("data");
		if (data != null) {
			type = data.getString("type");
		}
		List<DataDict> list = dataDictService.findListByType(type);
		return list;
	}
}
