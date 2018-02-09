package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelRetention;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import com.wf.data.service.data.DatawareFinalChannelRetentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日各渠道关键数据(最新版)
 * @author jianjian huang
 * 2018年2月1日
 */
public class NewChannelDataJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);
    private final DatawareFinalChannelInfoAllService infoAllService = SpringContextHolder.getBean(DatawareFinalChannelInfoAllService.class);
    private final DatawareFinalChannelRetentionService retentionService = SpringContextHolder.getBean(DatawareFinalChannelRetentionService.class);
    private static final Integer TIMES = 5;
    private static final String COMMA = ",";

    public void execute() {
        logger.info("开始每日各渠道关键数据分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        // 昨天的开始时间
        String date = DateUtils.getYesterdayDate();
        String prvDate = DateUtils.formatDate(DateUtils.getPrevDate(new Date(),2));
        while (count <= TIMES) {
            String contentTemp = "<table border='1' cellspacing=1 cellpadding=5 style='text-align:center;border-collapse:collapse'>"+
             "<tr style='font-weight:bold'><td style='background-color:#D6D6D6;'>searchDate日报</td><td bgcolor='#FF6666' colspan=2>降幅超过30%</td><td bgcolor='#FFE4E1' colspan=2>降幅小于30%</td><td bgcolor='#00991F' colspan=2>涨幅大于30%</td><td bgcolor='ccffcc' colspan=2>涨幅小于30%</td><td></td><td></td><td></td><td></td><td></td></tr>"+
            "<tr style='font-weight:bold;background-color:#206A9C;color:white'><td>渠道</td><td>充值金额</td><td>充值人数</td><td>日活人数</td><td>新增人数</td><td>投注用户数</td><td>投注转化率</td><td>付费渗透率</td><td>新用户投注转化率</td><td>新用户次留</td><td>投注流水</td><td>返奖流水</td><td>返奖率</td><td>流水差</td></tr>";
            String tableEnd = "</table>";
            try {
                // 获取收件人 ------ 收件人要改
                String receivers = dataConfigService.findByName(DataConstants.NEW_CHANNEL_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(contentTemp.replace("searchDate",date));
                    String channelStrs = dataConfigService.findByName(DataConstants.NEW_DATA_DAILY_CHANNELS).getValue();
                    if(StringUtils.isNotEmpty(channelStrs)) {
                        String[] channels = channelStrs.split(COMMA);
                        List<Long> mainChannels = channelInfoService.findMainChannelIds();
                        for (String channelStr:channels) {
                          long channelId = Long.parseLong(channelStr);
                          ChannelInfo channelInfo = channelInfoService.get(channelId);
                          if(mainChannels.contains(channelId)) {
                              content.append(buildData(date,channelId,null).replace("channelName",channelInfo.getName()));
                          } else {
                              content.append(buildData(date,null,channelId).replace("channelName",channelInfo.getName()));
                          }
                        }
                        content.append(buildData(date,1L,null).replace("channelName","合计"));
                        content.append(buildData(prvDate,1L,null).replace("channelName","前日合计"));
                    }
                    content.append(tableEnd);
                    content.insert(0, date +"数据如下" + "<br/><br/>");
                    // 发送邮件
                    for (String to : receivers.split(COMMA)) {
                        try {
                            emailHander.sendHtml(to,String.format("每日各渠道关键数据分析汇总(%s)",DateUtils.getDate()),content.toString());
                        } catch (MessagingException e) {
                            logger.error("每日各渠道关键数据邮件发送失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        }
                    }
                } else {
                    logger.error("每日各渠道关键数据分析接收人未设置，traceId={}", TraceIdUtils.getTraceId());
                }
                logger.info("每日各渠道关键数据邮件发送成功:traceId={}", TraceIdUtils.getTraceId());
                break;
            } catch (Exception e) {
                count++;
                if (count <= 5) {
                    logger.error("每日各渠道关键数据发送失败，重新发送{}，ex={}，traceId={}",count,LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                } else {
                    logger.error("每日各渠道关键数据发送失败，停止发送，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                }
            }
        }
    }

    private String buildData(String date,Long parentId,Long channelId) {
        return getTemplate(date,parentId,channelId);
    }


    /**
     * 各渠道数据模板
     */
    private String getTemplate(String date,Long parentId,Long channelId){
        String template = "<tr><td style='background-color:#D6D6D6;'>channelName</td><td>rechargeSum</td><td>rechargeUser</td><td>activeUser</td><td>newUser</td><td>bettingUser</td><td>bettingRate</td><td>payRate</td><td>newBettingRate</td><td>newRemainRate</td><td>cathecticMoney</td><td>winMoney</td><td>winMoneyRate</td><td>moneyGap</td></tr>";
        StringBuilder sb = new StringBuilder();
        String yesDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),1));
        DatawareFinalChannelInfoAll info = infoAllService.findByDate(toMap(date,parentId,channelId));
        DatawareFinalChannelInfoAll yesInfo = infoAllService.findByDate(toMap(yesDate,parentId,channelId));
        if(info == null){
           info = new DatawareFinalChannelInfoAll();
        }
        if(yesInfo == null){
           yesInfo = new DatawareFinalChannelInfoAll();
        }
        // 1、充值金额
        String rechargeSum = colorMoney(info.getRechargeAmount(),yesInfo.getRechargeAmount());
        // 2、充值用户数
        String rechargeUser = color(info.getRechargeCount(),yesInfo.getRechargeCount());
        // 3、日活人数
        String activeUser = color(info.getDau(),yesInfo.getDau());
        // 4、新增用户
        String newUser = color(info.getNewUsers(),yesInfo.getNewUsers());
        // 5、投注用户数
        String bettingUser = color(info.getUserCount(),yesInfo.getUserCount());
        // 6、投注转化率
        String bettingRate = colorRate(info.getBettingRate(),yesInfo.getBettingRate());
        // 7、付费渗透率(充值用户数/投注用户数)
        String payRate = colorRate(info.getBettingPayRate(),yesInfo.getBettingPayRate());
        // 10、投注流水
        String cathecticMoney = colorMoney(info.getBettingAmount(),yesInfo.getBettingAmount());
        // 11、返奖流水
        String winMoney = colorMoney(info.getResultAmount(),yesInfo.getResultAmount());
        // 12、返奖率
        String winMoneyRate = colorRate(info.getResultRate(),yesInfo.getResultRate());
        // 13、流水差
        String moneyGap = colorMoney((info.getBettingAmount()-info.getResultAmount()),(yesInfo.getBettingAmount()-yesInfo.getResultAmount()));
        // 8、新增投注转化率
        String newBettingRate = colorRate(info.getUserBettingRate(),yesInfo.getUserBettingRate());
        // 9、新增次日留存
        String twoDaysBefore = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),2));
        DatawareFinalChannelRetention retention = retentionService.findByDate(toMap(yesDate,parentId,channelId));
        DatawareFinalChannelRetention yesRetention = retentionService.findByDate(toMap(twoDaysBefore,parentId,channelId));
        if(retention == null){
            retention = new DatawareFinalChannelRetention();
        }
        if(yesRetention == null){
            yesRetention = new DatawareFinalChannelRetention();
        }
        String newRemainRate = colorRate(retention.getUsersDayRetention(),yesRetention.getUsersDayRetention());
        String result = template
        .replace("<td>rechargeSum</td>",rechargeSum).replace("<td>activeUser</td>", activeUser).replace("<td>bettingUser</td>", bettingUser)
        .replace("<td>bettingRate</td>", bettingRate).replace("<td>rechargeUser</td>", rechargeUser).replace("<td>payRate</td>", payRate)
        .replace("<td>cathecticMoney</td>",cathecticMoney).replaceFirst("<td>winMoney</td>", winMoney).replace("<td>winMoneyRate</td>", winMoneyRate)
        .replace("<td>newUser</td>", newUser).replace("<td>newBettingRate</td>",newBettingRate).replace("<td>newRemainRate</td>", newRemainRate)
        .replace("<td>moneyGap</td>", moneyGap);
        sb.append(result);
        return sb.toString();
    }

    private String colorMoney(Double tod,Double yes){
        if(tod == null){
            tod = 0.0;
        }
        if(yes == null){
            yes = 0.0;
        }
        if(yes == 0){
            return "<td bgcolor='#00991F' style='text-align:right'>"+format(tod)+"</td>";
        }
        double value = BigDecimalUtil.div(tod-yes,yes);
        if(value <= -0.3){
        return "<td bgcolor='#FF6666' style='text-align:right'>"+format(tod)+"</td>";
        } else if(value > -0.3 && value < 0){
        return "<td bgcolor='#FFE4E1' style='text-align:right'>"+format(tod)+"</td>";
        }else if(value >=0 && value < 0.3){
            return "<td bgcolor='ccffcc' style='text-align:right'>"+format(tod)+"</td>";
        }else{
            return "<td bgcolor='#00991F' style='text-align:right'>"+format(tod)+"</td>";
        }
    }

    private String colorRate(Double tod,Double yes){
        if(tod == null){
            tod = 0.0;
        }
        if(yes == null){
            yes = 0.0;
        }
        if(yes == 0){
            return "<td bgcolor='#00991F' style='text-align:right'>"+tod+"%</td>";
        }
        double value = BigDecimalUtil.div(tod-yes,yes);
        if(value <= -0.3){
            return "<td bgcolor='#FF6666' style='text-align:right'>"+tod+"%</td>";
        } else if(value > -0.3 && value < 0){
            return "<td bgcolor='#FFE4E1' style='text-align:right'>"+tod+"%</td>";
        }else if(value >=0 && value < 0.3){
            return "<td bgcolor='ccffcc' style='text-align:right'>"+tod+"%</td>";
        }else{
            return "<td bgcolor='#00991F' style='text-align:right'>"+tod+"%</td>";
        }
    }

    private String color(Long tod,Long yes){
        if(tod == null){
            tod = 0L;
        }
        if(yes == null){
            yes = 0L;
        }
        if(yes == 0){
            return "<td bgcolor='#00991F' style='text-align:right'>"+tod+"</td>";
        }
        double value = BigDecimalUtil.div(tod-yes,yes);
        if(value <= -0.3){
            return "<td bgcolor='#FF6666' style='text-align:right'>"+tod+"</td>";
        } else if(value > -0.3 && value < 0){
            return "<td bgcolor='#FFE4E1' style='text-align:right'>"+tod+"</td>";
        }else if(value >=0 && value < 0.3){
            return "<td bgcolor='ccffcc' style='text-align:right'>"+tod+"</td>";
        }else{
            return "<td bgcolor='#00991F' style='text-align:right'>"+tod+"</td>";
        }
    }



    private Map<String,Object> toMap(String date,Long parentId,Long channelId) {
        Map<String,Object> map = new HashMap<>(5);
        map.put("searchDate",date);
        map.put("businessDate",date);
        map.put("date",date);
        map.put("parentId",parentId);
        map.put("channelId",channelId);
        return map;
    }

    private String format(double num) {
        return new DecimalFormat(",###,##0").format(num);
    }


}
