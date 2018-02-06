package com.wf.data.controller.admin.platform;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareThirdBettingRecord;
import com.wf.data.service.data.DatawareThirdBettingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
        Integer gameType = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            gameType = data.getInteger("gameType");
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
        record.setGameType(gameType);

        Page<DatawareThirdBettingRecord> page = new Page<DatawareThirdBettingRecord>(record);
        return dataGrid(datawareThirdBettingRecordService.findPage(page));
    }

    /**
     * 查询列表
     */
    @RequestMapping("/sumData")
    public Object sumData(@RequestBody Map<String, Object> dataParam) {
        Map<String, Object> data = new HashMap<>();
        DatawareThirdBettingRecord record = datawareThirdBettingRecordService.sumDataByConds(dataParam);
        if (null == record) {
            data.put("bettingData", 0.00);
            data.put("returnData", 0.00);
        } else {
            if (null == record.getBettingAmount()) {
                data.put("bettingData", 0.00);
            } else {
                data.put("bettingData", record.getBettingAmount());
            }
            if (null == record.getResultAmount()) {
                data.put("returnData", 0.00);
            } else {
                data.put("returnData", record.getResultAmount());
            }

        }
        return data;
    }


}
