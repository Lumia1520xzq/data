package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sun.scenario.effect.impl.prism.ps.PPStoPSWDisplacementMapPeer;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareFinalEntranceAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcs
 * 2018/02/02
 */
@RestController
@RequestMapping(value = "/data/entrance/analysis")
public class EntranceAnalysisController extends ExtJsController{

    @Autowired
    private DatawareFinalEntranceAnalysisService datawareFinalEntranceAnalysisService;
    @Autowired
    private DataDictService dictService;
    private static final Long[] eventIds = {139904L,139902L,139901L,139910L,139905L,139906L,139907L,139903L,139909L,139908L};


    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String businessDate, HttpServletResponse response) {
        List<DatawareFinalEntranceAnalysis> list = Lists.newArrayList();
        DatawareFinalEntranceAnalysis dto = new DatawareFinalEntranceAnalysis();
        dto.setBusinessDate(businessDate);
        try {
            list = datawareFinalEntranceAnalysisService.findList(dto, 1000);
            String fileName = "奖多多各入口用户分析表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("奖多多各入口用户分析表", DatawareFinalEntranceAnalysis.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 奖多多各入口分析
     */
    @RequestMapping("/getList")
    public Object getList() {

        JSONObject json = getRequestJson();
        String searchDate = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            searchDate = data.getString("searchDate");
        }
        try {
            if (StringUtils.isBlank(searchDate)) {
                searchDate = DateUtils.getYesterdayDate();
            } else if (DateUtils.parseDate(searchDate).after(DateUtils.parseDate(DateUtils.getYesterdayDate()))){
                searchDate = DateUtils.getYesterdayDate();
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>(2);
        //开始时间往前推14天
        String beginDate = DateUtils.getPrevDate(searchDate,14);
        params.put("beginDate",beginDate);
        params.put("endate",searchDate);
        //所有的数据
        List<DatawareFinalEntranceAnalysis> analysisList = datawareFinalEntranceAnalysisService.getAnalysisListByDate(params);
        //查找出最后一天的数据
        List<DatawareFinalEntranceAnalysis> lastList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(analysisList)){
            for(DatawareFinalEntranceAnalysis record:analysisList){
                if(searchDate.equals(record.getBusinessDate())){
                    lastList.add(record);
                }
                if(!dateList.contains(sub(record.getBusinessDate()))){
                    dateList.add(sub(record.getBusinessDate()));
                }
            }
        }
        List<Double> entranceDauRateList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        for(DatawareFinalEntranceAnalysis record:lastList){
            entranceDauRateList.add(record.getEntranceDauRate());
            eventNameList.add(record.getEventName());
        }
        //查找每天页多多游戏的dau
        List<Long> yeDuoDuoDauList = new ArrayList<>();
        //页多多游戏入口dau占比
        List<Double> yeDuoduoEntranceDauRate = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(analysisList)){
            for(DatawareFinalEntranceAnalysis record:analysisList){
                if(eventIds[0].equals(record.getEventId())){
                    yeDuoDuoDauList.add(record.getEntranceDau());
                    yeDuoduoEntranceDauRate.add(record.getEntranceDauRate());
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("entranceDauRateList",entranceDauRateList);
        map.put("eventNameList",eventNameList);
        map.put("searchDate",searchDate);
        map.put("yeDuoDuoDauList",yeDuoDuoDauList);
        map.put("yeDuoduoEntranceDauRate",yeDuoduoEntranceDauRate);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }

    private static String sub(String date){
        if(StringUtils.isBlank(date)){
            return null;
        }
        int index = date.indexOf("-");
        return date.substring(index+1);
    }


}

