package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
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
        params.put("endDate",searchDate);
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
        List<Double> entranceSignRateList = new ArrayList<>();
        List<Double> entranceBettingRateList = new ArrayList<>();
        List<Double> entrancePayRateList = new ArrayList<>();
        for(DatawareFinalEntranceAnalysis record:lastList){
            entranceDauRateList.add(record.getEntranceDauRate());
            eventNameList.add(record.getEventName());
            entranceSignRateList.add(record.getEntranceSignRate());
            entranceBettingRateList.add(record.getEntranceBettingRate());
            entrancePayRateList.add(record.getEntrancePayRate());
        }

        //查找发现页多多游戏的dau
        List<Long> duoDuoDauList = new ArrayList<>();
        List<Long> iconDauList = new ArrayList<>();
        List<Long> bannerDauList = new ArrayList<>();
        List<Long> advDauList = new ArrayList<>();
        List<Long> gameDauList = new ArrayList<>();
        List<Long> rollDauList = new ArrayList<>();
        List<Long> pointDauList = new ArrayList<>();
        List<Long> actCenterDauList = new ArrayList<>();
        List<Long> pushDauList = new ArrayList<>();
        List<Long> strongAdvDauList = new ArrayList<>();
        //发现页多多游戏入口dau占比
        List<Double> duoduoDauRate = new ArrayList<>();
        List<Double> iconDauRate = new ArrayList<>();
        List<Double> bannerDauRate = new ArrayList<>();
        List<Double> advDauRate = new ArrayList<>();
        List<Double> gameDauRate = new ArrayList<>();
        List<Double> rollDauRate = new ArrayList<>();
        List<Double> pointDauRate = new ArrayList<>();
        List<Double> actCenterDauRate = new ArrayList<>();
        List<Double> pushDauRate = new ArrayList<>();
        List<Double> strongAdvDauRate = new ArrayList<>();
        //入口签到转化率
        List<Double> duoduoSignRate = new ArrayList<>();
        List<Double> iconSignRate = new ArrayList<>();
        List<Double> bannerSignRate = new ArrayList<>();
        List<Double> advSignRate = new ArrayList<>();
        List<Double> gameSignRate = new ArrayList<>();
        List<Double> rollSignRate = new ArrayList<>();
        List<Double> pointSignRate = new ArrayList<>();
        List<Double> actCenterSignRate = new ArrayList<>();
        List<Double> pushSignRate = new ArrayList<>();
        List<Double> strongAdvSignRate = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(analysisList)){
            for(DatawareFinalEntranceAnalysis record:analysisList){
                if(eventIds[0].equals(record.getEventId())){
                    duoDuoDauList.add(record.getEntranceDau());
                    duoduoDauRate.add(record.getEntranceDauRate());
                    duoduoSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[1].equals(record.getEventId())){
                    iconDauList.add(record.getEntranceDau());
                    iconDauRate.add(record.getEntranceDauRate());
                    iconSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[2].equals(record.getEventId())){
                    bannerDauList.add(record.getEntranceDau());
                    bannerDauRate.add(record.getEntranceDauRate());
                    bannerSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[3].equals(record.getEventId())){
                    advDauList.add(record.getEntranceDau());
                    advDauRate.add(record.getEntranceDauRate());
                    advSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[4].equals(record.getEventId())){
                    gameDauList.add(record.getEntranceDau());
                    gameDauRate.add(record.getEntranceDauRate());
                    gameSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[5].equals(record.getEventId())){
                    rollDauList.add(record.getEntranceDau());
                    rollDauRate.add(record.getEntranceDauRate());
                    rollSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[6].equals(record.getEventId())){
                    pointDauList.add(record.getEntranceDau());
                    pointDauRate.add(record.getEntranceDauRate());
                    pointSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[7].equals(record.getEventId())){
                    actCenterDauList.add(record.getEntranceDau());
                    actCenterDauRate.add(record.getEntranceDauRate());
                    actCenterSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[8].equals(record.getEventId())){
                    pushDauList.add(record.getEntranceDau());
                    pushDauRate.add(record.getEntranceDauRate());
                    pushSignRate.add(record.getEntranceSignRate());
                }
                if(eventIds[9].equals(record.getEventId())){
                    strongAdvDauList.add(record.getEntranceDau());
                    strongAdvDauRate.add(record.getEntranceDauRate());
                    strongAdvSignRate.add(record.getEntranceSignRate());
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("entranceDauRateList",entranceDauRateList);
        map.put("eventNameList",eventNameList);
        map.put("searchDate",searchDate);

        map.put("duoDuoDauList",duoDuoDauList);
        map.put("duoduoDauRate",duoduoDauRate);

        map.put("iconDauList",iconDauList);
        map.put("iconDauRate",iconDauRate);

        map.put("bannerDauList",bannerDauList);
        map.put("bannerDauRate",bannerDauRate);

        map.put("advDauList",advDauList);
        map.put("advDauRate",advDauRate);

        map.put("gameDauList",gameDauList);
        map.put("gameDauRate",gameDauRate);

        map.put("rollDauList",rollDauList);
        map.put("rollDauRate",rollDauRate);

        map.put("pointDauList",pointDauList);
        map.put("pointDauRate",pointDauRate);

        map.put("actCenterDauList",actCenterDauList);
        map.put("actCenterDauRate",actCenterDauRate);

        map.put("pushDauList",pushDauList);
        map.put("pushDauRate",pushDauRate);

        map.put("strongAdvDauList",strongAdvDauList);
        map.put("strongAdvDauRate",strongAdvDauRate);

        map.put("entranceSignRateList",entranceSignRateList);
        map.put("entranceBettingRateList",entranceBettingRateList);
        map.put("entrancePayRateList",entrancePayRateList);


        map.put("duoduoSignRate",duoduoSignRate);
        map.put("iconSignRate",iconSignRate);
        map.put("bannerSignRate",bannerSignRate);
        map.put("advSignRate",advSignRate);
        map.put("gameSignRate",gameSignRate);
        map.put("rollSignRate",rollSignRate);
        map.put("pointSignRate",pointSignRate);
        map.put("actCenterSignRate",actCenterSignRate);
        map.put("pushSignRate",pushSignRate);
        map.put("strongAdvSignRate",strongAdvSignRate);

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

