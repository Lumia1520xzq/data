package com.wf.data.controller.admin.tcard;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.TcardUserBettingLogService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 乐盈三张
 *
 * @author lcs
 */
@RestController
@RequestMapping("/data/admin/tcard")
public class TcardController extends ExtJsController {

    @Autowired
    private TcardUserBettingLogService tcardUserBettingLogService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;

    private static final String DATE = "2018-02-01";

    /**
     * 根据类型获取列表
     *
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        List<TcardDto> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
        }
        Map<String, Object> params = new HashMap<>();
        for (String searchDate : DateUtils.getDateList(data)) {
            params.put("searchDate", searchDate);
            if (null != channelId) {
                params.put("channelId", channelId);
            }
            if (null != parentId && null == channelId) {
                params.put("parentId", parentId);
            }
            params.put("gameType", 11);
            TcardDto dto = datawareBettingLogDayService.getTcardBettingByday(params);

            if (null != dto) {
                dto.setSearchDate(searchDate);

                if (null == dto.getBettingAmount()) {
                    dto.setBettingAmount(0.00);
                }
                if (null == dto.getResultAmount()) {
                    dto.setResultAmount(0.00);
                }
                if (null == dto.getUserCount()) {
                    dto.setUserCount(0);
                }
                if (null == dto.getBettingCount()) {
                    dto.setBettingCount(0);
                }

                dto.setAmountDiff(dto.getBettingAmount() - dto.getResultAmount());

                Integer dauCount = datawareBuryingPointDayService.getGameDau(params);
                if (null == dauCount) dauCount = 0;
                dto.setDauCount(dauCount);
                double conversionRate = 0.00;
                double returnRate1 = 0.00;
                double returnRate2 = 0.00;
                Double bettingArpu = 0.00;
                Double bettingAsp = 0.00;
                Double tableAmount = 0.00;
                if (0 != dauCount) {
                    conversionRate = BigDecimalUtil.round(BigDecimalUtil.div(dto.getUserCount().doubleValue() * 100, dauCount.doubleValue()), 2);
                }
                dto.setConversionRate(String.valueOf(conversionRate) + "%");
                if (0 != dto.getBettingAmount()) {
                    params.put("bettingType", 1);
                    params.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                    params.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                    tableAmount = tcardUserBettingLogService.getTableAmount(params);
                }
                if (null != tableAmount) {
                    dto.setTableAmount(BigDecimalUtil.round(tableAmount, 2));
                }

                if (0 != dto.getBettingAmount()) {
                    returnRate1 = dto.getResultAmount() / dto.getBettingAmount() * 100;
                }
                double beetingAmount = dto.getBettingAmount() - dto.getTableAmount();
                if (beetingAmount > 0) {
                    returnRate2 = dto.getResultAmount() / beetingAmount * 100;

                    if (0 != dto.getUserCount()) {
                        bettingArpu = beetingAmount / dto.getUserCount();
                    }

                    if (0 != dto.getBettingCount()) {
                        bettingAsp = beetingAmount / dto.getBettingCount();
                    }
                }
                dto.setReturnRate1(String.valueOf(BigDecimalUtil.round(returnRate1, 2)) + "%");
                dto.setReturnRate2(String.valueOf(BigDecimalUtil.round(returnRate2, 2)) + "%");
                dto.setBettingAsp(BigDecimalUtil.round(bettingAsp, 2));
                dto.setBettingArpu(BigDecimalUtil.round(bettingArpu, 2));

                list.add(dto);
            }
        }


        return list;
    }

    /**
     * 三张场次分析
     *
     * @return
     */
    @RequestMapping("/getAnalysisList")
    public Object getAnalysisList() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        List<TcardDto> list = Lists.newArrayList();
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
        }
        Map<String, Object> params = new HashMap<>();
        if (null != channelId) {
            params.put("channelId", channelId);
        }
        if (null != parentId && null == channelId) {
            params.put("parentId", parentId);
        }
        params.put("bettingType", 1);
        Date standardDate = DateUtils.parseDate(DATE);
        for (String searchDate : DateUtils.getDateList(data)) {
            params.put("searchDate", searchDate);
            TcardDto dto = new TcardDto();
            params.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
            params.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
            Date date = DateUtils.parseDate(searchDate);
            if(date.after(standardDate)){
                params.put("roomType", 1);
                params.put("amount",null);
            }else{
                params.put("roomType",null);
                params.put("amount",20);
            }
            int lowBettingUser = tcardUserBettingLogService.getUserCountByBettingType(params);
            double lowTableFee = tcardUserBettingLogService.getTableAmount(params);
            int lowTables = tcardUserBettingLogService.getTablesByBettingType(params);
            double lowAvgRounds = toDouble(lowTableFee, lowBettingUser,20);
            if(date.after(standardDate)){
                params.put("roomType", 2);
                params.put("amount",null);
            }else{
                params.put("roomType",null);
                params.put("amount",300);
            }
            int midBettingUser = tcardUserBettingLogService.getUserCountByBettingType(params);
            double midTableFee = tcardUserBettingLogService.getTableAmount(params);
            int midTables = tcardUserBettingLogService.getTablesByBettingType(params);
            double midAvgRounds = toDouble(midTableFee, midBettingUser,300);
            if(date.after(standardDate)){
                params.put("roomType", 3);
                params.put("amount",null);
            }else{
                params.put("roomType",null);
                params.put("amount",3000);
            }
            int highBettingUser = tcardUserBettingLogService.getUserCountByBettingType(params);
            double highTableFee = tcardUserBettingLogService.getTableAmount(params);
            int highTables = tcardUserBettingLogService.getTablesByBettingType(params);
            double highAvgRounds = toDouble(highTableFee, highBettingUser,3000);
            dto.setLowBettingUser(lowBettingUser);
            dto.setMidBettingUser(midBettingUser);
            dto.setHighBettingUser(highBettingUser);
            dto.setLowTableFee(lowTableFee);
            dto.setMidTableFee(midTableFee);
            dto.setHighTableFee(highTableFee);
            dto.setLowTables(lowTables);
            dto.setMidTables(midTables);
            dto.setHighTables(highTables);
            dto.setLowAvgRounds(lowAvgRounds);
            dto.setMidAvgRounds(midAvgRounds);
            dto.setHighAvgRounds(highAvgRounds);
            dto.setSearchDate(searchDate);
            list.add(dto);
        }
        return list;
    }

    private static double toDouble(double one, int two,int three) {
        if (0 == two || 0 == three) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(one));
        BigDecimal b2 = new BigDecimal(two*three);
        return b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
