package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 整体数据概览
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/board/view")
public class WholeDataViewController extends ExtJsController {

    @Autowired
    private DatawareFinalChannelInfoAllService datawareFinalChannelInfoAllService;
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelRetentionService datawareFinalChannelRetentionService;
    @Autowired
    private DatawareFinalChannelCostService datawareFinalChannelCostService;

    /**
     * 整体数据概览
     */
    @RequestMapping("/getList")
    public Object getList() {
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
        if(null == parentId){
            parentId = 1L;
        }
        ChannelInfo channelInfo = channelInfoService.get(parentId);
        if (null != channelInfo) {
            if (null != channelInfo.getParentId()) {
                params.put("channelId", parentId);
            } else {
                params.put("parentId", parentId);
            }
        }else {
            params.put("parentId", parentId);
        }
            params.put("beginDate",startTime);
            params.put("endDate",endTime);
            List<DatawareFinalChannelInfoAll> allList = datawareFinalChannelInfoAllService.getListByChannelAndDate(params);
            for(DatawareFinalChannelInfoAll info:allList) {
                String date = info.getBusinessDate();
                params.put("date",date);
                DatawareFinalChannelRetention retention = datawareFinalChannelRetentionService.findByDate(params);
                if(null != retention) {
                    Double usersDayRetention = retention.getUsersDayRetention();
                    Double dayRetention = retention.getDayRetention();
                    Double usersRate = retention.getUsersRate();
                    info.setUsersDayRetention(usersDayRetention);
                    info.setDayRetention(dayRetention);
                    info.setUsersRate(usersRate);
                }else{
                    info.setUsersDayRetention(0.0);
                    info.setDayRetention(0.0);
                    info.setUsersRate(0.0);
                }
                DatawareFinalChannelCost cost = datawareFinalChannelCostService.findByDate(params);
                if(null != cost){
                    Double totalCost = cost.getTotalCost();
                    Double costRate = cost.getCostRate();
                    info.setTotalCost(totalCost);
                    info.setCostRate( BigDecimalUtil.round(BigDecimalUtil.mul(costRate,100),2));
                }
                else{
                    info.setTotalCost(0.0);
                    info.setCostRate(0.0);
                }
            }
            if(CollectionUtils.isNotEmpty(allList)) {
                //获取最后一条记录
                DatawareFinalChannelInfoAll lastInfoAll =  allList.get(allList.size()-1);
                //获取最后一天的日期
                String endDate = lastInfoAll.getBusinessDate();
                //前一天的日期
                String beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),1));
                //前一天的数据
                params.put("date",beforeDate);
                DatawareFinalChannelInfoAll lastButOneInfoAll = datawareFinalChannelInfoAllService.findByDate(params);
                DatawareFinalChannelCost lastButOneCost = datawareFinalChannelCostService.findByDate(params);

                //1、日环比
                if(null != lastButOneInfoAll) {
                String dayDauRate = cal(lastInfoAll.getDau(),lastButOneInfoAll.getDau());
                String dayRechargeAmountRate = cal(lastInfoAll.getRechargeAmount(),lastButOneInfoAll.getRechargeAmount());
                String dayRechargeCountRate = cal(lastInfoAll.getRechargeCount(),lastButOneInfoAll.getRechargeCount());
                String dayNewUsersRate = cal(lastInfoAll.getNewUsers(),lastButOneInfoAll.getNewUsers());
                String dayUserCountRate = cal(lastInfoAll.getUserCount(),lastButOneInfoAll.getUserCount());
                String dayBettingRate = cal(lastInfoAll.getBettingRate(),lastButOneInfoAll.getBettingRate());
                String dayDauPayRate = cal(lastInfoAll.getDauPayRate(),lastButOneInfoAll.getDauPayRate());
                String dayBettingPayRate = cal(lastInfoAll.getBettingPayRate(),lastButOneInfoAll.getBettingPayRate());
                String dayUserBettingRate = cal(lastInfoAll.getUserBettingRate(),lastButOneInfoAll.getUserBettingRate());
                String dayBettingAmountRate = cal(lastInfoAll.getBettingAmount(),lastButOneInfoAll.getBettingAmount());
                String dayResultRate = cal(lastInfoAll.getResultRate(),lastButOneInfoAll.getResultRate());
                String dayPayArpuRate = cal(lastInfoAll.getPayArpu(),lastButOneInfoAll.getPayArpu());
                String dayPayArppuRate = cal(lastInfoAll.getPayArppu(),lastButOneInfoAll.getPayArppu());

                //比较最后一天和昨天
                Date yesterday =  DateUtils.parseDate(DateUtils.getYesterdayDate());
                if(DateUtils.parseDate(endDate).before(yesterday)){
                    DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);
                    if(null != lastButOneRetention){
                        String dayUsersDayRetentionRate = cal(lastInfoAll.getUsersDayRetention(),lastButOneRetention.getUsersDayRetention());
                        String dayDayRetentionRate = cal(lastInfoAll.getDayRetention(),lastButOneRetention.getDayRetention());
                        String dayUsersRate = cal(lastInfoAll.getUsersRate(),lastButOneRetention.getUsersRate());
                        lastInfoAll.setDayUsersDayRetentionRate(dayUsersDayRetentionRate);
                        lastInfoAll.setDayDayRetentionRate(dayDayRetentionRate);
                        lastInfoAll.setDayUsersRate(dayUsersRate);
                    }else{
                        lastInfoAll.setDayUsersDayRetentionRate("0%");
                        lastInfoAll.setDayDayRetentionRate("0%");
                        lastInfoAll.setDayUsersRate("0%");
                    }
                }else{
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),1));
                    params.put("date",beforeDate);
                    DatawareFinalChannelRetention lastRetention = datawareFinalChannelRetentionService.findByDate(params);
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),2));
                    params.put("date",beforeDate);
                    DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);

                    if(null != lastButOneRetention){
                        String dayUsersDayRetentionRate = cal(lastRetention.getUsersDayRetention(),lastButOneRetention.getUsersDayRetention());
                        String dayDayRetentionRate = cal(lastRetention.getDayRetention(),lastButOneRetention.getDayRetention());
                        String dayUsersRate = cal(lastRetention.getUsersRate(),lastButOneRetention.getUsersRate());
                        lastInfoAll.setDayUsersDayRetentionRate(dayUsersDayRetentionRate);
                        lastInfoAll.setDayDayRetentionRate(dayDayRetentionRate);
                        lastInfoAll.setDayUsersRate(dayUsersRate);
                    }else{
                        lastInfoAll.setDayUsersDayRetentionRate("0%");
                        lastInfoAll.setDayDayRetentionRate("0%");
                        lastInfoAll.setDayUsersRate("0%");
                    }
                }

                if(null != lastButOneCost){
                    String dayTotalCost = cal(lastInfoAll.getTotalCost(),lastButOneCost.getTotalCost());
                    String dayCostRate = cal(lastInfoAll.getCostRate()/100,lastButOneCost.getCostRate());
                    lastInfoAll.setDayTotalCost(dayTotalCost);
                    lastInfoAll.setDayCostRate(dayCostRate);
                }else{
                    lastInfoAll.setDayTotalCost("0%");
                    lastInfoAll.setDayCostRate("0%");
                }
                lastInfoAll.setDayDauRate(dayDauRate);
                lastInfoAll.setDayRechargeAmountRate(dayRechargeAmountRate);
                lastInfoAll.setDayRechargeCountRate(dayRechargeCountRate);
                lastInfoAll.setDayNewUsersRate(dayNewUsersRate);
                lastInfoAll.setDayUserCountRate(dayUserCountRate);
                lastInfoAll.setDayBettingRate(dayBettingRate);
                lastInfoAll.setDayDauPayRate(dayDauPayRate);
                lastInfoAll.setDayBettingPayRate(dayBettingPayRate);
                lastInfoAll.setDayUserBettingRate(dayUserBettingRate);
                lastInfoAll.setDayBettingAmountRate(dayBettingAmountRate);
                lastInfoAll.setDayResultRate(dayResultRate);
                lastInfoAll.setDayPayArpuRate(dayPayArpuRate);
                lastInfoAll.setDayPayArppuRate(dayPayArppuRate);
                }
                //一周前的日期
                String weekBeforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),7));
                //一周前的数据
                params.put("date",weekBeforeDate);
                DatawareFinalChannelInfoAll weekInfoAll = datawareFinalChannelInfoAllService.findByDate(params);
                DatawareFinalChannelCost weekCost = datawareFinalChannelCostService.findByDate(params);
                //1、周同比
                if(null != weekInfoAll) {
                    String weekDauRate = cal(lastInfoAll.getDau(),weekInfoAll.getDau());
                    String weekRechargeAmountRate = cal(lastInfoAll.getRechargeAmount(),weekInfoAll.getRechargeAmount());
                    String weekRechargeCountRate = cal(lastInfoAll.getRechargeCount(),weekInfoAll.getRechargeCount());
                    String weekNewUsersRate = cal(lastInfoAll.getNewUsers(),weekInfoAll.getNewUsers());
                    String weekUserCountRate = cal(lastInfoAll.getUserCount(),weekInfoAll.getUserCount());
                    String weekBettingRate = cal(lastInfoAll.getBettingRate(),weekInfoAll.getBettingRate());
                    String weekDauPayRate = cal(lastInfoAll.getDauPayRate(),weekInfoAll.getDauPayRate());
                    String weekBettingPayRate = cal(lastInfoAll.getBettingPayRate(),weekInfoAll.getBettingPayRate());
                    String weekUserBettingRate = cal(lastInfoAll.getUserBettingRate(),weekInfoAll.getUserBettingRate());
                    String weekBettingAmountRate = cal(lastInfoAll.getBettingAmount(),weekInfoAll.getBettingAmount());
                    String weekResultRate = cal(lastInfoAll.getResultRate(),weekInfoAll.getResultRate());
                    String weekPayArpuRate = cal(lastInfoAll.getPayArpu(),weekInfoAll.getPayArpu());
                    String weekPayArppuRate = cal(lastInfoAll.getPayArppu(),weekInfoAll.getPayArppu());



                    //比较最后一天和昨天
                    Date yesterday =  DateUtils.parseDate(DateUtils.getYesterdayDate());
                    if(DateUtils.parseDate(endDate).before(yesterday)){
                        DatawareFinalChannelRetention weekRetention = datawareFinalChannelRetentionService.findByDate(params);
                        if(null != weekRetention){
                            String weekUsersDayRetentionRate = cal(lastInfoAll.getUsersDayRetention(),weekRetention.getUsersDayRetention());
                            String weekDayRetentionRate = cal(lastInfoAll.getDayRetention(),weekRetention.getDayRetention());
                            String weekUsersRate = cal(lastInfoAll.getUsersRate(),weekRetention.getUsersRate());
                            lastInfoAll.setWeekUsersDayRetentionRate(weekUsersDayRetentionRate);
                            lastInfoAll.setWeekDayRetentionRate(weekDayRetentionRate);
                            lastInfoAll.setWeekUsersRate(weekUsersRate);
                        }else{
                            lastInfoAll.setWeekUsersDayRetentionRate("0%");
                            lastInfoAll.setWeekDayRetentionRate("0%");
                            lastInfoAll.setWeekUsersRate("0%");
                        }
                    } else {
                        beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),1));
                        params.put("date",beforeDate);
                        DatawareFinalChannelRetention lastRetention = datawareFinalChannelRetentionService.findByDate(params);
                        beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate),8));
                        params.put("date",beforeDate);
                        DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);
                        if(null != lastButOneRetention){
                            String weekUsersDayRetentionRate = cal(lastRetention.getUsersDayRetention(),lastButOneRetention.getUsersDayRetention());
                            String weekDayRetentionRate = cal(lastRetention.getDayRetention(),lastButOneRetention.getDayRetention());
                            String weekUsersRate = cal(lastRetention.getUsersRate(),lastButOneRetention.getUsersRate());
                            lastInfoAll.setWeekUsersDayRetentionRate(weekUsersDayRetentionRate);
                            lastInfoAll.setWeekDayRetentionRate(weekDayRetentionRate);
                            lastInfoAll.setWeekUsersRate(weekUsersRate);
                        }else{
                            lastInfoAll.setWeekUsersDayRetentionRate("0%");
                            lastInfoAll.setWeekDayRetentionRate("0%");
                            lastInfoAll.setWeekUsersRate("0%");
                        }
                    }


                    if(null != weekCost) {
                        String weekTotalCost = cal(lastInfoAll.getTotalCost(), weekCost.getTotalCost());
                        String weekCostRate = cal(lastInfoAll.getCostRate()/100, weekCost.getCostRate());
                        lastInfoAll.setWeekTotalCost(weekTotalCost);
                        lastInfoAll.setWeekCostRate(weekCostRate);
                    }else{
                        lastInfoAll.setWeekTotalCost("0%");
                        lastInfoAll.setWeekCostRate("0%");
                    }
                    lastInfoAll.setWeekDauRate(weekDauRate);
                    lastInfoAll.setWeekRechargeAmountRate(weekRechargeAmountRate);
                    lastInfoAll.setWeekRechargeCountRate(weekRechargeCountRate);
                    lastInfoAll.setWeekNewUsersRate(weekNewUsersRate);
                    lastInfoAll.setWeekUserCountRate(weekUserCountRate);
                    lastInfoAll.setWeekBettingRate(weekBettingRate);
                    lastInfoAll.setWeekDauPayRate(weekDauPayRate);
                    lastInfoAll.setWeekBettingPayRate(weekBettingPayRate);
                    lastInfoAll.setWeekUserBettingRate(weekUserBettingRate);
                    lastInfoAll.setWeekBettingAmountRate(weekBettingAmountRate);
                    lastInfoAll.setWeekResultRate(weekResultRate);
                    lastInfoAll.setWeekPayArpuRate(weekPayArpuRate);
                    lastInfoAll.setWeekPayArppuRate(weekPayArppuRate);
                }
            }
            return  allList;
    }


    private  String cal(Long last,Long notlast){
        if(0 == notlast){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }
    private  String cal(Double last,Double notlast){
        if(0 == notlast){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }


}
