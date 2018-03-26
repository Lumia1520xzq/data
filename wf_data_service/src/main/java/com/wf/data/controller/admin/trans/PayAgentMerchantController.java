package com.wf.data.controller.admin.trans;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.trans.entity.TransConvert;
import com.wf.data.service.TransConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/3/28
 */
@RestController
@RequestMapping(value = "/data/admin/trans/payAgentMerchant")
public class PayAgentMerchantController extends ExtJsController {

    @Autowired
    private TransConvertService transConvertService;

    @ResponseBody
    @RequestMapping("list")
    public Object listData() {
        JSONObject json = getRequestJson();
        String merchantCode = null;
        String startTime = null;
        String endTime = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            startTime = data.getString("startTime");
            merchantCode = data.getString("merchantCode");
            endTime = data.getString("endTime");
        }

        //设置默认搜索时间为一个月
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            startTime = DateUtils.getDate();
            endTime = DateUtils.getDate(DateUtils.DATE_PATTERN);
        }

        TransConvert transConvert = new TransConvert();
        transConvert.setMerchantCode(merchantCode);
        transConvert.setStartTime(startTime);
        transConvert.setEndTime(endTime);
        return transConvertService.getByPayAgentMerchant(transConvert);
    }

    /**
     * 查询列表
     */
    @RequestMapping("/sumData")
    public Object sumData(@RequestBody TransConvert transConvert) {
        if (StringUtils.isNotBlank(transConvert.getStartTime())) {
            transConvert.setStartTime(transConvert.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(transConvert.getEndTime())) {
            transConvert.setEndTime(transConvert.getEndTime() + " 23:59:59");
        }
        Map<String,Object> mapParam = new HashMap<>();
        mapParam.put("startTime",transConvert.getStartTime());
        mapParam.put("endTime",transConvert.getEndTime());
        mapParam.put("merchantCode",transConvert.getMerchantCode());
        Double data = transConvertService.sumDataByConds(mapParam);
        return data == null ? 0D : data;
    }

}
