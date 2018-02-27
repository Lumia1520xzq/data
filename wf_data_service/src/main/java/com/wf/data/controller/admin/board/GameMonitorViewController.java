package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.prism.ps.PPStoPSWDisplacementMapPeer;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareGameBettingInfoHourService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 游戏数据每小时预警
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/game/view")
public class GameMonitorViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareGameBettingInfoHourService bettingInfoService;

    /**
     * 游戏数据小时预警
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        String businessDate = null;
        Integer gameType = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            businessDate = data.getString("businessDate");
            gameType = data.getInteger("gameType");
        }
        try {
            Date now = new Date();
            if (StringUtils.isBlank(businessDate)||businessDate.equals(DateUtils.formatDate(now))) {
                businessDate = getDate();
            }else if(DateUtils.parseDate(businessDate).after(now)){
                businessDate = getDate();
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
        if (null == gameType){
            gameType = 0;
        }
        params.put("gameType",gameType);
        //今天的数据
        params.put("businessDate",businessDate);
        List<DatawareGameBettingInfoHour> todData = bettingInfoService.getByDateAndHour(params);
        //昨天的数据
        params.put("businessDate",DateUtils.getPrevDate(businessDate,1));
        List<DatawareGameBettingInfoHour>  yesData = bettingInfoService.getByDateAndHour(params);
        //计算今天和昨天数据的流水差和返奖率
        if(CollectionUtils.isNotEmpty(todData)){
            for(DatawareGameBettingInfoHour tod:todData){
                tod.setHourMoneyGap(tod.getHourBettingAmount()-tod.getHourReturnAmount());
                tod.setHourReturnRate(calRate(tod.getHourReturnAmount(),tod.getHourBettingAmount()));
                tod.setMoneyGap(tod.getBettingAmount()-tod.getReturnAmount());
                tod.setReturnRate(calRate(tod.getReturnAmount(),tod.getBettingAmount()));
            }
        }
        if(CollectionUtils.isNotEmpty(yesData)){
            for(DatawareGameBettingInfoHour yes:yesData){
                yes.setHourMoneyGap(yes.getHourBettingAmount()-yes.getHourReturnAmount());
                yes.setHourReturnRate(calRate(yes.getHourReturnAmount(),yes.getHourBettingAmount()));
                yes.setMoneyGap(yes.getBettingAmount()-yes.getReturnAmount());
                yes.setReturnRate(calRate(yes.getReturnAmount(),yes.getBettingAmount()));
            }
        }

        //获取今天最后一条记录
        if(CollectionUtils.isNotEmpty(todData)){
            DatawareGameBettingInfoHour lastRecord = todData.get(todData.size()-1);
            String hour = lastRecord.getBusinessHour();
            //获取昨天相同时间点的数据
            params.put("businessHour",hour);
            List<DatawareGameBettingInfoHour> yesLastRecordList = bettingInfoService.getByDateAndHour(params);
            DatawareGameBettingInfoHour yesLastRecord = new DatawareGameBettingInfoHour();
            yesLastRecord.init(yesLastRecord);
            if(CollectionUtils.isNotEmpty(yesLastRecordList)){
                yesLastRecord = yesLastRecordList.get(0);
            }
            String dayDauRate = cal(lastRecord.getDau(),yesLastRecord.getDau());
            String dayUserCountRate = cal(lastRecord.getBettingUserCount(),yesLastRecord.getBettingUserCount());
            String dayBettingCountRate = cal(lastRecord.getBettingCount(),yesLastRecord.getBettingCount());
            String dayBettingAmountRate = cal(lastRecord.getBettingAmount(),yesLastRecord.getBettingAmount());
            String dayMoneyGapRate = cal(lastRecord.getMoneyGap(),yesLastRecord.getBettingAmount()-yesLastRecord.getReturnAmount());
            String dayReturnRate = cal(lastRecord.getReturnRate(),calRate(yesLastRecord.getReturnAmount(),yesLastRecord.getBettingAmount()));

            //获取一周前相同时间点的数据
            params.put("businessDate",DateUtils.getPrevDate(businessDate,7));
            List<DatawareGameBettingInfoHour> weekLastRecordList = bettingInfoService.getByDateAndHour(params);
            DatawareGameBettingInfoHour weekLastRecord = new DatawareGameBettingInfoHour();
            weekLastRecord.init(weekLastRecord);
            if(CollectionUtils.isNotEmpty(weekLastRecordList)){
                weekLastRecord = weekLastRecordList.get(0);
            }

            String weekDauRate = cal(lastRecord.getDau(),weekLastRecord.getDau());
            String weekUserCountRate = cal(lastRecord.getBettingUserCount(),weekLastRecord.getBettingUserCount());
            String weekBettingCountRate = cal(lastRecord.getBettingCount(),weekLastRecord.getBettingCount());
            String weekBettingAmountRate = cal(lastRecord.getBettingAmount(),weekLastRecord.getBettingAmount());
            String weekMoneyGapRate = cal(lastRecord.getMoneyGap(),weekLastRecord.getBettingAmount()-weekLastRecord.getReturnAmount());
            String weekReturnRate = cal(lastRecord.getReturnRate(),calRate(weekLastRecord.getReturnAmount(),weekLastRecord.getBettingAmount()));

            lastRecord.setDayDauRate(dayDauRate);
            lastRecord.setDayUserCountRate(dayUserCountRate);
            lastRecord.setDayBettingCountRate(dayBettingCountRate);
            lastRecord.setDayBettingAmountRate(dayBettingAmountRate);
            lastRecord.setDayMoneyGapRate(dayMoneyGapRate);
            lastRecord.setDayReturnRate(dayReturnRate);

            lastRecord.setWeekDauRate(weekDauRate);
            lastRecord.setWeekUserCountRate(weekUserCountRate);
            lastRecord.setWeekBettingCountRate(weekBettingCountRate);
            lastRecord.setWeekBettingAmountRate(weekBettingAmountRate);
            lastRecord.setWeekMoneyGapRate(weekMoneyGapRate);
            lastRecord.setWeekReturnRate(weekReturnRate);

            todData.set(todData.size()-1,lastRecord);
        }

        //历史七天的数据
        params.put("beginDate",DateUtils.getPrevDate(businessDate,7));
        params.put("endDate",DateUtils.getPrevDate(businessDate,1));
        List<DatawareGameBettingInfoHour> historyOriginalList = bettingInfoService.getSumDataByDateAndHour(params);
        List<DatawareGameBettingInfoHour> historyData = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(historyOriginalList)) {
            for(DatawareGameBettingInfoHour record:historyOriginalList){
                DatawareGameBettingInfoHour info = new DatawareGameBettingInfoHour();
                info.setBusinessHour(record.getBusinessHour());
                info.setHourDau(record.getHourDau()/7);
                info.setHourBettingUserCount(record.getHourBettingUserCount()/7);
                info.setHourBettingCount(record.getHourBettingCount()/7);
                info.setHourBettingAmount(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourBettingAmount(),7),0));
                info.setHourMoneyGap(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourBettingAmount()-record.getHourReturnAmount(),7),0));
                info.setHourReturnRate(BigDecimalUtil.round(BigDecimalUtil.div(record.getHourReturnAmount()*100,record.getHourBettingAmount()),2));
                historyData.add(info);
            }
        }
        Map<String,Object> map = new HashMap<>(3);
        map.put("todData",todData);
        map.put("yesData",yesData);
        map.put("historyData",historyData);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }


    private static String getDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR,-1);
        Date date = cal.getTime();
        return DateUtils.formatDate(date,DateUtils.DATE_PATTERN);
    }

    private  String cal(Long last,Long notlast){
        if(null == notlast || 0 == notlast){
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }

    private  String cal(Double last,Double notlast){
        if(null == notlast || 0 == notlast) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last-notlast,notlast),"#.##%");
    }

    private static double calRate(Double one,Double two){
        if(two == null || two == 0L){
            return 0.0;
        }
        if(one == null || one == 0L){
            return 0.0;
        }
        return BigDecimalUtil.round(BigDecimalUtil.div(one*100 ,two),2);
    }


}
