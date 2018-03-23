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
import com.wf.data.service.data.DatawareFinalEntranceAnalysisService;
import jodd.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * @author lcs
 * 2018/02/02
 */
@RestController
@RequestMapping(value = "/data/entrance/analysis")
public class EntranceAnalysisController extends ExtJsController{

    @Autowired
    private DatawareFinalEntranceAnalysisService datawareFinalEntranceAnalysisService;
    private static final Long[] eventIds = {139904L,139902L,139901L,139910L,139905L,139906L,139907L,139903L,139909L,139908L};

    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@Valid @RequestParam String businessDate, HttpServletResponse response) {
        List<DatawareFinalEntranceAnalysis> list = Lists.newArrayList();
        DatawareFinalEntranceAnalysis dto = new DatawareFinalEntranceAnalysis();
        if(StringUtil.isNotBlank(businessDate) && !"undefined".equals(businessDate)){
            dto.setBusinessDate(DateUtils.formatGTMDate(businessDate));
        }else {
            dto.setBusinessDate(DateUtils.getYesterdayDate());
        }
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
        String activeUserType = null;
        String convertUserType = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            searchDate = data.getString("searchDate");
            activeUserType = data.getString("activeUserType");
            convertUserType = data.getString("convertUserType");
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
        if(activeUserType == null){
            activeUserType = "0";
        }
        if(convertUserType == null){
            convertUserType = "0";
        }
        params.put("activeUserType",activeUserType);
        params.put("convertUserType",convertUserType);
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

        List<String> yesDateList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(analysisList)){
            for(DatawareFinalEntranceAnalysis record:analysisList){
                if(!yesDateList.contains(minusAndSub(record.getBusinessDate()))){
                    yesDateList.add(minusAndSub(record.getBusinessDate()));
                }
            }
        }

        List<Double> entranceDauRateList = new ArrayList<>();
        List<Long> entranceDauList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<Double> entranceSignRateList = new ArrayList<>();
        List<Double> entranceBettingRateList = new ArrayList<>();
        List<Double> entrancePayRateList = new ArrayList<>();
        List<Double> entranceDayRetentionList = new ArrayList<>();
        for(DatawareFinalEntranceAnalysis record:lastList){
            entranceDauRateList.add(record.getEntranceDauRate());
            entranceDauList.add(record.getEntranceDau());
            eventNameList.add(record.getEventName());
            entranceSignRateList.add(record.getEntranceSignRate());
            entranceBettingRateList.add(record.getEntranceBettingRate());
            entrancePayRateList.add(record.getEntrancePayRate());
            entranceDayRetentionList.add(record.getEntranceDayRetention());
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

        List<Double> duoduoBettingRate = new ArrayList<>();
        List<Double> iconBettingRate = new ArrayList<>();
        List<Double> bannerBettingRate = new ArrayList<>();
        List<Double> advBettingRate = new ArrayList<>();
        List<Double> gameBettingRate = new ArrayList<>();
        List<Double> rollBettingRate = new ArrayList<>();
        List<Double> pointBettingRate = new ArrayList<>();
        List<Double> actCenterBettingRate = new ArrayList<>();
        List<Double> pushBettingRate = new ArrayList<>();
        List<Double> strongAdvBettingRate = new ArrayList<>();

        List<Double> duoduoPayRate = new ArrayList<>();
        List<Double> iconPayRate = new ArrayList<>();
        List<Double> bannerPayRate = new ArrayList<>();
        List<Double> advPayRate = new ArrayList<>();
        List<Double> gamePayRate = new ArrayList<>();
        List<Double> rollPayRate = new ArrayList<>();
        List<Double> pointPayRate = new ArrayList<>();
        List<Double> actCenterPayRate = new ArrayList<>();
        List<Double> pushPayRate = new ArrayList<>();
        List<Double> strongAdvPayRate = new ArrayList<>();

        List<Double> duoduoRetentionRate = new ArrayList<>();
        List<Double> iconRetentionRate = new ArrayList<>();
        List<Double> bannerRetentionRate = new ArrayList<>();
        List<Double> advRetentionRate = new ArrayList<>();
        List<Double> gameRetentionRate = new ArrayList<>();
        List<Double> rollRetentionRate = new ArrayList<>();
        List<Double> pointRetentionRate = new ArrayList<>();
        List<Double> actCenterRetentionRate = new ArrayList<>();
        List<Double> pushRetentionRate = new ArrayList<>();
        List<Double> strongAdvRetentionRate = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(analysisList)){
            for(DatawareFinalEntranceAnalysis record:analysisList){
                if(eventIds[0].equals(record.getEventId())){
                    duoDuoDauList.add(record.getEntranceDau());
                    duoduoDauRate.add(record.getEntranceDauRate());
                    duoduoSignRate.add(record.getEntranceSignRate());
                    duoduoBettingRate.add(record.getEntranceBettingRate());
                    duoduoPayRate.add(record.getEntrancePayRate());
                    duoduoRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[1].equals(record.getEventId())){
                    iconDauList.add(record.getEntranceDau());
                    iconDauRate.add(record.getEntranceDauRate());
                    iconSignRate.add(record.getEntranceSignRate());
                    iconBettingRate.add(record.getEntranceBettingRate());
                    iconPayRate.add(record.getEntrancePayRate());
                    iconRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[2].equals(record.getEventId())){
                    bannerDauList.add(record.getEntranceDau());
                    bannerDauRate.add(record.getEntranceDauRate());
                    bannerSignRate.add(record.getEntranceSignRate());
                    bannerBettingRate.add(record.getEntranceBettingRate());
                    bannerPayRate.add(record.getEntrancePayRate());
                    bannerRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[3].equals(record.getEventId())){
                    advDauList.add(record.getEntranceDau());
                    advDauRate.add(record.getEntranceDauRate());
                    advSignRate.add(record.getEntranceSignRate());
                    advBettingRate.add(record.getEntranceBettingRate());
                    advPayRate.add(record.getEntrancePayRate());
                    advRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[4].equals(record.getEventId())){
                    gameDauList.add(record.getEntranceDau());
                    gameDauRate.add(record.getEntranceDauRate());
                    gameSignRate.add(record.getEntranceSignRate());
                    gameBettingRate.add(record.getEntranceBettingRate());
                    gamePayRate.add(record.getEntrancePayRate());
                    gameRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[5].equals(record.getEventId())){
                    rollDauList.add(record.getEntranceDau());
                    rollDauRate.add(record.getEntranceDauRate());
                    rollSignRate.add(record.getEntranceSignRate());
                    rollBettingRate.add(record.getEntranceBettingRate());
                    rollPayRate.add(record.getEntrancePayRate());
                    rollRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[6].equals(record.getEventId())){
                    pointDauList.add(record.getEntranceDau());
                    pointDauRate.add(record.getEntranceDauRate());
                    pointSignRate.add(record.getEntranceSignRate());
                    pointBettingRate.add(record.getEntranceBettingRate());
                    pointPayRate.add(record.getEntrancePayRate());
                    pointRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[7].equals(record.getEventId())){
                    actCenterDauList.add(record.getEntranceDau());
                    actCenterDauRate.add(record.getEntranceDauRate());
                    actCenterSignRate.add(record.getEntranceSignRate());
                    actCenterBettingRate.add(record.getEntranceBettingRate());
                    actCenterPayRate.add(record.getEntrancePayRate());
                    actCenterRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[8].equals(record.getEventId())){
                    pushDauList.add(record.getEntranceDau());
                    pushDauRate.add(record.getEntranceDauRate());
                    pushSignRate.add(record.getEntranceSignRate());
                    pushBettingRate.add(record.getEntranceBettingRate());
                    pushPayRate.add(record.getEntrancePayRate());
                    pushRetentionRate.add(record.getEntranceDayRetention());
                }
                if(eventIds[9].equals(record.getEventId())){
                    strongAdvDauList.add(record.getEntranceDau());
                    strongAdvDauRate.add(record.getEntranceDauRate());
                    strongAdvSignRate.add(record.getEntranceSignRate());
                    strongAdvBettingRate.add(record.getEntranceBettingRate());
                    strongAdvPayRate.add(record.getEntrancePayRate());
                    strongAdvRetentionRate.add(record.getEntranceDayRetention());
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        String yesterday = DateUtils.getPrevDate(searchDate,1);

        map.put("dateList",dateList);
        map.put("yesDateList",yesDateList);
        map.put("entranceDauRateList",entranceDauRateList);
        map.put("entranceDauList",entranceDauList);
        map.put("eventNameList",eventNameList);
        map.put("searchDate",searchDate);
        map.put("yesterday",yesterday);

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
        map.put("entranceDayRetentionList",entranceDayRetentionList);

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

        map.put("duoduoBettingRate",duoduoBettingRate);
        map.put("iconBettingRate",iconBettingRate);
        map.put("bannerBettingRate",bannerBettingRate);
        map.put("advBettingRate",advBettingRate);
        map.put("gameBettingRate",gameBettingRate);
        map.put("rollBettingRate",rollBettingRate);
        map.put("pointBettingRate",pointBettingRate);
        map.put("actCenterBettingRate",actCenterBettingRate);
        map.put("pushBettingRate",pushBettingRate);
        map.put("strongAdvBettingRate",strongAdvBettingRate);

        map.put("duoduoPayRate",duoduoPayRate);
        map.put("iconPayRate",iconPayRate);
        map.put("bannerPayRate",bannerPayRate);
        map.put("advPayRate",advPayRate);
        map.put("gamePayRate",gamePayRate);
        map.put("rollPayRate",rollPayRate);
        map.put("pointPayRate",pointPayRate);
        map.put("actCenterPayRate",actCenterPayRate);
        map.put("pushPayRate",pushPayRate);
        map.put("strongAdvPayRate",strongAdvPayRate);

        map.put("duoduoRetentionRate",duoduoRetentionRate);
        map.put("iconRetentionRate",iconRetentionRate);
        map.put("bannerRetentionRate",bannerRetentionRate);
        map.put("advRetentionRate",advRetentionRate);
        map.put("gameRetentionRate",gameRetentionRate);
        map.put("rollRetentionRate",rollRetentionRate);
        map.put("pointRetentionRate",pointRetentionRate);
        map.put("actCenterRetentionRate",actCenterRetentionRate);
        map.put("pushRetentionRate",pushRetentionRate);
        map.put("strongAdvRetentionRate",strongAdvRetentionRate);

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


    private static String minusAndSub(String date){
        if(StringUtils.isBlank(date)){
            return null;
        }
        //往前推一天
        String yesterday =  DateUtils.getPrevDate(date,1);
        int index = yesterday.indexOf("-");
        return yesterday.substring(index+1);
    }

}

