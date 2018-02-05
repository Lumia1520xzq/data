package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.data.entity.DatawareFinalChannelInfoHour;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.platform.entity.PlatSignedUser;
import com.wf.data.dto.UserDataOverviewDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareConvertHourService;
import com.wf.data.service.data.DatawareFinalChannelInfoHourService;
import com.wf.data.service.data.DatawareUserSignDayService;
import com.wf.data.service.platform.PlatSignedUserService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * @author shihui
 * @date 2018/2/1
 */

@RestController
@RequestMapping("/data/admin/channel/userdata")
public class ChannelUserdataController extends ExtJsController {

    @Autowired
    private DatawareFinalChannelInfoHourService channelInfoHourService;

    @Autowired
    private DatawareConvertHourService convertHourService;

    @Autowired
    private DatawareUserSignDayService userSignDayService;



    /**
     * 投注数据
     *
     * @return
     */
    @RequestMapping("/betting/list")
    public Object listData() {

        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Integer userType = null;
        String beginDate = null;
        String endDate = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userType = data.getInteger("userType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }

        List<String> datelist = Lists.newArrayList();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 1);//当前小时
        String currentDay = LocalDate.now().toString();//当前日期
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("channelId", channelId);

        //获取时间集合（设置默认搜索时间为一周）
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
                endDate = DateUtils.getDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                endDate = beginDate;
                datelist.add(endDate);
            } else {
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        //循环时间段，根据时间获取数据
        for (int i = datelist.size() - 1; i >= 0; i--) {
            String dateStr = datelist.get(i);
            params.put("businessDate", dateStr);
            if (dateStr.equals(currentDay)) {
                params.put("businessOur", currentHour);
            } else {
                params.put("businessOur", "23");
            }
            //从清洗表中获取DAU，投注人数，投注笔数，投注金额，流水差
            DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
            UserDataOverviewDto dto = new UserDataOverviewDto();
            dto.setBusinessDate(dateStr);
            if (channelInfoHour != null) {
                //DAU
                Long dau = channelInfoHour.getDau();
                //投注人数
                Long bUserCount = channelInfoHour.getUserCount();
                //投注笔数
                Long bCount = channelInfoHour.getBettingCount();
                //投注金额
                Double bAmount = channelInfoHour.getBettingAmount();
                //流水差
                Double diffAmount = channelInfoHour.getDiffAmount();
                //投注ARPU=投注金额/投注人数
                Double bettingArpu = bUserCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(bAmount, bUserCount), 1);
                //投注ASP=投注金额/投注笔数
                Double bASP = bCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(bAmount, bCount), 1);
                //返奖金额
                Double rateAmount = channelInfoHour.getResultAmount();
                //返奖率
                String returnRate = rateAmount == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(rateAmount, bAmount), "#.##%");

                dto.setDau(dau);
                dto.setBettingUserCount(bUserCount);
                dto.setBettingCount(bCount);
                dto.setBettingAmount(bAmount);
                dto.setDiffAmount(diffAmount);
                dto.setBettingArpu(bettingArpu);
                dto.setBettingAsp(bASP);
                dto.setReturnRate(returnRate);
            }
            overviewDtos.add(dto);
        }

        return overviewDtos;
    }


    /**
     * 充值数据
     *
     * @return
     */
    @RequestMapping("/recharge/list")
    public Object rechargeListData() {

        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Integer userType = null;
        String beginDate = null;
        String endDate = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userType = data.getInteger("userType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }

        List<String> datelist = Lists.newArrayList();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 1);//当前小时
        String currentDay = LocalDate.now().toString();//当前日期
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("channelId", channelId);

        //获取时间集合（设置默认搜索时间为一周）
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
                endDate = DateUtils.getDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                endDate = beginDate;
                datelist.add(endDate);
            } else {
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        //循环时间段，根据时间获取数据
        for (int i = datelist.size() - 1; i >= 0; i--) {
            Long recharCount = 0L;
            UserDataOverviewDto dto = new UserDataOverviewDto();
            String dateStr = datelist.get(i);
            dto.setBusinessDate(dateStr);

            params.put("businessDate", dateStr);
            if (dateStr.equals(currentDay)) {
                params.put("businessOur", currentHour);
            } else {
                params.put("businessOur", "23");
            }

            //从充值清洗表中获取充值人数
            recharCount = convertHourService.findrechargeCountByDate(params);
            dto.setRechargeCount(recharCount);

            //从清洗表中获取DAU,充值人数，充值金额
            DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
            if (channelInfoHour != null) {
                //DAU
                Long dau = channelInfoHour.getDau();
                //充值人数
                Long recharUserCount = channelInfoHour.getRechargeCount();
                //充值金额
                Double rechargeAmount = channelInfoHour.getRechargeAmount();
                //充值ARPU=充值金额/DAU
                Double rechargeArpu = dau == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(rechargeAmount, dau), 1);
                //充值ARPPU=充值金额/充值人数
                Double rechargeArppu = recharUserCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(rechargeAmount, recharUserCount), 1);

                dto.setDau(dau);
                dto.setRecharUserCount(recharUserCount);
                dto.setRechargeAmount(rechargeAmount);
                dto.setRechargeArpu(rechargeArpu);
                dto.setRechargeArppu(rechargeArppu);
            }
            overviewDtos.add(dto);
        }
        return overviewDtos;
    }


    /**
     * 转化率数据
     *
     * @return
     */
    @RequestMapping("/conversion/list")
    public Object conversionListData() {

        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Integer userType = null;
        String beginDate = null;
        String endDate = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userType = data.getInteger("userType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }

        List<String> datelist = Lists.newArrayList();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 1);//当前小时
        String currentDay = LocalDate.now().toString();//当前日期
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("channelId", channelId);

        //获取时间集合（设置默认搜索时间为一周）
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
                endDate = DateUtils.getDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                endDate = beginDate;
                datelist.add(endDate);
            } else {
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        //循环时间段，根据时间获取数据
        for (int i = datelist.size() - 1; i >= 0; i--) {
            Long signedUserNum = 0L;
            UserDataOverviewDto dto = new UserDataOverviewDto();
            String dateStr = datelist.get(i);
            dto.setBusinessDate(dateStr);

            params.put("businessDate", dateStr);
            if (dateStr.equals(currentDay)) {
                params.put("businessOur", currentHour);
            } else {
                params.put("businessOur", "23");
            }

            //从签到表中获取签到人数
            signedUserNum = userSignDayService.getSignedCountByTime(params);
            dto.setSignedUserNum(signedUserNum);

            //从清洗表中获取DAU,充值人数，充值金额
            DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
            if (channelInfoHour != null) {
                //DAU
                Long dau = channelInfoHour.getDau();
                //投注人数
                Long bUserCount = channelInfoHour.getUserCount();
                //充值人数
                Long recharUserCount = channelInfoHour.getRechargeCount();
                //签到转化率=签到人数/DAU
                String signedConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(signedUserNum, dau), "#.##%");
                //投注转化率=投注人数/DAU
                String bettingConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(bUserCount, dau), "#.##%");
                //DAU付费转化率=充值人数/DAU
                String dauPayConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(recharUserCount, dau), "#.##%");
                //投注付费转化率=充值人数/投注人数
                String bettingPayConversionRate = bUserCount == 0L ? "0%" : NumberUtils.format(BigDecimalUtil.div(recharUserCount, bUserCount), "#.##%");

                dto.setDau(dau);
                dto.setBettingUserCount(bUserCount);
                dto.setRecharUserCount(recharUserCount);
                dto.setSignedConversionRate(signedConversionRate);
                dto.setBettingConversionRate(bettingConversionRate);
                dto.setDauPayConversionRate(dauPayConversionRate);
                dto.setBettingPayConversionRate(bettingPayConversionRate);
            }
            overviewDtos.add(dto);
        }
        return overviewDtos;
    }

    /**
     * 新用户数据
     *
     * @return
     */
    @RequestMapping("/newuser/list")
    public Object newuserListData() {

        JSONObject json = getRequestJson();
        Long parentId = null;
        Long channelId = null;
        Integer userType = null;
        String beginDate = null;
        String endDate = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userType = data.getInteger("userType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }

        List<String> datelist = Lists.newArrayList();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 1);//当前小时
        String currentDay = LocalDate.now().toString();//当前日期
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("channelId", channelId);

        //获取时间集合（设置默认搜索时间为一周）
        try {
            if (StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
                beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
                endDate = DateUtils.getDate();
                datelist = DateUtils.getDateList(beginDate, endDate);
            } else if (StringUtils.isBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
                beginDate = endDate;
                datelist.add(beginDate);
            } else if (StringUtils.isNotBlank(beginDate) && StringUtils.isBlank(endDate)) {
                endDate = beginDate;
                datelist.add(endDate);
            } else {
                datelist = DateUtils.getDateList(beginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        //循环时间段，根据时间获取数据
        for (int i = datelist.size() - 1; i >= 0; i--) {
            UserDataOverviewDto dto = new UserDataOverviewDto();
            String dateStr = datelist.get(i);
            dto.setBusinessDate(dateStr);

            params.put("businessDate", dateStr);
            if (dateStr.equals(currentDay)) {
                params.put("businessOur", currentHour);
            } else {
                params.put("businessOur", "23");
            }

            //从清洗表中获取DAU,新增用户数
            DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
            if (channelInfoHour != null) {
                //DAU
                Long dau = channelInfoHour.getDau();
                //新增用户数
                Long newUsersNum = channelInfoHour.getNewUsers();

                dto.setDau(dau);
                dto.setNewUsersNum(newUsersNum);
            }
            overviewDtos.add(dto);
        }
        return overviewDtos;
    }


}
