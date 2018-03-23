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
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;
import com.wf.data.dto.DailyReportDto;
import com.wf.data.service.data.DatawareGameBettingInfoHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shihui
 * @date 2018/2/5
 */

@RestController
@RequestMapping("/data/admin/channel/gamedata")
public class ChannelGameDataController extends ExtJsController {

    @Autowired
    private DatawareGameBettingInfoHourService gameBettingInfoHourService;

    @RequestMapping("list")
    public Object getList() {
        List<DailyReportDto> list = Lists.newArrayList();
        List<String> datelist = Lists.newArrayList();

        JSONObject json = getRequestJson();
        String gameType = null;
        Long parentId = null;
        Long channelId = null;
        String BeginDate = null;
        String endDate = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            gameType = data.getString("gameType");
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            BeginDate = data.getString("BeginDate");
            endDate = data.getString("endDate");
        }

        //默认为所有游戏
        if (gameType == null) {
            gameType = "0";
        }

        //获取日期集合
        try {
            if (StringUtils.isBlank(BeginDate) && StringUtils.isBlank(endDate)) {
                BeginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
                endDate = DateUtils.getDate();
                datelist = DateUtils.getDateList(BeginDate, endDate);
            } else if (StringUtils.isBlank(BeginDate) && StringUtils.isNotBlank(endDate)) {
                BeginDate = endDate;
                datelist.add(BeginDate);
            } else if (StringUtils.isNotBlank(BeginDate) && StringUtils.isBlank(endDate)) {
                endDate = BeginDate;
                datelist.add(endDate);
            } else {
                datelist = DateUtils.getDateList(BeginDate, endDate);
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }

        //设置参数
        Map<String, Object> params = new HashMap<>(5);
        params.put("gameType", gameType);
        params.put("parentId", parentId);
        params.put("channelId", channelId);
        //循环日期集合
        if (datelist != null) {
            for (int i = datelist.size() - 1; i >= 0; i--) {
                DailyReportDto dto = new DailyReportDto();
                //行日期
                String dateStr = datelist.get(i);
                //设置小时参数
                if (dateStr.equals(DateUtils.getDate())) {
                    String currentHour = Integer.toString(LocalTime.now().getHour() - 2);//当前小时
                    if (currentHour.length() == 1) {
                        currentHour = "0" + currentHour;
                    }
                    params.put("businessHour", currentHour);
                } else {
                    params.put("businessHour", 23);
                }
                params.put("businessDate", dateStr);

                //从游戏清洗表获取数据(dataware_game_betting_info_hour)
                DatawareGameBettingInfoHour gameBettingInfoHour = gameBettingInfoHourService.getInfoByDate(params);

                dto.setSearchDate(dateStr);
                dto.setGameType(Integer.parseInt(gameType));
                if (null != gameBettingInfoHour) {
                    //游戏DAU
                    Integer dau = Integer.parseInt(gameBettingInfoHour.getDau().toString());
                    //投注人数
                    Integer bettingUserCount = Integer.parseInt(gameBettingInfoHour.getBettingUserCount().toString());
                    //投注笔数
                    Integer bettingCount = Integer.parseInt(gameBettingInfoHour.getBettingCount().toString());
                    //投注金额
                    Double bettingAmount = gameBettingInfoHour.getBettingAmount();
                    //投注转化率=投注人数/dau
                    String bettingConversionRate = calRate(bettingUserCount, dau);
                    //投注ARPU=投注金额/投注人数
                    Double beetingARPU = calDiv(bettingAmount, bettingUserCount);
                    //投注ASP=投注金额/投注笔数
                    Double beetingASP = calDiv(bettingAmount, bettingCount);
                    //返奖金额
                    Double resultAmount = gameBettingInfoHour.getReturnAmount() == null ? 0 : gameBettingInfoHour.getReturnAmount();
                    //流水差=投注-返奖
                    Double diffAmoutn = bettingAmount - resultAmount;
                    //返奖率=返奖流水/投注流水
                    String returnRate = calRate(resultAmount, bettingAmount);

                    dto.setDau(dau);
                    dto.setBettingUserCount(bettingUserCount);
                    dto.setBettingCount(bettingCount);
                    dto.setBettingAmount(bettingAmount);
                    dto.setBettingRate(bettingConversionRate);
                    dto.setArpu(beetingARPU);
                    dto.setAsp(beetingASP);
                    dto.setAmountGap(diffAmoutn);
                    dto.setReturnRate(returnRate);
                }
                list.add(dto);
            }
        }
        return list;
    }

    private Double calDiv(double divisor, double dividend) {
        if (dividend == 0) {
            return 0D;
        }
        return BigDecimalUtil.round(BigDecimalUtil.div(divisor, dividend), 1);
    }

    private String calRate(double divisor, double dividend) {
        if (dividend == 0) {
            return "0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(divisor, dividend), "#.##%");
    }
}
