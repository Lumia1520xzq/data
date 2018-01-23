package com.wf.data.controller.admin.business;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
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
 * @author chris
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
        return data == null ? 0D : data;
    }

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        JSONObject json = getRequestJson();
        Long channelId = null;
        Long parentId = null;
        Long userId = null;
        Integer bizType = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userId = data.getLong("userId");
            bizType = data.getInteger("bizType");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        TransConvert record = new TransConvert();
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        record.setChannelId(channelId);
        record.setParentId(parentId);
        record.setUserId(userId);
        record.setBizType(bizType);
        Page<TransConvert> page = new Page<>(record);
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
