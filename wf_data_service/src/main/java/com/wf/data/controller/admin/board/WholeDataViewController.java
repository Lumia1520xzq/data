package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.controller.response.DataViewExcelResponse;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelCost;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelRetention;
import com.wf.data.dao.datarepo.entity.DatawareFinalRegisteredRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelCostService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
import com.wf.data.service.data.DatawareFinalRegisteredRetentionService;
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
    @Autowired
    private DatawareFinalRegisteredRetentionService datawareFinalRegisteredRetentionService;

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
        params.put("endDate", endTime);
        List<DatawareFinalChannelInfoAll> allList = datawareFinalChannelInfoAllService.getListByChannelAndDate(params);
        for (DatawareFinalChannelInfoAll info : allList) {
            String date = info.getBusinessDate();
            params.put("date", date);
            DatawareFinalChannelRetention retention = datawareFinalChannelRetentionService.findByDate(params);
            if (null != retention) {
                Double usersDayRetention = retention.getUsersDayRetention();
                Double dayRetention = retention.getDayRetention();
                info.setUsersDayRetention(usersDayRetention);
                info.setDayRetention(dayRetention);
            } else {
                info.setUsersDayRetention(0.0);
                info.setDayRetention(0.0);
            }
            //新用户占比
            info.setUsersRate(calRate(info.getNewUsers(), info.getDau()));
            DatawareFinalChannelCost cost = datawareFinalChannelCostService.findByDate(params);
            if (null != cost) {
                Double totalCost = cost.getTotalCost();
                Double costRate = cost.getCostRate();
                info.setTotalCost(totalCost);
                info.setCostRate(BigDecimalUtil.round(BigDecimalUtil.mul(costRate, 100), 2));
            } else {
                info.setTotalCost(0.0);
                info.setCostRate(0.0);
            }
            //新用户七留
            DatawareFinalRegisteredRetention registeredRetention = datawareFinalRegisteredRetentionService.findByDate(params);
            if (null != registeredRetention) {
                double sevenRetention = registeredRetention.getRetention7();
                info.setSevenRetention(BigDecimalUtil.round(BigDecimalUtil.mul(sevenRetention, 100), 2));
            } else {
                info.setSevenRetention(0.0);
            }
            //流水差
            info.setMoneyGap(info.getBettingAmount() - info.getResultAmount());
            System.out.println("首日付费率:" + info.getFirstRechargeRate());
        }
        if (CollectionUtils.isNotEmpty(allList)) {
            //获取最后一条记录
            DatawareFinalChannelInfoAll lastInfoAll = allList.get(allList.size() - 1);
            //获取最后一天的日期
            String endDate = lastInfoAll.getBusinessDate();
            //前一天的日期
            String beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 1));
            //前一天的数据
            params.put("date", beforeDate);
            DatawareFinalChannelInfoAll lastButOneInfoAll = datawareFinalChannelInfoAllService.findByDate(params);
            DatawareFinalChannelCost lastButOneCost = datawareFinalChannelCostService.findByDate(params);
            //1、日环比
            if (null != lastButOneInfoAll) {
                String dayDauRate = cal(lastInfoAll.getDau(), lastButOneInfoAll.getDau());
                String dayRechargeAmountRate = cal(lastInfoAll.getRechargeAmount(), lastButOneInfoAll.getRechargeAmount());
                String dayRechargeCountRate = cal(lastInfoAll.getRechargeCount(), lastButOneInfoAll.getRechargeCount());
                String dayNewUsersRate = cal(lastInfoAll.getNewUsers(), lastButOneInfoAll.getNewUsers());
                String dayUserCountRate = cal(lastInfoAll.getUserCount(), lastButOneInfoAll.getUserCount());
                String dayBettingRate = cal(lastInfoAll.getBettingRate(), lastButOneInfoAll.getBettingRate());
                String dayDauPayRate = cal(lastInfoAll.getDauPayRate(), lastButOneInfoAll.getDauPayRate());
                String dayBettingPayRate = cal(lastInfoAll.getBettingPayRate(), lastButOneInfoAll.getBettingPayRate());
                String dayUserBettingRate = cal(lastInfoAll.getUserBettingRate(), lastButOneInfoAll.getUserBettingRate());
                String dayBettingAmountRate = cal(lastInfoAll.getBettingAmount(), lastButOneInfoAll.getBettingAmount());
                String dayResultRate = cal(lastInfoAll.getResultRate(), lastButOneInfoAll.getResultRate());
                String dayPayArpuRate = cal(lastInfoAll.getPayArpu(), lastButOneInfoAll.getPayArpu());
                String dayPayArppuRate = cal(lastInfoAll.getPayArppu(), lastButOneInfoAll.getPayArppu());
                String dayUsersRate = cal(calRate(lastInfoAll.getNewUsers(), lastInfoAll.getDau()), calRate(lastButOneInfoAll.getNewUsers(), lastButOneInfoAll.getDau()));
                String dayMoneyGapRate = cal(lastInfoAll.getMoneyGap(), lastButOneInfoAll.getBettingAmount() - lastButOneInfoAll.getResultAmount());
                String dayFirstRechargeRate = cal(lastInfoAll.getFirstRechargeRate(), lastButOneInfoAll.getFirstRechargeRate());
                //比较最后一天和昨天
                Date yesterday = DateUtils.parseDate(DateUtils.getYesterdayDate());
                if (DateUtils.parseDate(endDate).before(yesterday)) {
                    DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);
                    if (null != lastButOneRetention) {
                        String dayUsersDayRetentionRate = cal(lastInfoAll.getUsersDayRetention(), lastButOneRetention.getUsersDayRetention());
                        String dayDayRetentionRate = cal(lastInfoAll.getDayRetention(), lastButOneRetention.getDayRetention());
                        lastInfoAll.setDayUsersDayRetentionRate(dayUsersDayRetentionRate);
                        lastInfoAll.setDayDayRetentionRate(dayDayRetentionRate);
                    } else {
                        lastInfoAll.setDayUsersDayRetentionRate("0%");
                        lastInfoAll.setDayDayRetentionRate("0%");
                    }
                } else {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 1));
                    params.put("date", beforeDate);
                    DatawareFinalChannelRetention lastRetention = datawareFinalChannelRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastInfoAllOne = datawareFinalChannelInfoAllService.findByDate(params);
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 2));
                    params.put("date", beforeDate);
                    DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastButOneInfoAllOne = datawareFinalChannelInfoAllService.findByDate(params);
                    if (null != lastButOneRetention) {
                        String dayUsersDayRetentionRate = cal(lastRetention.getUsersDayRetention(), lastButOneRetention.getUsersDayRetention());
                        String dayDayRetentionRate = cal(lastRetention.getDayRetention(), lastButOneRetention.getDayRetention());
                        lastInfoAll.setDayUsersDayRetentionRate(dayUsersDayRetentionRate);
                        lastInfoAll.setDayDayRetentionRate(dayDayRetentionRate);
                    } else {
                        lastInfoAll.setDayUsersDayRetentionRate("0%");
                        lastInfoAll.setDayDayRetentionRate("0%");
                    }
                }
                //比较最后一天之前七天
                Date weekDay = DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.formatDate(new Date())), 6);
                if (DateUtils.parseDate(endDate).before(weekDay)) {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 1));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention lastButOneSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    if (null != lastButOneSevenRetention) {
                        String daySevenRetentionRate = cal(lastInfoAll.getSevenRetention(), lastButOneSevenRetention.getRetention7() * 100);
                        lastInfoAll.setDaySevenRetentionRate(daySevenRetentionRate);
                    } else {
                        lastInfoAll.setDaySevenRetentionRate("0%");
                    }
                    String dayWeekRechargeRate = cal(lastInfoAll.getWeekRechargeRate(), lastButOneInfoAll.getWeekRechargeRate());
                    lastInfoAll.setDayWeekRechargeRate(dayWeekRechargeRate);
                } else {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 6));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention lastSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastInfoAllTwo = datawareFinalChannelInfoAllService.findByDate(params);
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 7));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention lastButOneSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastButOneInfoAllTwo = datawareFinalChannelInfoAllService.findByDate(params);
                    if (null != lastButOneSevenRetention) {
                        String daySevenRetentionRate = cal(lastSevenRetention.getRetention7(), lastButOneSevenRetention.getRetention7());
                        lastInfoAll.setDaySevenRetentionRate(daySevenRetentionRate);
                    } else {
                        lastInfoAll.setDaySevenRetentionRate("0%");
                    }
                    if (null != lastButOneInfoAllTwo) {
                        String dayWeekRechargeRate = cal(lastInfoAllTwo.getWeekRechargeRate(), lastButOneInfoAllTwo.getWeekRechargeRate());
                        lastInfoAll.setDayWeekRechargeRate(dayWeekRechargeRate);
                    } else {
                        lastInfoAll.setDayWeekRechargeRate("0%");
                    }
                }
                if (null != lastButOneCost) {
                    String dayTotalCost = cal(lastInfoAll.getTotalCost(), lastButOneCost.getTotalCost());
                    String dayCostRate = cal(lastInfoAll.getCostRate() / 100, lastButOneCost.getCostRate());
                    lastInfoAll.setDayTotalCost(dayTotalCost);
                    lastInfoAll.setDayCostRate(dayCostRate);
                } else {
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
                lastInfoAll.setDayUsersRate(dayUsersRate);
                lastInfoAll.setDayMoneyGapRate(dayMoneyGapRate);
                lastInfoAll.setDayFirstRechargeRate(dayFirstRechargeRate);
            }
            //一周前的日期
            String weekBeforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 7));
            //一周前的数据
            params.put("date", weekBeforeDate);
            DatawareFinalChannelInfoAll weekInfoAll = datawareFinalChannelInfoAllService.findByDate(params);
            DatawareFinalChannelCost weekCost = datawareFinalChannelCostService.findByDate(params);
            //1、周同比
            if (null != weekInfoAll) {
                String weekDauRate = cal(lastInfoAll.getDau(), weekInfoAll.getDau());
                String weekRechargeAmountRate = cal(lastInfoAll.getRechargeAmount(), weekInfoAll.getRechargeAmount());
                String weekRechargeCountRate = cal(lastInfoAll.getRechargeCount(), weekInfoAll.getRechargeCount());
                String weekNewUsersRate = cal(lastInfoAll.getNewUsers(), weekInfoAll.getNewUsers());
                String weekUserCountRate = cal(lastInfoAll.getUserCount(), weekInfoAll.getUserCount());
                String weekBettingRate = cal(lastInfoAll.getBettingRate(), weekInfoAll.getBettingRate());
                String weekDauPayRate = cal(lastInfoAll.getDauPayRate(), weekInfoAll.getDauPayRate());
                String weekBettingPayRate = cal(lastInfoAll.getBettingPayRate(), weekInfoAll.getBettingPayRate());
                String weekUserBettingRate = cal(lastInfoAll.getUserBettingRate(), weekInfoAll.getUserBettingRate());
                String weekBettingAmountRate = cal(lastInfoAll.getBettingAmount(), weekInfoAll.getBettingAmount());
                String weekResultRate = cal(lastInfoAll.getResultRate(), weekInfoAll.getResultRate());
                String weekPayArpuRate = cal(lastInfoAll.getPayArpu(), weekInfoAll.getPayArpu());
                String weekPayArppuRate = cal(lastInfoAll.getPayArppu(), weekInfoAll.getPayArppu());
                String weekUsersRate = cal(calRate(lastInfoAll.getNewUsers(), lastInfoAll.getDau()), calRate(weekInfoAll.getNewUsers(), weekInfoAll.getDau()));
                String weekMoneyGapRate = cal(lastInfoAll.getMoneyGap(), weekInfoAll.getBettingAmount() - weekInfoAll.getResultAmount());
                String weekFirstRechargeRate = cal(lastInfoAll.getFirstRechargeRate(), weekInfoAll.getFirstRechargeRate());
                //比较最后一天和昨天
                Date yesterday = DateUtils.parseDate(DateUtils.getYesterdayDate());
                if (DateUtils.parseDate(endDate).before(yesterday)) {
                    DatawareFinalChannelRetention weekRetention = datawareFinalChannelRetentionService.findByDate(params);
                    if (null != weekRetention) {
                        String weekUsersDayRetentionRate = cal(lastInfoAll.getUsersDayRetention(), weekRetention.getUsersDayRetention());
                        String weekDayRetentionRate = cal(lastInfoAll.getDayRetention(), weekRetention.getDayRetention());
                        lastInfoAll.setWeekUsersDayRetentionRate(weekUsersDayRetentionRate);
                        lastInfoAll.setWeekDayRetentionRate(weekDayRetentionRate);
                    } else {
                        lastInfoAll.setWeekUsersDayRetentionRate("0%");
                        lastInfoAll.setWeekDayRetentionRate("0%");
                    }
                } else {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 1));
                    params.put("date", beforeDate);
                    DatawareFinalChannelRetention lastRetention = datawareFinalChannelRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastInfoAllThree = datawareFinalChannelInfoAllService.findByDate(params);
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 8));
                    params.put("date", beforeDate);
                    DatawareFinalChannelRetention lastButOneRetention = datawareFinalChannelRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastButOneInfoAllThree = datawareFinalChannelInfoAllService.findByDate(params);
                    if (null != lastButOneRetention) {
                        String weekUsersDayRetentionRate = cal(lastRetention.getUsersDayRetention(), lastButOneRetention.getUsersDayRetention());
                        String weekDayRetentionRate = cal(lastRetention.getDayRetention(), lastButOneRetention.getDayRetention());
                        lastInfoAll.setWeekUsersDayRetentionRate(weekUsersDayRetentionRate);
                        lastInfoAll.setWeekDayRetentionRate(weekDayRetentionRate);
                    } else {
                        lastInfoAll.setWeekUsersDayRetentionRate("0%");
                        lastInfoAll.setWeekDayRetentionRate("0%");
                    }
                }
                //比较最后一天和前七天
                Date weekDay = DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.formatDate(new Date())), 6);
                if (DateUtils.parseDate(endDate).before(weekDay)) {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 7));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention weekSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    if (null != weekSevenRetention) {
                        String weekSevenRetentionRate = cal(lastInfoAll.getSevenRetention(), weekSevenRetention.getRetention7() * 100);
                        lastInfoAll.setWeekSevenRetentionRate(weekSevenRetentionRate);
                    } else {
                        lastInfoAll.setWeekSevenRetentionRate("0%");
                    }
                    String weekWeekRechargeRate = cal(lastInfoAll.getWeekRechargeRate(), weekInfoAll.getWeekRechargeRate());
                    lastInfoAll.setWeekWeekRechargeRate(weekWeekRechargeRate);
                } else {
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 6));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention lastSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastInfoAllFour = datawareFinalChannelInfoAllService.findByDate(params);
                    beforeDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(endDate), 13));
                    params.put("date", beforeDate);
                    DatawareFinalRegisteredRetention lastButOneSevenRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                    DatawareFinalChannelInfoAll lastButOneInfoAllFour = datawareFinalChannelInfoAllService.findByDate(params);
                    if (null != lastButOneSevenRetention) {
                        String weekSevenRetentionRate = cal(lastSevenRetention.getRetention7(), lastButOneSevenRetention.getRetention7());
                        lastInfoAll.setWeekSevenRetentionRate(weekSevenRetentionRate);
                    } else {
                        lastInfoAll.setWeekSevenRetentionRate("0%");
                    }
                    if (null != lastButOneInfoAllFour) {
                        String weekWeekRechargeRate = cal(lastInfoAllFour.getWeekRechargeRate(), lastButOneInfoAllFour.getWeekRechargeRate());
                        lastInfoAll.setWeekWeekRechargeRate(weekWeekRechargeRate);
                    } else {
                        lastInfoAll.setWeekWeekRechargeRate("0%");
                    }
                }
                if (null != weekCost) {
                    String weekTotalCost = cal(lastInfoAll.getTotalCost(), weekCost.getTotalCost());
                    String weekCostRate = cal(lastInfoAll.getCostRate() / 100, weekCost.getCostRate());
                    lastInfoAll.setWeekTotalCost(weekTotalCost);
                    lastInfoAll.setWeekCostRate(weekCostRate);
                } else {
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
                lastInfoAll.setWeekUsersRate(weekUsersRate);
                lastInfoAll.setWeekMoneyGapRate(weekMoneyGapRate);
                lastInfoAll.setWeekFirstRechargeRate(weekFirstRechargeRate);

            }
        }
        return allList;
    }

    private String cal(Long last, Long notlast) {
        if (0 == notlast) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last - notlast, notlast), "#.##%");
    }

    private String cal(Double last, Double notlast) {
        if (0 == notlast) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(last - notlast, notlast), "#.##%");
    }

    private static double calRate(Long one, Long two) {
        if (two == null || two == 0L) {
            return 0.0;
        }
        if (one == null || one == 0L) {
            return 0.0;
        }
        return BigDecimalUtil.round(BigDecimalUtil.div(one * 100, two), 2);
    }

    /**
     * 数据下载
     */
    @RequestMapping("/export")
    public void export(@RequestParam String parentId, @RequestParam String startTime, @RequestParam String endTime, HttpServletResponse response) {
        List<DataViewExcelResponse> responses = new ArrayList<>();
        try {
            /* 整理参数*/
            Long parentIdParam = null;
            String beginDateParam = null;
            String endDateParam = null;
            try {
                if (!judgeParamIsBank(parentId)) {
                    parentIdParam = Long.parseLong(parentId);
                }
                if (judgeParamIsBank(startTime) && judgeParamIsBank(endTime)) {//开始/结束日期为空
                    beginDateParam = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -14));
                    endDateParam = DateUtils.getYesterdayDate();
                } else if (judgeParamIsBank(startTime) && !judgeParamIsBank(endTime)) {
                    beginDateParam = formatGTMDate(endTime);
                } else if (!judgeParamIsBank(startTime) && judgeParamIsBank(endTime)) {
                    endDateParam = formatGTMDate(startTime);
                }else {
                    beginDateParam = formatGTMDate(startTime);
                    endDateParam = formatGTMDate(endTime);
                }
            } catch (Exception e) {
                logger.error("导出核心指标总览数据时查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId());
            }

            /*从dataware_final_channel_info_all获取数据*/
            Map<String, Object> params = new HashMap<>(3);
            if (null == parentIdParam) {
                parentIdParam = 1L;
            }

            ChannelInfo channelInfo = channelInfoService.get(parentIdParam);
            if (null != channelInfo) {
                if (null != channelInfo.getParentId()) {
                    params.put("channelId", parentIdParam);
                } else {
                    params.put("parentId", parentIdParam);
                }
            } else {
                params.put("parentId", parentIdParam);
            }
            params.put("beginDate", beginDateParam);
            params.put("endDate", endDateParam);

            List<DatawareFinalChannelInfoAll> allList = datawareFinalChannelInfoAllService.getListByChannelAndDate(params);
            for (DatawareFinalChannelInfoAll info : allList) {

                DataViewExcelResponse excelResponse = new DataViewExcelResponse();
                excelResponse.setBusinessDate(info.getBusinessDate());
                excelResponse.setChannelId(info.getChannelId());
                excelResponse.setDau(info.getDau());
                excelResponse.setRechargeAmount(info.getRechargeAmount());
                excelResponse.setBettingRate(info.getBettingRate());
                excelResponse.setDauPayRate(info.getDauPayRate());
                excelResponse.setBettingPayRate(info.getBettingPayRate());
                excelResponse.setNewUsers(info.getNewUsers());
                excelResponse.setUsersRate(info.getUsersRate());
                excelResponse.setUserBettingRate(info.getUserBettingRate());
                excelResponse.setFirstRechargeRate(info.getFirstRechargeRate());
                excelResponse.setRechargeCount(info.getRechargeCount());
                excelResponse.setPayArpu(info.getPayArpu());
                excelResponse.setPayArppu(info.getPayArppu());
                excelResponse.setUserCount(info.getUserCount());
                excelResponse.setBettingAmount(info.getBettingAmount());
                excelResponse.setResultRate(info.getResultRate());
                excelResponse.setWeekRechargeRate(info.getWeekRechargeRate());

                String date = info.getBusinessDate();
                params.put("date", date);
                DatawareFinalChannelRetention retention = datawareFinalChannelRetentionService.findByDate(params);
                if (null != retention) {
                    Double usersDayRetention = retention.getUsersDayRetention();
                    Double dayRetention = retention.getDayRetention();
                    excelResponse.setUsersDayRetention(usersDayRetention);
                    excelResponse.setDayRetention(dayRetention);
                } else {
                    excelResponse.setUsersDayRetention(0.0);
                    excelResponse.setDayRetention(0.0);
                }
                //新用户占比
                excelResponse.setUsersRate(calRate(info.getNewUsers(), info.getDau()));
                DatawareFinalChannelCost cost = datawareFinalChannelCostService.findByDate(params);
                if (null != cost) {
                    Double totalCost = cost.getTotalCost();
                    Double costRate = cost.getCostRate();
                    excelResponse.setTotalCost(totalCost);
                    excelResponse.setCostRate(BigDecimalUtil.round(BigDecimalUtil.mul(costRate, 100), 2));
                } else {
                    excelResponse.setTotalCost(0.0);
                    excelResponse.setCostRate(0.0);
                }
                //新用户七留
                DatawareFinalRegisteredRetention registeredRetention = datawareFinalRegisteredRetentionService.findByDate(params);
                if (null != registeredRetention) {
                    double sevenRetention = registeredRetention.getRetention7();
                    excelResponse.setSevenRetention(BigDecimalUtil.round(BigDecimalUtil.mul(sevenRetention, 100), 2));
                } else {
                    excelResponse.setSevenRetention(0.0);
                }
                //流水差
                excelResponse.setMoneyGap(info.getBettingAmount() - info.getResultAmount());
                responses.add(excelResponse);
            }

            String fileName = "核心指标总览" + com.wf.core.utils.type.DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("核心指标总览", DataViewExcelResponse.class).setDataList(responses).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean judgeParamIsBank(String param) {
        if (param.equals("undefined") || param.equals("null") || StringUtils.isBlank(param)) {
            return true;
        }
        return false;
    }

    /**
     * 格式化GMT时间
     *
     * @param date
     * @return
     */
    public String formatGTMDate(String date) {
        DateFormat gmt = new SimpleDateFormat("yyyy-MM-dd");
        date = date.replace("GMT 0800", "GMT +08:00").replace("GMT 0800", "GMT+0800").replaceAll("\\(.*\\)", "");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US);
        Date time = null;
        try {
            time = defaultFormat.parse(date);
            gmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gmt.format(time);
    }

}
