package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.dao.entity.mysql.ReportGameInfo;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.ReportChangeNoteService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.elasticsearch.EsUicBuryingPointService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日关键数值汇总
 *
 * @author jdd
 */
@Component
public class DailyKeyDataJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final EsUicBuryingPointService uicBuryingPointService = SpringContextHolder.getBean(EsUicBuryingPointService.class);
    private final ReportChangeNoteService reportChangeNoteService = SpringContextHolder.getBean(ReportChangeNoteService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);

    private final String CONTENT_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse' >" +
            "<tr style='font-weight:bold'><td>日期</td><td>充值金额</td><td>日活人数</td><td>投注人数</td><td>充值人数</td><td>充值笔数</td></tr>" +
            "<tr><td>date</td><td>rechargeSum</td><td>dailyActive</td><td>dailyBetting</td><td>dailyRecharge</td><td>rechargeNum</td></tr>" +
            "</table>";

    private final String CONTENT_TWO_START = "<table border='1' style='text-align: center ; border-collapse: collapse'>" +
            "<tr style='font-weight:bold'><td>游戏</td><td>日总投注金叶子</td><td>日总派奖金叶子</td><td>日投注人数</td><td>日投注笔数</td></tr>";
    private final String CONTENT_TWO_PART = "<tr><td>game</td><td>bettingSum</td><td>awardSum</td><td>bettingMan</td><td>bettingNum</td></tr>";
    private final String CONTENT_TWO_END = "</table>";

    public void execute() {
        logger.info("开始查询每日关键数值");
        byte count = 0;
        //昨天的日期
        String date = DateUtils.getYesterdayDate();
        while (count <= 5) {
            try {
                String receivers = dataConfigService.findByName(DataConstants.DAILY_KEY_DATA).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    //所有游戏汇总信息 
                    Long channelId = null;
                    content.append("汇总数据：所有渠道汇总数值" + "<br/><br/>");
                    content.append(this.getSumDataTemplate(date, channelId) + "<br/><br/>");
                    content.append(CONTENT_TWO_START);
                    content.append(getAllGameData(date, channelId));
                    content.append(getDartGameData(date, channelId));
                    content.append(getBilliardGameData(date, channelId));
                    content.append(getWarGameData(date, channelId));
                    content.append(getArrowGameData(date, channelId));
                    content.append(getFootballGameData(date, channelId));
                    content.append(getMotorGameData(date, channelId));
                    content.append(getKingdomGameData(date, channelId));
                    content.append(getFishGameData(date, channelId));
                    content.append(CONTENT_TWO_END);

                    content.append("<br/>☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆<br/><br/>");

                    //金山渠道
                    channelId = 100001L;
                    content.append("奖多多渠道(金山)" + "<br/><br/>");
                    content.append(this.getSumDataTemplate(date, channelId) + "<br/><br/>");
                    content.append(CONTENT_TWO_START);
                    content.append(getAllGameData(date, channelId));
                    content.append(getDartGameData(date, channelId));
                    content.append(getBilliardGameData(date, channelId));
                    content.append(getWarGameData(date, channelId));
                    content.append(getArrowGameData(date, channelId));
                    content.append(getFootballGameData(date, channelId));
                    content.append(getMotorGameData(date, channelId));
                    content.append(getKingdomGameData(date, channelId));
                    content.append(getFishGameData(date, channelId));
                    content.append(CONTENT_TWO_END);

                    content.append("<br/>☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆<br/><br/>");

                    //澳客渠道
                    channelId = 100006L;
                    content.append("澳客渠道" + "<br/><br/>");
                    content.append(this.getSumDataTemplate(date, channelId) + "<br/><br/>");
                    content.append(CONTENT_TWO_START);
                    content.append(getAllGameData(date, channelId));
                    content.append(getDartGameData(date, channelId));
                    content.append(getBilliardGameData(date, channelId));
                    content.append(getWarGameData(date, channelId));
                    content.append(getArrowGameData(date, channelId));
                    content.append(getFootballGameData(date, channelId));
                    content.append(getMotorGameData(date, channelId));
                    content.append(getKingdomGameData(date, channelId));
                    content.append(getFishGameData(date, channelId));
                    content.append(CONTENT_TWO_END);

                    for (String to : receivers.split(",")) {
                        try {
                            emailHander.sendHtml(to, String.format("%s 关键数值汇总", date), content.toString());
                        } catch (MessagingException e) {
                            logger.error("每日关键数值发送失败：" + to);
                        }
                    }
                } else {
                    logger.error(">>>>>>>>>>>>每日关键数值接收人未设置");
                }
                logger.info(">>>>>>>>>>>>>>每日关键数值发送正常结束");
                break;
            } catch (Exception ex) {
                count++;
                if (count <= 5) {
                    logger.error(">>>>>>>>>>>>>>每日关键数值发送异常,重新执行 " + count, ex);
                } else {
                    logger.error(">>>>>>>>>>>>>>每日关键数值重新发送失败", ex);
                }
            }
        }
    }

    private String getSumDataTemplate(String date, Long channelId) {
        //充值金额(trans_convert)
        Double rechargeSum = transConvertService.findSumRechargeByTime(toMap(date, channelId, null));
        //日活人数(es)
        Integer dailyActive = uicBuryingPointService.getActiveCount(date, channelId);
        //投注人数(es)
        Integer dailyBetting = reportChangeNoteService.getCathecticUserNum(toMap(date, channelId, null));
        //充值人数(trans_convert)
        List<Long> rechargeIds = transConvertService.getRechargeUserIdsByDay(toMap(date, channelId, null));
        Integer dailyRecharge = CollectionUtils.isEmpty(rechargeIds) ? 0 : rechargeIds.size();
        //充值笔数
        Long rechargeNum = transConvertService.getRechargeCountByDay(toMap(date, channelId, null));
        return CONTENT_ONE
                .replace("date", date)
                .replace("rechargeSum", rechargeSum.toString())
                .replace("dailyActive", dailyActive.toString())
                .replace("dailyBetting", dailyBetting.toString())
                .replace("dailyRecharge", dailyRecharge.toString())
                .replace("rechargeNum", rechargeNum.toString());
    }

    private Map<String, Object> toMap(String date, Long channelId, Integer gameType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginDate", date + " 00:00:00");
        map.put("endDate", date + " 23:59:59");
        map.put("channelId", channelId);
        map.put("userIds", getInternalUserIds());
        map.put("gameType", gameType);
        return map;
    }

    private String getAllGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, null);
        return temp.replace("game", "疯狂游戏厅");
    }

    private String getDartGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 1);
        return temp.replace("game", "梦想飞镖");
    }

    private String getBilliardGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 2);
        return temp.replace("game", "梦想桌球");
    }

    private String getWarGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 4);
        return temp.replace("game", "热血军团");
    }

    private String getArrowGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 5);
        return temp.replace("game", "貂蝉保卫战");
    }

    private String getFootballGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 7);
        return temp.replace("game", "足球竞猜");
    }

    private String getMotorGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 8);
        return temp.replace("game", "热血摩托");
    }

    private String getKingdomGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 9);
        return temp.replace("game", "多多三国");

    }

    private String getFishGameData(String date, Long channelId) {
        String temp = getGameDataTemplate(date, channelId, 10);
        return temp.replace("game", "多多捕鱼");
    }

    //模板
    private String getGameDataTemplate(String date, Long channelId, Integer gameType) {
        Map<String, Object> params = toMap(date, channelId, gameType);
        ReportGameInfo info = reportChangeNoteService.findCathecticListByDate(params);
        if (info == null) {
            info = new ReportGameInfo();
        }
        //日投注金叶子
        Long bettingSum = info.getCathecticMoney();
        //日总派奖金叶子数
        Long awardSum = info.getWinMoney();
        //日投注人数
        Integer bettingMan = info.getCathecticUserNum();
        //日投注笔数
        Long bettingNum = info.getCathecticNum();
        return CONTENT_TWO_PART
                .replace("bettingSum", bettingSum.toString())
                .replace("awardSum", awardSum.toString())
                .replace("bettingMan", bettingMan.toString())
                .replace("bettingNum", bettingNum.toString());
    }

    //查询内部人员的Id
    private List<Long> getInternalUserIds() {
        List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
        return userIds;
    }

}
