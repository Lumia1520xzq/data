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
import com.wf.data.dto.TcardDto;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.elasticsearch.EsUicAllGameService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏数据报表
 * @author JoeH
 * 2018.01.18
 */
public class AllGameDayReportJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final EsUicAllGameService gameService = SpringContextHolder.getBean(EsUicAllGameService.class);
    private final DatawareBuryingPointDayService datawareBuryingPointDayService = SpringContextHolder.getBean(DatawareBuryingPointDayService.class);
    private final DatawareBettingLogDayService datawareBettingLogDayService = SpringContextHolder.getBean(DatawareBettingLogDayService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);

    private final String CONTENT_TEMP_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse' >"
            + "<tr style='font-weight:bold'><td rowspan='30' bgcolor='#DDDDDD' width='120'><span>gameName</span><br/><span>dateTime日</span></td><td colspan='8' bgcolor='#DDDDDD'>基础数据</td></tr>"
            + "<tr style='font-weight:bold'><td>新增用户</td><td>活跃用户</td><td>平台日活人数</td><td>导入率</td><td>累计用户</td><td>&nbsp </td> <td>&nbsp </td><td>&nbsp </td></tr>"
            + "<tr><td>newUser</td><td>activeUser</td><td>dailyActive</td><td>importRate</td><td>sumUser</td><td> &nbsp</td> <td> &nbsp</td><td> &nbsp</td></tr>"
            + "<tr style='font-weight:bold'><td colspan='8' bgcolor='#DDDDDD'>流水数据</td></tr>"
            + "<tr style='font-weight:bold'><td >投注流水</td><td>返奖流水</td><td>应用流水差 </td><td>返奖率</td><td>投注人数</td><td>投注ARPU</td><td>投注笔数</td><td>人均频次</td> </tr>"
            + "<tr><td >cathecticMoney</td><td>winMoney</td><td>moneyGap</td><td>winMoneyRate</td><td>cathecticUserCount</td><td>cathecticARPU</td><td>cathecticNum</td><td>averageNum</td></tr>"
            + "<tr style='font-weight:bold'><td colspan='8' bgcolor='#DDDDDD'>趋势</td></tr>"
            + "<tr style='font-weight:bold'><td>日期(向前7天)</td><td>日活</td><td>投注用户数</td><td>投注转化率</td><td>新增用户</td><td>新增用户投注人数</td><td>新增投注转化率</td><td>新增次留</td></tr>";

    private static final String COMMA = ",";
    private static final int TIMES = 5;

    public void execute() {
        logger.info("开始游戏数据日报表分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        // 昨天的开始时间
        String date = DateUtils.getYesterdayDate();
        date = "2018-01-15";
        while (count <= TIMES) {
            try {
                // 获取收件人
                String receivers = dataConfigService.findByName(DataConstants.GAME_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_TCARD,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_FISH,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_NEW_THREE_KINGDOM,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_BILLIARDS,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_DART,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_BIKE,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_WARS,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_ARROWS,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_KINGDOM,date));
                    content.append(buildGameInfo(GameTypeContents.GAME_TYPE_CANDY,date));
                    content.insert(0, date + "数据如下" + "<br/><br/>");
                    // 发送邮件
                    for (String to : receivers.split(COMMA)) {
                        try {
                            emailHander.sendHtml(to,String.format("游戏数据日报表分析汇总(%s)", DateUtils.getDate()), content.toString());
                        } catch (MessagingException e) {
                            logger.error("游戏数据日报表发送失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        }
                    }
                } else {
                    logger.error("游戏数据日报表未设置收件人，traceId={}", TraceIdUtils.getTraceId());
                }
                logger.info("游戏数据日报表发送成功:traceId={}", TraceIdUtils.getTraceId());
                break;
            } catch (Exception e) {
                count++;
                if (count <= 5) {
                    logger.error("游戏数据日报表分析异常,重新执行{}，ex={}，traceId={}",count,LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                } else {
                    logger.error("游戏数据日报表分析异常，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                }
            }
        }
    }


    private String buildGameInfo(Integer gameType,String date) {
        String tableEnd = "</table><br/>";
        String demo = getTempOne(gameType, date) + getTempTwo(gameType, date) + tableEnd;
        String gameName;
        switch(gameType){
            case GameTypeContents.GAME_TYPE_TCARD:gameName="乐赢三张";break;
            case GameTypeContents.GAME_TYPE_FISH:gameName="捕鱼大冒险";break;
            case GameTypeContents.GAME_TYPE_NEW_THREE_KINGDOM:gameName="真.热血无双";break;
            case GameTypeContents.GAME_TYPE_BILLIARDS:gameName="梦想桌球";break;
            case GameTypeContents.GAME_TYPE_DART:gameName="梦想飞镖";break;
            case GameTypeContents.GAME_TYPE_BIKE:gameName="热血摩托";break;
            case GameTypeContents.GAME_TYPE_WARS:gameName="热血军团";break;
            case GameTypeContents.GAME_TYPE_ARROWS:gameName="貂蝉保卫战";break;
            case GameTypeContents.GAME_TYPE_KINGDOM:gameName="热血三国";break;
            case GameTypeContents.GAME_TYPE_CANDY:gameName="糖果夺宝";break;
            default:gameName="";break;
        }
        return demo.replace("gameName",gameName).replace("dateTime",date);
    }

    /**
     * 基础+流水数据
     */
    private String getTempOne(Integer gameType, String date) {
        // 1、新增用户数(不变)
        Integer newUser = gameService.getNewUser(gameType, date);
        Map<String,Object> map = new HashMap<>(5);
        // 3、平台日活(清洗表)
        map.put("searchDate",date);
        Integer dailyActive = datawareBuryingPointDayService.getGameDau(map);
        // 2、活跃用户(清洗表)
        map.put("gameType",gameType);
        Integer activeUser = datawareBuryingPointDayService.getGameDau(map);
        // 4、导入率
        String importRate = dailyActive == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(activeUser, dailyActive, 4), "#.##%");
        // 5、累计用户
        Integer sumUser = gameService.getSumUser(gameType);
        // 投注信息(从清洗表中取)
        // ReportGameInfo info = getBettingInfo(gameType, date);
        TcardDto info = datawareBettingLogDayService.getTcardBettingByday(map);
        if (info == null) {
            info = new TcardDto();
        }
        // 6、投注流水
        Double cathecticMoney = info.getBettingAmount();
        // 7、返奖流水
        Double winMoney = info.getResultAmount();
        // 8、应用流水差
        Double moneyGap = cathecticMoney - winMoney;
        // 9、返奖率
        String winMoneyRate = cathecticMoney == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(winMoney, cathecticMoney, 4), "#.##%");
        // 10、投注人数
        Integer cathecticUserCount = info.getUserCount();
        // 11、投注ARPU
        String cathecticARPU = cathecticUserCount == 0 ? "0" : NumberUtils.format(BigDecimalUtil.div(cathecticMoney, cathecticUserCount, 4), "#.#");
        // 12、投注笔数
        Integer cathecticNum = info.getBettingCount();
        // 13、人均频次
        String averageNum = cathecticUserCount == 0 ? "0" : NumberUtils.format(BigDecimalUtil.div(cathecticNum, cathecticUserCount, 4), "#.#");
         return CONTENT_TEMP_ONE
        .replace("newUser", newUser.toString())
        .replace("activeUser", activeUser.toString())
        .replace("dailyActive", dailyActive.toString())
        .replace("importRate", importRate)
        .replace("sumUser", sumUser.toString())
        .replace("cathecticMoney", cathecticMoney.toString())
        .replaceFirst("winMoney", winMoney.toString())
        .replace("moneyGap", moneyGap.toString())
        .replace("winMoneyRate", winMoneyRate)
        .replace("cathecticUserCount", cathecticUserCount.toString())
        .replace("cathecticARPU", cathecticARPU)
        .replace("cathecticNum", cathecticNum.toString())
        .replace("averageNum", averageNum);
    }

    /**
     * 趋势(前7天~前1天)
     */
    private String getTempTwo(Integer gameType, String date){
        StringBuffer sb = new StringBuffer();
        String beginDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date), 7));
        String endDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date), 1));
        List<String> list = DateUtils.getDateList(beginDate,endDate);
        String temp = "<tr><td>date</td><td>activeUser</td><td>bettingUser</td><td>bettingRate</td><td>newUser</td><td>newBettingUser</td><td>newBettingRate</td><td>newRemainRate</td></tr>";
        Map<String,Object> map = new HashMap<>(5);
        map.put("gameType",gameType);
        for (String dat : list) {
            // 1、日期
            // 2、活跃用户(清洗表)
            map.put("searchDate",dat);
            Integer activeUser = datawareBuryingPointDayService.getGameDau(map);
            // 3、投注用户数
            List<Long> bettingUserIds = datawareBettingLogDayService.getBettingUserIds(map);
            Integer bettingUser;
            if(CollectionUtils.isEmpty(bettingUserIds)){
                bettingUserIds = new ArrayList<>();
                bettingUser = 0;
            }else{
                bettingUser = bettingUserIds.size();
            }
            // 4、投注转化率
            String bettingRate = activeUser == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(bettingUser, activeUser, 4), "#.##%");
            // 5、新增用户
            Integer newUser = gameService.getNewUser(gameType,dat);
            // 6、新增用户中的投注人数
            // 新增用户列表
            List<Long> newUserList = gameService.getNewUserList(gameType,dat);
            // 投注列表
            Integer newBettingUser = CollectionUtils.intersection(newUserList,bettingUserIds).size();
            // 7、新增投注转化率
            String newBettingRate = newUser == 0 ? "0%" : NumberUtils.format(BigDecimalUtil.div(newBettingUser, newUser, 4), "#.##%");
            // 8、新增次日留存
            String newRemainRate = gameService.getRemainRate(gameType, dat);
            String result = temp
            .replace("date", dat).replace("activeUser", activeUser.toString())
            .replace("bettingUser", bettingUser.toString()).replace("bettingRate", bettingRate)
            .replace("newUser", newUser.toString()).replace("newBettingUser", newBettingUser.toString())
            .replace("newBettingRate", newBettingRate).replace("newRemainRate", newRemainRate);
            sb.append(result);
        }
        return sb.toString();
    }

}
