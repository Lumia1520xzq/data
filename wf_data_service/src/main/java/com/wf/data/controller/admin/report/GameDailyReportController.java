package com.wf.data.controller.admin.report;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dto.DailyReportDto;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 游戏数据日报表
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/admin/report")
public class GameDailyReportController extends ExtJsController {

    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;
//    @Autowired
//    private EsUicAllGameService esUicAllGameService;

    /**
     * 返回日报表列表
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Integer gameType = null;
        Long parentId = null;
        String startTime = null;
        String endTime = null;
        List<DailyReportDto> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        List<String> datelist = Lists.newArrayList();
        if (data != null) {
            gameType = data.getInteger("gameType");
            parentId = data.getLong("parentId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
                endTime = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(startTime, endTime);
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
                datelist.add(startTime);
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                endTime = startTime;
                datelist.add(endTime);
            } else {
                datelist = DateUtils.getDateList(startTime, endTime);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>(5);
        params.put("gameType", gameType);
        params.put("parentId",parentId);
        for (String searchDate : datelist) {
            DailyReportDto dto = new DailyReportDto();
            params.put("searchDate", searchDate);
            //3、DAU
            Integer dau = datawareBuryingPointDayService.getGameDau(params);
            //4、导入率(游戏dau/平台dau)
            params.put("gameType",null);
            Integer platDau = datawareBuryingPointDayService.getGameDau(params);
            String importRate = cal(dau,platDau);
            // 投注情况
            params.put("gameType", gameType);
            TcardDto tcardDto = datawareBettingLogDayService.getTcardBettingByday(params);
            //5、投注人数
            Integer bettingUserCount = tcardDto.getUserCount();
            //6、投注笔数
            Integer bettingCount = tcardDto.getBettingCount();
            //7、投注流水
            Double bettingAmount = tcardDto.getBettingAmount();
            Double returnAmount = tcardDto.getResultAmount();
            //8、流水差(投注-返奖)
            Double amountGap = bettingAmount - returnAmount;
            //9、投注转化率(投注人数/dau)
            String bettingRate = cal(bettingUserCount,dau);
            //10、返奖率(返奖流水/投注流水)
            String returnRate = cal(returnAmount,bettingAmount);
            //11、投注ARPU(投注流水/投注人数)
            Double arpu =  bettingUserCount == 0?0:BigDecimalUtil.round(BigDecimalUtil.div(bettingAmount,bettingUserCount),1);
            //12、投注ASP(投注流水/投注笔数)
            Double asp = bettingCount == 0?0:BigDecimalUtil.round(BigDecimalUtil.div(bettingAmount,bettingCount),1);
            //13、人均频率(投注笔数/投注人数)
            Double avgBettingCount = bettingUserCount == 0?0:BigDecimalUtil.round(BigDecimalUtil.div(bettingCount,bettingUserCount),1);
            //14、新增用户
            //历史活跃用户id
            //List<Long> historyDauIds = datawareBuryingPointDayService.getHistoryDauIds(params);
            //当天活跃用户
            //List<Long> todayDauIds = datawareBuryingPointDayService.getGameDauIds(params);
            //新增用户
            //List<Long> newUserIds = getNewIds(historyDauIds,todayDauIds);
            //List<Long> newUserIds = esUicAllGameService.getNewUserIds(gameType,searchDate,parentId);
            //Integer newUserCount = newUserIds.size();
            //15、新增投注转化率(新增且投注用户/新增用户)
            //投注用户id
            //List<Long> bettingUserIds = datawareBettingLogDayService.getBettingUserIds(params);
            //String newUserRate = getTransRate(newUserIds,bettingUserIds);
            //16、新增次留(第二天活跃/头一天新增)
            //第二天活跃
            //String nextDay = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(searchDate)));
            //params.put("searchDate", nextDay);
            //List<Long> nextDauIds = datawareBuryingPointDayService.getGameDauIds(params);
            //String newUserRemain = getTransRate(newUserIds,nextDauIds);
            dto.setGameType(gameType);
            dto.setSearchDate(searchDate);
            dto.setDau(dau);
            dto.setImportRate(importRate);
            dto.setBettingUserCount(bettingUserCount);
            dto.setBettingCount(bettingCount);
            dto.setBettingAmount(bettingAmount);
            dto.setAmountGap(amountGap);
            dto.setBettingRate(bettingRate);
            dto.setReturnRate(returnRate);
            dto.setArpu(arpu);
            dto.setAsp(asp);
            dto.setAvgBettingCount(avgBettingCount);
            //dto.setNewUserCount(newUserCount);
            //dto.setNewUserRate(newUserRate);
            //dto.setNewUserRemain(newUserRemain);
            list.add(dto);
        }
        return list;
    }

    private String getTransRate(List<Long> newIds,List<Long> bettingIds){
        if(CollectionUtils.isEmpty(newIds)||CollectionUtils.isEmpty(bettingIds)){
            return "0%";
        }
        List<Long> newAndBettingIds = (List<Long>)CollectionUtils.intersection(newIds,bettingIds);
        return cal(newAndBettingIds.size(),newIds.size());
    }

    private List<Long> getNewIds(List<Long> historyDauIds,List<Long> todayDauIds){
        if(null == historyDauIds){
            historyDauIds = new ArrayList<>();
        }
        if(null == todayDauIds){
            todayDauIds = new ArrayList<>();
        }
        return (List<Long>)CollectionUtils.disjunction(todayDauIds,CollectionUtils.intersection(historyDauIds,todayDauIds));
    }

    private String cal(int dau,int platDau){
        if(0 == platDau){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(dau,platDau),"#.##%");
    }

    private String cal(double returnAmount,double bettingAmount){
        if(0 == bettingAmount){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(returnAmount,bettingAmount),"#.##%");
    }

}
