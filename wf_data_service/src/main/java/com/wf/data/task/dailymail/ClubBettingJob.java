package com.wf.data.task.dailymail;

import com.wf.base.rpc.ConfigRpcService;
import com.wf.core.email.EmailHander;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.dao.data.entity.ReportGameInfo;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.ReportChangeNoteService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.elasticsearch.EsClubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 俱乐部邮件
 * 定时发送邮件
 * 处理获取数据失败问题，重试15次
 * Created by jianjian on 2017/10/23
 */
public class ClubBettingJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReportChangeNoteService reportService = SpringContextHolder.getBean(ReportChangeNoteService.class);
    private final EsClubService clubService = SpringContextHolder.getBean(EsClubService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);

    private static final Long CLUB_ANDROID_CHANNEL = 400001001L;
    
    private static final Long CLUB_IOS_CHANNEL = 500001001L;
	
    
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
            "</table>";



    
	//查询内部人员
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }
    
    //俱乐部渠道list
    private List<Long> getClubChannelIds(){
	List<Long> channelIds = Arrays.asList(CLUB_ANDROID_CHANNEL,CLUB_IOS_CHANNEL);
	return channelIds;
    }
    
    
    private String format(Object obj) {
    	DecimalFormat df = new DecimalFormat("#,###");
    	String str = df.format(obj);
    	return str;
    }
    
    public void execute() {
        logger.info("开始投注人数分析");

        byte count = 0;
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        //处理0点时的统计数据，当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -1); 
        while (count <= 15) {
            try {
                String receivers = dataConfigService.findByName(DataConstants.CLUB_BETTING_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(EMAIL_STYLE);
                    //所有游戏汇总信息
                    content.append(buildSumInfo(cal));
                    content.append(buildDartInfo(cal));
                    content.append(buildBilliardInfo(cal));
                    content.append(buildWarInfo(cal));
                    content.append(buildArrowInfo(cal));
                    content.append(buildMotorInfo(cal));
                    content.append(buildKingdomInfo(cal));
                    content.insert(0, "截止" + DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN + " HH:00") + "<br/><br/>");
                    for (String to : receivers.split(",")) {
                        try {
                            emailHander.sendHtml(to, String.format("截至%s %s俱乐部渠道投注情况汇总",
                                            DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN),
                                            DateUtils.formatDate(currDate, DateUtils.MINUTE_PATTERN)),
                                    content.toString());
                        } catch (MessagingException e) {
                            logger.error("俱乐部邮件分析发送失败：" + to);
                        }
                    }
                } else {
                    logger.error(">>>>>>>>>>>>投注人数分析接收人未设置");
                }
                logger.info(">>>>>>>>>>>>>>投注人数分析正常结束");
                break;
            } catch (Exception ex) {
                count++;
                if (count <= 15) {
                    logger.error(">>>>>>>>>>>>>>投注人数分析异常，重新执行 " + count, ex);
                } else {
                    logger.error(">>>>>>>>>>>>>>投注人数分析重新执行失败，停止分析", ex);
                }
            }
        }
    }
    
    private String buildSumInfo (Calendar cal) {
    	String temp = getTemp(cal, null);
    	return temp.replace("GAME_NAME", "多多游戏");
    }

    private String buildDartInfo(Calendar cal) {
        String temp = getTemp(cal, 1);
        return temp.replace("GAME_NAME", "梦想飞镖");
    }
    
    private String buildBilliardInfo(Calendar cal) {
        String temp = getTemp(cal, 2);
        return temp.replace("GAME_NAME", "梦想桌球");
    }
    
    private String buildWarInfo(Calendar cal) {
        String temp = getTemp(cal, 4);
        return temp.replace("GAME_NAME", "热血军团");
    }
    
    private String buildArrowInfo(Calendar cal) {
        String temp = getTemp(cal, 5);
        return temp.replace("GAME_NAME", "貂蝉保卫战");
    }
    
    private String buildMotorInfo(Calendar cal) {
        String temp = getTemp(cal, 8);
        return temp.replace("GAME_NAME", "热血摩托");
    }
    
    private String buildKingdomInfo(Calendar cal) {
        String temp = getTemp(cal, 9);
        return temp.replace("GAME_NAME", "多多三国");
    }
    
    private Map<String,Object> getParams(Calendar cal,Integer gameType,Integer searchType){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("gameType",gameType);
        params.put("userIds",getInternalUserIds());
        params.put("channelIds",getClubChannelIds());
        if(searchType==1){
        String hourDateStr = DateUtils.formatDate(cal.getTime(), DateUtils.DATE_PATTERN + " HH");
        params.put("beginDate",hourDateStr + ":00:00");	
        params.put("endDate", hourDateStr + ":59:59");	
        }else {
        String date = DateUtils.formatDate(cal.getTime());
        params.put("beginDate",date+" 00:00:00");
        params.put("endDate",date+" 23:59:59");
        }
        return params;
    }

    private String getTemp(Calendar cal,Integer gameType){
        int today = cal.get(Calendar.DATE);
        //今天所有的投注情况 
        ReportGameInfo dayInfo = reportService.findCathecticListByDate(getParams(cal,gameType,0));
        if(dayInfo==null){
        dayInfo=new ReportGameInfo();
        }
        //DAU
        Integer dailyActive = clubService.getActiveUser(gameType,DateUtils.formatDate(cal.getTime()),getClubChannelIds());
	    return CONTENT_TEMP
		.replace("TODAY", (today < 10 ? ("0" + today) : String.valueOf(today)))
		.replace("DAILY_ACTIVE",format(dailyActive))
		.replaceFirst("BETTING_AMOUNT",format((dayInfo.getCathecticMoney())))
		.replaceFirst("AWARD_AMOUNT", format((dayInfo.getWinMoney())))
		.replaceFirst("BETTING_USER", format((dayInfo.getCathecticUserNum())))
		.replaceFirst("BETTING_NUM", format((dayInfo.getCathecticNum())))
		.replaceFirst("AWARD_RATE", dayInfo.getCathecticMoney()==0L?"0%":
	    NumberUtils.format(BigDecimalUtil.div(dayInfo.getWinMoney(),dayInfo.getCathecticMoney(),4),"#.##%"));
     
    }
    
}
