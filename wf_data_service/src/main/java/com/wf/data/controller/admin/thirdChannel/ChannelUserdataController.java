package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.dao.datarepo.entity.DatawareConvertHour;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoHour;
import com.wf.data.dto.UserDataOverviewDto;
import com.wf.data.service.data.*;
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

    @Autowired
    private DatawareBettingLogHourService bettingLogHourService;

    @Autowired
    private DatawareBuryingPointHourService buryingPointHourService;

    @Autowired
    private DatawareUserInfoService userInfoService;

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
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 2);//当前小时
        if (currentHour.length() == 1) {
            currentHour = "0" + currentHour;
        }
        String currentDay = LocalDate.now().toString();//当前日期

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
            //数据日期
            String dateStr = datelist.get(i);
            //DAU
            Long dau = 0L;
            //投注人数
            Long bUserCount = 0L;
            //投注笔数
            Long bCount = 0L;
            //投注金额
            Double bAmount = 0D;
            //流水差
            Double diffAmount = 0D;
            //返奖金额
            Double rateAmount = 0D;

            //用户类型为新增用户
            if (userType != null && userType == 1) {
                //获取新用户list
                List<Long> userIds = getNewUserIds(dateStr, parentId, channelId);
                if (userIds != null && userIds.size() != 0) {
                    params.put("userIds", userIds);
                    params.put("bettingDate", dateStr);
                    params.put("buryingDate", dateStr);
                    //获取投注清洗表数据
                    DatawareBettingLogHour bettingLogHour = bettingLogHourService.getNewUserBettingInfoByDate(params);
                    if (bettingLogHour != null) {
                        Integer newUserDau = buryingPointHourService.getDauByDateAndHour(params);
                        if (newUserDau != null) {
                            dau = newUserDau.longValue();
                        }
                        bUserCount = bettingLogHour.getBettingUserCount() == null ? 0L : bettingLogHour.getBettingUserCount().longValue();
                        bCount = bettingLogHour.getBettingCount() == null ? 0L : bettingLogHour.getBettingCount().longValue();
                        bAmount = bettingLogHour.getBettingAmount() == null ? 0L : bettingLogHour.getBettingAmount();
                        rateAmount = bettingLogHour.getResultAmount() == null ? 0L : bettingLogHour.getResultAmount();
                        diffAmount = bAmount - rateAmount;
                    }
                }
            } else {
                params.put("businessDate", dateStr);
                if (dateStr.equals(currentDay)) {
                    params.put("businessOur", currentHour);
                } else {
                    params.put("businessOur", "23");
                }

                //从DatawareFinalChannelInfoHour表中获取DAU，投注人数，投注笔数，投注金额，流水差
                DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
                if (channelInfoHour != null) {
                    dau = channelInfoHour.getDau();
                    bUserCount = channelInfoHour.getUserCount();
                    bCount = channelInfoHour.getBettingCount();
                    bAmount = channelInfoHour.getBettingAmount();
                    diffAmount = channelInfoHour.getDiffAmount();
                    rateAmount = channelInfoHour.getResultAmount();
                }
            }
            //投注ARPU=投注金额/投注人数
            Double bettingArpu = bUserCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(bAmount, bUserCount), 1);
            //投注ASP=投注金额/投注笔数
            Double bASP = bCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(bAmount, bCount), 1);
            //返奖率
            String returnRate = rateAmount == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(rateAmount, bAmount), "#.##%");

            dto.setBusinessDate(dateStr);
            dto.setDau(dau);
            dto.setBettingUserCount(bUserCount);
            dto.setBettingCount(bCount);
            dto.setBettingAmount(bAmount);
            dto.setDiffAmount(diffAmount);
            dto.setBettingArpu(bettingArpu);
            dto.setBettingAsp(bASP);
            dto.setReturnRate(returnRate);
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
        List<String> datelist = Lists.newArrayList();
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 2);//当前小时
        if (currentHour.length() == 1) {
            currentHour = "0" + currentHour;
        }
        String currentDay = LocalDate.now().toString();//当前日期

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
            params.put("businessDate", dateStr);

            //充值人数
            Long recharCount = 0L;
            //DAU
            Long dau = 0L;
            //充值人数
            Long recharUserCount = 0L;
            //充值金额
            Double rechargeAmount = 0D;

            if (userType != null && userType == 1) {
                //获取新用户list
                List<Long> userIds = getNewUserIds(dateStr, parentId, channelId);
                if (userIds != null && userIds.size() != 0) {
                    params.put("userIds", userIds);
                    params.put("buryingDate", dateStr);
                    params.put("convertDate", dateStr);

                    //获取充值清洗表数据
                    DatawareConvertHour convertHour = convertHourService.getNewUserConverInfo(params);
                    if (convertHour != null) {
                        Integer newUserDau = buryingPointHourService.getDauByDateAndHour(params);
                        if (newUserDau != null) {
                            dau = newUserDau.longValue();
                        }
                        recharUserCount = convertHour.getRechargeUserCount() == null ? 0L : convertHour.getRechargeUserCount();
                        rechargeAmount = convertHour.getThirdAmount() == null ? 0D : convertHour.getThirdAmount();
                        recharCount = convertHour.getRechargeCount() == null ? 0L : convertHour.getRechargeCount().longValue();
                    }
                }
            } else {
                if (dateStr.equals(currentDay)) {
                    params.put("businessOur", currentHour);
                } else {
                    params.put("businessOur", "23");
                }
                //从清洗表中获取DAU,充值人数，充值金额
                DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
                if (channelInfoHour != null) {
                    dau = channelInfoHour.getDau();
                    recharUserCount = channelInfoHour.getRechargeCount() == null ? 0L : channelInfoHour.getRechargeCount();
                    rechargeAmount = channelInfoHour.getRechargeAmount() == null ? 0L : channelInfoHour.getRechargeAmount();
                }
                //从充值清洗表中获取充值人数
                recharCount = convertHourService.findrechargeCountByDate(params);
            }
            //充值ARPU=充值金额/DAU
            Double rechargeArpu = dau == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(rechargeAmount, dau), 1);
            //充值ARPPU=充值金额/充值人数
            Double rechargeArppu = recharUserCount == 0 ? 0 : BigDecimalUtil.round(BigDecimalUtil.div(rechargeAmount, recharUserCount), 1);

            dto.setBusinessDate(dateStr);
            dto.setDau(dau);
            dto.setRecharUserCount(recharUserCount);
            dto.setRechargeAmount(rechargeAmount);
            dto.setRechargeArpu(rechargeArpu);
            dto.setRechargeArppu(rechargeArppu);
            dto.setRechargeCount(recharCount);
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
        List<String> datelist = Lists.newArrayList();
        List<UserDataOverviewDto> overviewDtos = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String currentHour = Integer.toString(LocalTime.now().getHour() - 2);//当前小时
        if (currentHour.length() == 1) {
            currentHour = "0" + currentHour;
        }
        String currentDay = LocalDate.now().toString();//当前日期

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
            params.put("businessDate", dateStr);
            //DAU
            Long dau = 0L;
            //投注人数
            Long bUserCount = 0L;
            //充值人数
            Long recharUserCount = 0L;

            if (userType != null && userType == 1) {//用户类型：新用户
                List<Long> userIds = getNewUserIds(dateStr, parentId, channelId);
                if (userIds != null && userIds.size() != 0) {
                    params.put("userIds", userIds);
                    params.put("bettingDate", dateStr);
                    params.put("buryingDate", dateStr);
                    params.put("convertDate", dateStr);
                    //DAU
                    Integer newUserDau = buryingPointHourService.getDauByDateAndHour(params);
                    if (newUserDau != null) {
                        dau = newUserDau.longValue();
                    }
                    //获取投注清洗表数据
                    DatawareBettingLogHour bettingLogHour = bettingLogHourService.getNewUserBettingInfoByDate(params);
                    if (null != bettingLogHour) {
                        bUserCount = bettingLogHour.getBettingUserCount() == null ? 0L : bettingLogHour.getBettingUserCount().longValue();
                    }
                    //获取充值清洗表数据
                    DatawareConvertHour convertHour = convertHourService.getNewUserConverInfo(params);
                    if (null != convertHour) {
                        recharUserCount = convertHour.getRechargeUserCount();
                    }
                }
            } else {//用户类型：所有用户
                if (dateStr.equals(currentDay)) {
                    params.put("businessOur", currentHour);
                } else {
                    params.put("businessOur", "23");
                }
                //从DatawareFinalChannelInfoHour表中获取DAU,充值人数，充值金额
                DatawareFinalChannelInfoHour channelInfoHour = channelInfoHourService.findDataForPandect(params);
                if (channelInfoHour != null) {
                    dau = channelInfoHour.getDau();
                    bUserCount = channelInfoHour.getUserCount();
                    recharUserCount = channelInfoHour.getRechargeCount();
                }
            }
            //签到人数
            Long signedUserNum = userSignDayService.getSignedCountByTime(params);
            if (signedUserNum == null) {
                signedUserNum = 0L;
            }
            //签到转化率=签到人数/DAU
            String signedConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(signedUserNum, dau), "#.##%");
            //投注转化率=投注人数/DAU
            String bettingConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(bUserCount, dau), "#.##%");
            //DAU付费转化率=充值人数/DAU
            String dauPayConversionRate = dau == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(recharUserCount, dau), "#.##%");
            //投注付费转化率=充值人数/投注人数
            String bettingPayConversionRate = bUserCount == 0L ? "0%" : NumberUtils.format(BigDecimalUtil.div(recharUserCount, bUserCount), "#.##%");

            dto.setBusinessDate(dateStr);
            dto.setDau(dau);
            dto.setSignedUserNum(signedUserNum);
            dto.setBettingUserCount(bUserCount);
            dto.setRecharUserCount(recharUserCount);
            dto.setSignedConversionRate(signedConversionRate);
            dto.setBettingConversionRate(bettingConversionRate);
            dto.setDauPayConversionRate(dauPayConversionRate);
            dto.setBettingPayConversionRate(bettingPayConversionRate);
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
        String currentHour = Integer.toString(LocalTime.now().getHour() - 2);//当前小时
        if (currentHour.length() == 1) {
            currentHour = "0" + currentHour;
        }
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

    //获取新用户list
    private List<Long> getNewUserIds(String dateStr, Long parentId, Long channelId) {
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("businessDate", dateStr);
        userParams.put("parentId", parentId);
        userParams.put("channelId", channelId);
        return userInfoService.getNewUserByDate(userParams);
    }
}
