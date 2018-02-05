package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredArpu;
import com.wf.data.dao.data.entity.DatawareFinalRegisteredRetention;
import com.wf.data.service.data.DatawareFinalRegisteredArpuService;
import com.wf.data.service.data.DatawareFinalRegisteredRetentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author JoeH
 * 2018/02/02
 */
@RestController
@RequestMapping(value = "/data/arpu/view")
public class ArpuViewController extends ExtJsController {


    @Autowired
    private DatawareFinalRegisteredArpuService arpuService;
    @Autowired
    private DatawareFinalRegisteredRetentionService retentionService;



    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String beginDate = null;
        String endDate = null;
        List<DatawareFinalRegisteredArpu> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        List<String> datelist = Lists.newArrayList();
        if (data != null) {
            parentId = data.getLong("parentId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endDate = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                datelist.add(beginDate);
            } else {
                if(DateUtils.getDateInterval(beginDate,endDate)>=7){
                    endDate = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(beginDate), 6));
                }
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>(3);
        if (parentId == null){
            params.put("parentId", 1);
        }else{
            params.put("parentId", parentId);
        }
        for (String searchDate : datelist) {
            params.put("businessDate", searchDate);
            DatawareFinalRegisteredArpu arpu = arpuService.getArpuByDate(params);
            DatawareFinalRegisteredRetention retention = retentionService.getRetentionByDate(params);
            DatawareFinalRegisteredArpu record = new DatawareFinalRegisteredArpu();
            if(arpu != null){
                record.setChannelName(arpu.getChannelName());
                record.setBusinessDate(arpu.getBusinessDate());
                record.setNewUsers(arpu.getNewUsers());
                record.setArpu1(arpu.getArpu1());
                record.setArpu2(arpu.getArpu2());
                record.setArpu3(arpu.getArpu3());
                record.setArpu4(arpu.getArpu4());
                record.setArpu5(arpu.getArpu5());
                record.setArpu6(arpu.getArpu6());
                record.setArpu7(arpu.getArpu7());
                record.setArpu15(arpu.getArpu15());
                record.setArpu30(arpu.getArpu30());
                record.setArpu60(arpu.getArpu60());
                record.setArpu90(arpu.getArpu90());
                if(retention != null){
                    record.setRetention2(cal(retention.getRetention2()));
                    record.setRetention3(cal(retention.getRetention3()));
                    record.setRetention4(cal(retention.getRetention4()));
                    record.setRetention5(cal(retention.getRetention5()));
                    record.setRetention6(cal(retention.getRetention6()));
                    record.setRetention7(cal(retention.getRetention7()));
                    record.setRetention8(cal(retention.getRetention8()));
                    record.setRetention9(cal(retention.getRetention9()));
                    record.setRetention10(cal(retention.getRetention10()));
                    record.setRetention11(cal(retention.getRetention11()));
                    record.setRetention12(cal(retention.getRetention12()));
                    record.setRetention13(cal(retention.getRetention13()));
                    record.setRetention14(cal(retention.getRetention14()));
                    record.setRetention15(cal(retention.getRetention15()));
                }
                list.add(record);
            }

        }
        Collections.reverse(list);
        return list;
    }

    private static double cal(double num){
        return BigDecimalUtil.round(BigDecimalUtil.mul(num ,100),2);
    }

}
