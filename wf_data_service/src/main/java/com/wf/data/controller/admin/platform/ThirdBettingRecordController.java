package com.wf.data.controller.admin.platform;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareThirdBettingRecord;
import com.wf.data.service.data.DatawareThirdBettingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方投注数据
 *
 * @author lcs
 */
@RestController
@RequestMapping("/data/admin/platform/record")
public class ThirdBettingRecordController extends ExtJsController {

    @Autowired
    private DatawareThirdBettingRecordService datawareThirdBettingRecordService;

    /**
     * 根据类型获取列表
     *
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {


        JSONObject json = getRequestJson();
        Long channelId = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            channelId = data.getLong("channelId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            startTime = DateUtils.getYesterdayDate();
            endTime = DateUtils.getYesterdayDate();
        }
        DatawareThirdBettingRecord record = new DatawareThirdBettingRecord();
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        record.setChannelId(channelId);

        Page<DatawareThirdBettingRecord> page = new Page<DatawareThirdBettingRecord>(record);
        return dataGrid(datawareThirdBettingRecordService.findPage(page));
    }


}
