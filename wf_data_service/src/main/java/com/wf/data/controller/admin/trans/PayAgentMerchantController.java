package com.wf.data.controller.admin.trans;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.trans.entity.TransConvert;
import com.wf.data.service.TransConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            startTime = DateUtils.getYesterdayDate();
            endTime = DateUtils.getDate(DateUtils.DATE_PATTERN);
        }

        TransConvert transConvert = new TransConvert();
        transConvert.setMerchantCode(merchantCode);
        transConvert.setStartTime(startTime);
        transConvert.setEndTime(endTime);

        return transConvertService.getByPayAgentMerchant(transConvert);
    }

}
