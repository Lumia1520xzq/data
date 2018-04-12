package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.controller.response.FilterDataViewExcelResponse;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelConversion;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelConversionService;
import com.wf.data.service.data.DatawareFinalRechargeTagAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户分层分析
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/user/classify")
public class UserClassifyViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalRechargeTagAnalysisService rechargeTagAnalysisService;

    /**
     * 用户分层数据
     */
    @RequestMapping("/getList")
    public Object getList() {
        Map<String,Object> map = new HashMap<>();
        JSONObject json = getRequestJson();
        Long parentId = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -14));
                endTime = DateUtils.getYesterdayDate();
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                endTime = startTime;
            }

        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>(3);
        if (null == parentId) {
            parentId = 1L;
        }
        ChannelInfo channelInfo = channelInfoService.get(parentId);
        if (null != channelInfo) {
            if (null != channelInfo.getParentId()) {
                params.put("channelId", parentId);
            } else {
                params.put("parentId", parentId);
            }
        } else {
            params.put("parentId", parentId);
        }
        params.put("beginDate", startTime);
        params.put("endDate",endTime);
        //获取日期list
        List<String> dateList =  rechargeTagAnalysisService.getDateList(params);


        List<String> simpleDateList = new ArrayList<>();
        for(String date:dateList){
            int index = date.indexOf("-");
            simpleDateList.add(date.substring(index + 1));
        }

        map.put("dateList",simpleDateList);
        //获取dateList最后一天的日期
        String lastDate = DateUtils.getYesterdayDate();
        if(CollectionUtils.isNotEmpty(dateList)){
            lastDate = dateList.get(dateList.size()-1);
        }
        //截取次日留存的日期
        List<String> dayRetentionDateList = dateList;
        if(!DateUtils.parseDate(lastDate).before(DateUtils.getYesterday()) && CollectionUtils.isNotEmpty(dateList)){
            dayRetentionDateList = dateList.subList(0,dateList.size()-1);
        }

        List<String> simpleDayRetentionDateList = new ArrayList<>();
        for(String date:dayRetentionDateList){
            int index = date.indexOf("-");
            simpleDayRetentionDateList.add(date.substring(index + 1));
        }

        map.put("dayRetentionDateList",simpleDayRetentionDateList);

        //截取七日留存的日期
        List<String> weekRetentionDateList = dateList;
        if(!DateUtils.parseDate(lastDate).before(DateUtils.parseDate(DateUtils.getPrevDate(DateUtils.getYesterdayDate(),6))) && CollectionUtils.isNotEmpty(dateList)){
            if(dateList.size() >= 6){
                weekRetentionDateList = dateList.subList(0,dateList.size()-6);
            }else{
                weekRetentionDateList = new ArrayList<>();
            }
        }

        List<String> simpleWeekRetentionDateList = new ArrayList<>();
        for(String date:weekRetentionDateList){
            int index = date.indexOf("-");
            simpleWeekRetentionDateList.add(date.substring(index + 1));
        }

        map.put("weekRetentionDateList",simpleWeekRetentionDateList);

        //七日流失的日期
        List<String> weekLostDateList = dateList;
        if(!DateUtils.parseDate(lastDate).before(DateUtils.parseDate(DateUtils.getPrevDate(DateUtils.getYesterdayDate(),7))) && CollectionUtils.isNotEmpty(dateList)){
            if(dateList.size() >= 7){
                weekLostDateList = dateList.subList(0,dateList.size()-7);
            }else{
                weekLostDateList = new ArrayList<>();
            }
        }

        List<String> simpleWeekLostDateList = new ArrayList<>();
        for(String date:weekLostDateList){
            int index = date.indexOf("-");
            simpleWeekLostDateList.add(date.substring(index + 1));
        }


        map.put("weekLostDateList",simpleWeekLostDateList);

        // 循环取每个userTag对应的list0
        for (int index=0;index<=6;index++){
            params.put("userTag",index);
            List<DatawareFinalRechargeTagAnalysis> list = rechargeTagAnalysisService.getListByTagAndDate(params);
            map.put(""+index,list);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }



}
