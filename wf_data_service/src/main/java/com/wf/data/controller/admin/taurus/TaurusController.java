package com.wf.data.controller.admin.taurus;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.TaurusUserBettingLogService;
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
 * 乐赢拼十
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/admin/taurus")
public class TaurusController extends ExtJsController {

    @Autowired
    private TaurusUserBettingLogService taurusUserBettingLogService;
    @Autowired
    private DatawareBettingLogDayService datawareBettingLogDayService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;

    /**
     * 数据概览
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
        for (String searchDate : getDateList(data)) {
            params.put("searchDate", searchDate);
            if (null != channelId) {
                params.put("channelId", channelId);
            }
            if (null != parentId && null == channelId) {
                params.put("parentId", parentId);
            }
            params.put("gameType", 14);
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
                if (null == dauCount) {
                    dauCount = 0;
                }
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
                    tableAmount = taurusUserBettingLogService.getTableAmount(params);
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
     * 拼十场次分析
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
        for (String searchDate : getDateList(data)) {
            params.put("searchDate", searchDate);
            TcardDto dto = new TcardDto();
            params.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
            params.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));

            params.put("roomType",1);
            int rookieBettingUser = taurusUserBettingLogService.getUserCountByBettingType(params);
            double rookieTableFee = taurusUserBettingLogService.getTableAmount(params);
            int rookieTables = taurusUserBettingLogService.getTablesByBettingType(params);
            double rookieAvgRounds = toDouble(rookieTableFee, rookieBettingUser,30);

            params.put("roomType",2);
            int lowBettingUser = taurusUserBettingLogService.getUserCountByBettingType(params);
            double lowTableFee = taurusUserBettingLogService.getTableAmount(params);
            int lowTables = taurusUserBettingLogService.getTablesByBettingType(params);
            double lowAvgRounds = toDouble(lowTableFee, lowBettingUser,250);
            params.put("roomType",3);
            int midBettingUser = taurusUserBettingLogService.getUserCountByBettingType(params);
            double midTableFee = taurusUserBettingLogService.getTableAmount(params);
            int midTables = taurusUserBettingLogService.getTablesByBettingType(params);
            double midAvgRounds = toDouble(midTableFee, midBettingUser,800);
            params.put("roomType",4);
            int highBettingUser = taurusUserBettingLogService.getUserCountByBettingType(params);
            double highTableFee = taurusUserBettingLogService.getTableAmount(params);
            int highTables = taurusUserBettingLogService.getTablesByBettingType(params);
            double highAvgRounds = toDouble(highTableFee, highBettingUser,1200);

            dto.setRookieBettingUser(rookieBettingUser);
            dto.setLowBettingUser(lowBettingUser);
            dto.setMidBettingUser(midBettingUser);
            dto.setHighBettingUser(highBettingUser);

            dto.setRookieTableFee(rookieTableFee);
            dto.setLowTableFee(lowTableFee);
            dto.setMidTableFee(midTableFee);
            dto.setHighTableFee(highTableFee);

            dto.setRookieTables(rookieTables);
            dto.setLowTables(lowTables);
            dto.setMidTables(midTables);
            dto.setHighTables(highTables);

            dto.setRookieAvgRounds(rookieAvgRounds);
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

    private List<String> getDateList(JSONObject data) {
        String startTime = null;
        String endTime = null;
        if (data != null) {
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        List<String> datelist = Lists.newArrayList();
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -1));
                endTime = DateUtils.getYesterdayDate();
                datelist = DateUtils.getDateList(startTime, endTime);
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
                datelist.add(startTime);
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                datelist.add(startTime);
            } else {
                datelist = DateUtils.getDateList(startTime, endTime);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        return datelist;
    }

}
