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
import com.wf.data.common.constants.GameTypeContents;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogHour;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBettingLogHourService;
import com.wf.data.service.data.DatawareBuryingPointHourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台投注人数分析汇总
 * 定时发送邮件
 * 处理获取数据失败问题，重试5次
 * @author  jianjian on 2018/01/15
 */
public class BettingAnalyseJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final DatawareBettingLogHourService hourBettingService = SpringContextHolder.getBean(DatawareBettingLogHourService.class);
    private final DatawareBuryingPointHourService hourBuryingPointService = SpringContextHolder.getBean(DatawareBuryingPointHourService.class);


    private final String EMAIL_STYLE = "<style>table{margin-top:10px;width:700px;" +
            "border-collapse:collapse;border-spacing:0;" +
            "border-top:1px solid #D6D6D6;border-left:1px solid #D6D6D6}" +
            "th,td{border-right:1px solid #D6D6D6;border-bottom:1px solid #D6D6D6}" +
            ".game-name{padding-left:10px;background:#D6D6D6}" +
            "</style>";
    private final String CONTENT_TEMP = "<table><tr><th colspan='3' align='left' class='game-name'>GAME_NAME</th></tr>" +
            "<tr><th rowspan='6'>TODAY日</td><td width='150'>DAU</td><td width='350'>DAILY_ACTIVE</th></tr>" +
            "<tr><td>总投注金叶子</td><td>BETTING_AMOUNT</td></tr>" +
            "<tr><td>总派奖金叶子</td><td>AWARD_AMOUNT</td></tr>" +
            "<tr><td>投注人数</td><td>BETTING_USER</td></tr>" +
            "<tr><td>投注笔数</td><td>BETTING_NUM</td></tr>" +
            "<tr><td>返奖率</td><td>AWARD_RATE</td></tr>" +
            "<tr><th rowspan='5'>过去一小时<br/>TIME_SECTION</th><td>总投注金叶子</td><td>HOUR_BETTING_AMOUNT</td></tr>" +
            "<tr><td>总派奖金叶子</td><td>HOUR_AWARD_AMOUNT</td></tr>" +
            "<tr><td>投注人数</td><td>HOUR_BETTING_USER</td></tr>" +
            "<tr><td>投注笔数</td><td>HOUR_BETTING_NUM</td></tr>" +
            "<tr><td>返奖率</td><td>HOUR_AWARD_RATE</td></tr>" +
            "</table>";
    private static final int TIMES = 5;
    private static final String COMMA = ",";

    public void execute() {
        logger.info("开始每小时投注邮件分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        //处理0点时的统计数据，当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -1);
        while (count <= TIMES) {
            try {
                String receivers = dataConfigService.findByName(DataConstants.GAME_BETTING_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(EMAIL_STYLE);
                    //汇总数据
                    content.append(buildBettingInfo(cal,null));
                    //各游戏数据
                    String gameStr = dataConfigService.findByName(DataConstants.BETTING_ANALYSIS_GAMES).getValue();
                    if(StringUtils.isNotEmpty(gameStr)) {
                        String[] games = gameStr.split(COMMA);
                        for(String game:games){
                            content.append(buildBettingInfo(cal,Integer.parseInt(game)));
                        }
                    }
                    content.insert(0, "截止" + DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN + " HH:00") + "<br/><br/>");
                    for (String to : receivers.split(COMMA)) {
                        try {
                            emailHander.sendHtml(to, String.format("截至%s %s投注情况汇总",
                                    DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN),
                                    DateUtils.formatDate(currDate, DateUtils.MINUTE_PATTERN)),
                                    content.toString());
                        } catch (MessagingException e) {
                            logger.error("每小时投注邮件发送失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        }
                    }
                } else {
                    logger.error("每小时投注邮件未设置收件人，traceId={}", TraceIdUtils.getTraceId());
                }
                logger.info("每小时投注邮件发送成功:traceId={}", TraceIdUtils.getTraceId());
                break;
            } catch (Exception ex) {
                count++;
                if (count <= 5) {
                    logger.error("每小时投注邮件发送失败，重新发送{}，ex={}，traceId={}",count,LogExceptionStackTrace.erroStackTrace(ex), TraceIdUtils.getTraceId());
                } else {
                    logger.error("每小时投注邮件发送失败，停止发送，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(ex), TraceIdUtils.getTraceId());
                }
            }
        }
    }

    private String buildBettingInfo(Calendar cal,Integer gameType) {
        String temp = getTemp(cal,gameType);
        String gameName;
        if (null == gameType){
            gameName = "各游戏汇总";
        }else {
            switch (gameType) {
                case GameTypeContents.GAME_TYPE_DART:
                    gameName = "梦想飞镖";
                    break;
                case GameTypeContents.GAME_TYPE_BILLIARDS:
                    gameName = "梦想桌球";
                    break;
                case GameTypeContents.GAME_TYPE_NIUNIU:
                    gameName = "牛牛";
                    break;
                case GameTypeContents.GAME_TYPE_WARS:
                    gameName = "热血军团";
                    break;
                case GameTypeContents.GAME_TYPE_ARROWS:
                    gameName = "貂蝉保卫战";
                    break;
                case GameTypeContents.GAME_TYPE_BIKE:
                    gameName = "热血摩托";
                    break;
                case GameTypeContents.GAME_TYPE_KINGDOM:
                    gameName = "热血三国";
                    break;
                case GameTypeContents.GAME_TYPE_FISH:
                    gameName = "捕鱼大冒险";
                    break;
                case GameTypeContents.GAME_TYPE_TCARD:
                    gameName = "乐赢三张";
                    break;
                case GameTypeContents.GAME_TYPE_CANDY:
                    gameName = "糖果夺宝";
                    break;
                case GameTypeContents.GAME_TYPE_QUOITS:
                    gameName = "欢乐套圈";
                    break;
                default:
                    gameName = "";
                    break;
            }
        }
        return temp.replace("GAME_NAME", gameName);
    }


    private Map<String, Object> bettingParams(Calendar cal,Integer gameType,boolean calHour) {
        Map<String, Object> params = new HashMap<>(3);
        // 1、bettingDate
        String bettingDate = DateUtils.formatDate(cal.getTime(),DateUtils.DATE_PATTERN);
        params.put("bettingDate", bettingDate);
        // 2、bettingHour
        if (calHour){
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String bettingHour = hour<10?"0"+hour:String.valueOf(hour);
        params.put("bettingHour",bettingHour);
        }
        // 3、gameType
        params.put("gameType", gameType);
        return params;
    }

    private Map<String, Object> buryingParams(Calendar cal,Integer gameType) {
        Map<String, Object> params = new HashMap<>(2);
        // 1、buryingDate
        String buryingDate = DateUtils.formatDate(cal.getTime(),DateUtils.DATE_PATTERN);
        params.put("buryingDate", buryingDate);
        // 2、gameType
        params.put("gameType", gameType);
        return params;
    }

    private String getTemp(Calendar cal,Integer gameType) {
        int today = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int hourAfter = hour + 1;
        String timeSection = String.format("%s:00——%s:00", (hour < 10 ? "0" + hour : hour),
                (hourAfter < 10 ? "0" + hourAfter : hourAfter));
        //今天所有的投注情况
        DatawareBettingLogHour dayInfo = hourBettingService.getSumByDateAndHour(bettingParams(cal,gameType,false));
        if (dayInfo == null) {
            dayInfo = new DatawareBettingLogHour();
            dayInfo.setBettingAmount(0.0);
            dayInfo.setResultAmount(0.0);
            dayInfo.setBettingUserCount(0);
            dayInfo.setBettingCount(0);
        }
        //过去一小时的投注情况
        DatawareBettingLogHour hourInfo = hourBettingService.getSumByDateAndHour(bettingParams(cal,gameType,true));
        if (hourInfo == null) {
            hourInfo = new DatawareBettingLogHour();
            hourInfo.setBettingAmount(0.0);
            hourInfo.setResultAmount(0.0);
            hourInfo.setBettingUserCount(0);
            hourInfo.setBettingCount(0);
        }
        //DAU
        Integer dailyActive = hourBuryingPointService.getDauByDateAndHour(buryingParams(cal,gameType));
        return CONTENT_TEMP
                .replace("TODAY", (today < 10 ? ("0" + today) : String.valueOf(today)))
                .replace("DAILY_ACTIVE", format(dailyActive))
                .replaceFirst("BETTING_AMOUNT", format((dayInfo.getBettingAmount())))
                .replaceFirst("AWARD_AMOUNT", format((dayInfo.getResultAmount())))
                .replaceFirst("BETTING_USER", format((dayInfo.getBettingUserCount())))
                .replaceFirst("BETTING_NUM", format((dayInfo.getBettingCount())))
                .replaceFirst("AWARD_RATE", dayInfo.getBettingAmount() == null || dayInfo.getBettingAmount() == 0 ? "0%" :
                        NumberUtils.format(BigDecimalUtil.div(dayInfo.getResultAmount(), dayInfo.getBettingAmount(), 4), "#.##%"))
                .replace("TIME_SECTION", timeSection)
                .replace("HOUR_BETTING_AMOUNT", format((hourInfo.getBettingAmount())))
                .replace("HOUR_AWARD_AMOUNT", format((hourInfo.getResultAmount())))
                .replace("HOUR_BETTING_USER", format((hourInfo.getBettingUserCount())))
                .replace("HOUR_BETTING_NUM", format((hourInfo.getBettingCount())))
                .replace("HOUR_AWARD_RATE",hourInfo.getBettingAmount() == null || hourInfo.getBettingAmount() == 0 ? "0%" :
                        NumberUtils.format(BigDecimalUtil.div(hourInfo.getResultAmount(),hourInfo.getBettingAmount(), 4), "#.##%"));
    }

    private String format(Object obj) {
        DecimalFormat df = new DecimalFormat("#,###");
        if(null == obj){
            return "0";
        }
        return  df.format(obj);
    }



}
