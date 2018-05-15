package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.BigDataDict;
import com.wf.data.service.BigDataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 字典管理
 *
 * @author liucs
 */
@RestController
@RequestMapping("/data/admin/bigdata/dict")
public class BigDataDictController extends ExtJsController {

    @Autowired
    private BigDataDictService bigDataDictService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    public Object list() {
        Page<BigDataDict> page = getPage(BigDataDict.class);
        return dataGrid(bigDataDictService.findPage(page));
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping("/save")
    public Object save() {
        BigDataDict form = getForm(BigDataDict.class);
        if (form == null) {
            return error("请求参数错误");
        }
        BigDataDict dict = bigDataDictService.get(form.getId());
        if (dict != null) {
            form.setCreateTime(dict.getCreateTime());
        } else {
            form.setCreateTime(new Date());
        }
        form.setUpdateTime(new Date());
        bigDataDictService.save(form);
        return success();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        BigDataDict entity = getForm(BigDataDict.class);
        bigDataDictService.delete(entity);
        return success();
    }

    /**
     * 根据类型获取列表
     *
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
        List<BigDataDict> list = bigDataDictService.findListByType(type);
        return list;
    }
}
