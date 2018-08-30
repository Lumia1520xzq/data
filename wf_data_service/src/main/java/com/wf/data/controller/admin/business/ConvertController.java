package com.wf.data.controller.admin.business;

import com.wf.core.persistence.Page;
import com.wf.core.utils.type.MapUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.TransConvert;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shihui
 * @date 2017/3/11
 */
@RestController
@RequestMapping(value = "/data/admin/business/convert")
public class ConvertController extends ExtJsController {
    @Autowired
    private TransConvertService transConvertService;
    @Autowired
    private UicUserService uicUserService;

    /**
     * 查询列表
     */
    @RequestMapping("/sumData")
    public Object sumData(@RequestBody Map<String, Object> dataParam) {
        Double data = transConvertService.sumDataByConds(dataParam);
        data = data == null ? 0D : data;
        Double leaf = 0D;
        if (dataParam.get("merchantCode") == null || StringUtils.EMPTY.equals(dataParam.get("merchantCode"))) {
            dataParam.put("merchantCode", "h5gameuseleaf");
            leaf = transConvertService.sumDataByConds(dataParam);
            leaf = leaf == null ? 0D : leaf;
        } else if ("h5gameuseleaf".equals(dataParam.get("merchantCode"))) {
            leaf = data;
        }
        return MapUtils.toMap("sumData", data, "sumLeaf", leaf, "sumRmb", data - leaf);
    }

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        Page<TransConvert> page = getPage(TransConvert.class);
        Page<TransConvert> result = transConvertService.findPage(page);
        for (TransConvert item : result.getData()) {
            if (item.getUserId() != null) {
                UicUser user = uicUserService.get(item.getUserId());
                if (null != user) {
                    item.setUserName(user.getNickname());
                }
            }
        }
        return dataGrid(result);

    }

}
