package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dto.TcardDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareUserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日各渠道关键数据(改版)
 * @author jianjian huang
 * 2018年1月29日
 */
public class ChannelDataJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);
    private final DatawareConvertDayService convertDayService = SpringContextHolder.getBean(DatawareConvertDayService.class);
    private final DatawareBuryingPointDayService buryingPointDayService = SpringContextHolder.getBean(DatawareBuryingPointDayService.class);
    private final DatawareBettingLogDayService  bettingLogDayService = SpringContextHolder.getBean(DatawareBettingLogDayService.class);
    private final DatawareUserInfoService userInfoService = SpringContextHolder.getBean(DatawareUserInfoService.class);
    private static final Integer TIMES = 5;
    private static final String COMMA = ",";

    public void execute() {
        logger.info("开始每日各渠道关键数据分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        // 昨天的开始时间
        String date = DateUtils.getYesterdayDate();
        date = "2018-01-12";
        while (count <= TIMES) {
            String contentTemp = "<table border='1' style='text-align: center ; border-collapse: collapse'>"
                    +"<tr style='font-weight:bold'><td>渠道</td><td>渠道ID</td><td>充值金额</td><td>日活人数</td><td>投注用户数</td><td>充值人数</td><td>新增人数</td><td>投注流水</td><td>投注转化率</td><td>付费渗透率</td><td>新用户投注转化率</td><td>新用户次留</td><td>返奖流水</td><td>返奖率</td></tr>";
            String tableEnd = "</table>";
            try {
                // 获取收件人 ------ 收件人要改
                String receivers = dataConfigService.findByName(DataConstants.CHANNEL_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(contentTemp);
                    String channelStrs = dataConfigService.findByName(DataConstants.DATA_DAILY_CHANNELS).getValue();
                    if(StringUtils.isNotEmpty(channelStrs)) {
                        String[] channels = channelStrs.split(COMMA);
                        List<Long> mainChannels = channelInfoService.findMainChannelIds();
                        for (String channelStr:channels) {
                          long channelId = Long.parseLong(channelStr);
                          ChannelInfo channelInfo = channelInfoService.get(channelId);
                          if(mainChannels.contains(channelId)) {
                              content.append(buildData(date,channelId,null).replace("channelName",channelInfo.getName()).replace("channelId",channelStr));
                          } else {
                              content.append(buildData(date,null,channelId).replace("channelName",channelInfo.getName()).replace("channelId",channelStr));
                          }
                        }
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
    private String getTemplate(String date,Long parentId,Long channelId) {
        String template = "<tr><td>channelName</td><td>channelId</td><td>rechargeSum</td><td>activeUser</td><td>bettingUser</td><td>rechargeUser</td><td>newUser</td><td>cathecticMoney</td><td>bettingRate</td><td>payRate</td><td>newBettingRate</td><td>newRemainRate</td><td>winMoney</td><td>winMoneyRate</td></tr>";
        StringBuilder sb = new StringBuilder();
        // 2、充值金额
        Double rechargeSum = convertDayService.getRechargeSumByDate(toMap(date,parentId,channelId));
        // 3、日活人数
        List<Long> activeUserList = buryingPointDayService.getGameDauIds(toMap(date,parentId,channelId));
        Integer activeUser = activeUserList.size();
        TcardDto bettingInfo = bettingLogDayService.getTcardBettingByday(toMap(date,parentId,channelId));
        // 4、投注用户数
        Integer bettingUser = bettingInfo.getUserCount();
        // 5、投注转化率
        String bettingRate = activeUser == 0?"0%":NumberUtils.format(BigDecimalUtil.div(bettingUser,activeUser,4),"#.##%");
        // 6、充值用户数
        List<Long> rechargeList = convertDayService.getRechargeUserIdsByDate(toMap(date,parentId,channelId));
        Integer rechargeUser = CollectionUtils.isEmpty(rechargeList)?0:rechargeList.size();
        // 7、付费渗透率(充值用户数/投注用户数)
        String payRate = bettingUser == 0 ? "0%":NumberUtils.format(BigDecimalUtil.div(rechargeUser,bettingUser,4),"#.##%");
        // 8、投注流水
        Double cathecticMoney = bettingInfo.getBettingAmount();
        // 9、返奖流水
        Double winMoney = bettingInfo.getResultAmount();
        // 10、返奖率
        String winMoneyRate = cathecticMoney == 0?"0%":NumberUtils.format(BigDecimalUtil.div(winMoney,cathecticMoney,4),"#.##%");
        // 11、新增用户
        List<Long> newUserIds = userInfoService.getNewUserByDate(toMap(date,parentId,channelId));
        Integer	newUser= CollectionUtils.isEmpty(newUserIds)?0:newUserIds.size();
        // 新增用户中的投注人数
        // 投注用户
        List<Long> bettingUserIds = bettingLogDayService.getBettingUserIds(toMap(date,parentId,channelId));
        Integer newBettingUser = CollectionUtils.intersection(newUserIds,bettingUserIds).size();
        // 12、新增投注转化率
        String newBettingRate = newUser == 0?"0%":NumberUtils.format(BigDecimalUtil.div(newBettingUser,newUser,4),"#.##%");
        // 13、新增次日留存
        String yesDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),1));
        // 昨日新增用户
        List<Long> yesNewUserIds = userInfoService.getNewUserByDate(toMap(yesDate,parentId,channelId));
        String newRemainRate = getRemainRate(yesNewUserIds,activeUserList);
        String result = template
        .replace("rechargeSum",rechargeSum.toString()).replace("activeUser", activeUser.toString()).replace("bettingUser", bettingUser.toString())
        .replace("bettingRate", bettingRate).replace("rechargeUser", rechargeUser.toString()).replace("payRate", payRate)
        .replace("cathecticMoney", cathecticMoney.toString()).replaceFirst("winMoney", winMoney.toString()).replace("winMoneyRate", winMoneyRate)
        .replace("newUser", newUser.toString()).replace("newBettingRate",newBettingRate).replace("newRemainRate", newRemainRate);
        sb.append(result);
        return sb.toString();
    }

    private String getRemainRate(List<Long> yesNewUserIds,List<Long> activeUserList) {
        if(CollectionUtils.isNotEmpty(yesNewUserIds) || CollectionUtils.isNotEmpty(activeUserList)){
            return "0%";
        }
        List<Long> temp = (List<Long>)CollectionUtils.intersection(yesNewUserIds,activeUserList);
        return NumberUtils.format(BigDecimalUtil.div(temp.size(),yesNewUserIds.size(),4),"#.##%");
    }

    private Map<String,Object> toMap(String date,Long parentId,Long channelId) {
        Map<String,Object> map = new HashMap<>(5);
        map.put("searchDate",date);
        map.put("businessDate",date);
        map.put("parentId",parentId);
        map.put("channelId",channelId);
        return map;
    }


}
