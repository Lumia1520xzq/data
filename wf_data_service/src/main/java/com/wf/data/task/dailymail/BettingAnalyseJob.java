package com.wf.data.task.dailymail;

import com.wf.base.rpc.ConfigRpcService;
import com.wf.core.email.EmailHander;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.GameTypeContents;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.dao.entity.mysql.ReportGameInfo;
import com.wf.data.service.ReportChangeNoteService;
import com.wf.data.service.RoomFishInfoNewService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.elasticsearch.EsUicAllGameService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 平台投注人数分析汇总
 * 定时发送邮件
 * 处理获取数据失败问题，重试15次
 * Created by jianjian on 2017/08/24
 */
@Component
public class BettingAnalyseJob  {
    private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UicGroupService uicGroupService;
	@Autowired
	private ReportChangeNoteService reportService;
	@Autowired
	private EsUicAllGameService gameService;
	@Autowired
    private RoomFishInfoNewService roomFishInfoNewService;
    
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



    
	//查询内部人员
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }
    
    private String format(Object obj) {
    	DecimalFormat df = new DecimalFormat("#,###");
    	String str = df.format(obj);
    	return str;
    }
    
    public void execute() {
        logger.info("开始投注人数分析");

        ConfigRpcService configRpcService = SpringContextHolder.getBean(ConfigRpcService.class);
        EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);

        byte count = 0;
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        //处理0点时的统计数据，当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -1); 
        while (count <= 15) {
            try {
                String receivers = configRpcService.findByName(DataConstants.GAME_BETTING_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(EMAIL_STYLE);
                    //所有游戏汇总信息
                    content.append(buildSumInfo(cal));
                    content.append(buildDartInfo(cal));
                    content.append(buildBilliardInfo(cal));
                    content.append(buildWarInfo(cal));
                    content.append(buildArrowInfo(cal));
                    content.append(buildFootballInfo(cal));
                    content.append(buildMotorInfo(cal));
                    content.append(buildKingdomInfo(cal));
                    content.append(buildFishInfo(cal));
                    content.append(buildQuoitsInfo(cal));
                    content.insert(0, "截止" + DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN + " HH:00") + "<br/><br/>");
                    for (String to : receivers.split(",")) {
                        try {
                            emailHander.sendHtml(to, String.format("截至%s %s投注情况汇总",
                                            DateUtils.formatDate(currDate, DateUtils.DATE_PATTERN),
                                            DateUtils.formatDate(currDate, DateUtils.MINUTE_PATTERN)),
                                    content.toString());
                        } catch (MessagingException e) {
                            logger.error("Dart投注人数分析发送失败：" + to);
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

    private String buildSumInfo(Calendar cal) {
    	  String temp = getSumTemp(cal);
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
    
    private String buildFootballInfo(Calendar cal) {
        String temp = getTemp(cal, 7);
        return temp.replace("GAME_NAME", "足球竞猜");
    }
    
    private String buildMotorInfo(Calendar cal) {
        String temp = getTemp(cal, 8);
        return temp.replace("GAME_NAME", "热血摩托");
    }
    
    private String buildKingdomInfo(Calendar cal) {
        String temp = getTemp(cal, 9);
        return temp.replace("GAME_NAME", "多多三国");
    }
    
    private String buildFishInfo(Calendar cal) {
        String temp = getFishTemp(cal);
        return temp.replace("GAME_NAME","多多捕鱼");
    }
    
    private String buildQuoitsInfo(Calendar cal) {
        String temp = getTemp(cal,3);
        return temp.replace("GAME_NAME", "欢乐套圈");
    }
    
    private Map<String,Object> getParams(Calendar cal,Integer gameType,Integer searchType){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("gameType",gameType);
        params.put("userIds",getInternalUserIds());
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
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int hourAfter = hour + 1;
        String timeSection = String.format("%s:00——%s:00", (hour < 10 ? "0" + hour : hour),
        (hourAfter < 10 ? "0" + hourAfter : hourAfter));
        //今天所有的投注情况 
        ReportGameInfo dayInfo = reportService.findCathecticListByDate(getParams(cal,gameType,0));
        if(dayInfo==null){
        	dayInfo=new ReportGameInfo();
        }
        //过去一小时的投注情况 
        ReportGameInfo hourInfo = reportService.findCathecticListByDate(getParams(cal,gameType, 1));
        if(hourInfo==null){
        	hourInfo=new ReportGameInfo();
        }
        //DAU
        Integer dailyActive = gameService.getActiveUser(gameType,DateUtils.formatDate(cal.getTime()));
	    return CONTENT_TEMP
		.replace("TODAY", (today < 10 ? ("0" + today) : String.valueOf(today)))
		.replace("DAILY_ACTIVE",format(dailyActive))
		.replaceFirst("BETTING_AMOUNT",format((dayInfo.getCathecticMoney())))
		.replaceFirst("AWARD_AMOUNT", format((dayInfo.getWinMoney())))
		.replaceFirst("BETTING_USER", format((dayInfo.getCathecticUserNum())))
		.replaceFirst("BETTING_NUM", format((dayInfo.getCathecticNum())))
		.replaceFirst("AWARD_RATE", dayInfo.getCathecticMoney()==0L?"0%":
	     NumberUtils.format(BigDecimalUtil.div(dayInfo.getWinMoney(),dayInfo.getCathecticMoney(),4),"#.##%"))
        .replace("TIME_SECTION", timeSection)
        .replace("HOUR_BETTING_AMOUNT",format((hourInfo.getCathecticMoney())))
        .replace("HOUR_AWARD_AMOUNT", format((hourInfo.getWinMoney())))
        .replace("HOUR_BETTING_USER", format((hourInfo.getCathecticUserNum())))
        .replace("HOUR_BETTING_NUM", format((hourInfo.getCathecticNum())))
        .replace("HOUR_AWARD_RATE", hourInfo.getCathecticMoney()==0L?"0%":
   	     NumberUtils.format(BigDecimalUtil.div(hourInfo.getWinMoney(),hourInfo.getCathecticMoney(),4),"#.##%"));
    }
    
    
    private String getFishTemp(Calendar cal){
        int today = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int hourAfter = hour + 1;
        String timeSection = String.format("%s:00——%s:00", (hour < 10 ? "0" + hour : hour),
        (hourAfter < 10 ? "0" + hourAfter : hourAfter));
        //今天所有的投注情况 
        ReportGameInfo dayInfo = roomFishInfoNewService.findBettingInfoByDate(getParams(cal,null,0));
        if(dayInfo==null){
        	dayInfo=new ReportGameInfo();
        }
        //过去一小时的投注情况 
        ReportGameInfo hourInfo = roomFishInfoNewService.findBettingInfoByDate(getParams(cal,null,1));
        if(hourInfo==null){
        	hourInfo=new ReportGameInfo();
        }
        //DAU
        Integer dailyActive = gameService.getActiveUser(GameTypeContents.GAME_TYPE_FISH,DateUtils.formatDate(cal.getTime()));
	    return CONTENT_TEMP
		.replace("TODAY", (today < 10 ? ("0" + today) : String.valueOf(today)))
		.replace("DAILY_ACTIVE",format(dailyActive))
		.replaceFirst("BETTING_AMOUNT",format((dayInfo.getCathecticMoney())))
		.replaceFirst("AWARD_AMOUNT", format((dayInfo.getWinMoney())))
		.replaceFirst("BETTING_USER", format((dayInfo.getCathecticUserNum())))
		.replaceFirst("BETTING_NUM", format((dayInfo.getCathecticNum())))
		.replaceFirst("AWARD_RATE", dayInfo.getCathecticMoney()==0L?"0%":
	     NumberUtils.format(BigDecimalUtil.div(dayInfo.getWinMoney(),dayInfo.getCathecticMoney(),4),"#.##%"))
        .replace("TIME_SECTION", timeSection)
        .replace("HOUR_BETTING_AMOUNT",format((hourInfo.getCathecticMoney())))
        .replace("HOUR_AWARD_AMOUNT", format((hourInfo.getWinMoney())))
        .replace("HOUR_BETTING_USER", format((hourInfo.getCathecticUserNum())))
        .replace("HOUR_BETTING_NUM", format((hourInfo.getCathecticNum())))
        .replace("HOUR_AWARD_RATE", hourInfo.getCathecticMoney()==0L?"0%":
   	     NumberUtils.format(BigDecimalUtil.div(hourInfo.getWinMoney(),hourInfo.getCathecticMoney(),4),"#.##%"));
    }
    
    //捕鱼+其他数据汇总
    private String getSumTemp(Calendar cal){
        int today = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int hourAfter = hour + 1;
        String timeSection = String.format("%s:00——%s:00", (hour < 10 ? "0" + hour : hour),
        (hourAfter < 10 ? "0" + hourAfter : hourAfter));
        //今天所有的投注情况(除捕鱼) 
        ReportGameInfo daySumInfo = reportService.findCathecticListByDate(getParams(cal,null,0));
        if(daySumInfo==null){
        	daySumInfo=new ReportGameInfo();
        }
        //过去一小时的投注情况 (除捕鱼)
        ReportGameInfo hourSumInfo = reportService.findCathecticListByDate(getParams(cal,null,1));
        if(hourSumInfo==null){
        	hourSumInfo=new ReportGameInfo();
        }
        //今天所有的投注情况(捕鱼) 
        ReportGameInfo dayFishInfo = roomFishInfoNewService.findBettingInfoByDate(getParams(cal,null,0));
        if(dayFishInfo==null){
        	dayFishInfo=new ReportGameInfo();
        }
        //过去一小时的投注情况(捕鱼)
        ReportGameInfo hourFishInfo = roomFishInfoNewService.findBettingInfoByDate(getParams(cal,null,1));
        if(hourFishInfo==null){
        	hourFishInfo=new ReportGameInfo();
        }
        //DAU
        Integer dailyActive = gameService.getActiveUser(null,DateUtils.formatDate(cal.getTime()));
        //投注用户 需要去重
        
        List<Long> daySumUserIds = reportService.findBettingUsersByDate(getParams(cal,null,0));
        List<Long> dayFishUserIds = roomFishInfoNewService.findBettingUsersByDate(getParams(cal,null,0));
        Integer bettingUser=getBettingUsers(daySumUserIds,dayFishUserIds);
        
        List<Long> hourSumUserIds = reportService.findBettingUsersByDate(getParams(cal,null,1));
        List<Long> hourFishUserIds = roomFishInfoNewService.findBettingUsersByDate(getParams(cal,null,1));
        Integer hourBettingUser=getBettingUsers(hourSumUserIds,hourFishUserIds);
        
        
	    return CONTENT_TEMP
		.replace("TODAY", (today < 10 ? ("0" + today) : String.valueOf(today)))
		.replace("DAILY_ACTIVE",format(dailyActive))
		.replaceFirst("BETTING_AMOUNT",format( daySumInfo.getCathecticMoney()+dayFishInfo.getCathecticMoney() ))
		.replaceFirst("AWARD_AMOUNT", format( daySumInfo.getWinMoney()+dayFishInfo.getWinMoney() ) )
//		.replaceFirst("BETTING_USER", format( daySumInfo.getCathecticUserNum()+dayFishInfo.getCathecticUserNum() ))
		.replaceFirst("BETTING_USER", format( bettingUser ))
		.replaceFirst("BETTING_NUM", format( daySumInfo.getCathecticNum()+dayFishInfo.getCathecticNum() ))
		.replaceFirst("AWARD_RATE", (daySumInfo.getCathecticMoney()+dayFishInfo.getCathecticMoney())==0L?"0%":
	     NumberUtils.format(BigDecimalUtil.div(daySumInfo.getWinMoney()+dayFishInfo.getWinMoney(),
	     daySumInfo.getCathecticMoney()+dayFishInfo.getCathecticMoney(),4),"#.##%"))
        .replace("TIME_SECTION", timeSection)
        .replace("HOUR_BETTING_AMOUNT",format( hourSumInfo.getCathecticMoney()+hourFishInfo.getCathecticMoney() ))
        .replace("HOUR_AWARD_AMOUNT", format( hourSumInfo.getWinMoney()+hourFishInfo.getWinMoney() ))
//        .replace("HOUR_BETTING_USER", format( hourSumInfo.getCathecticUserNum()+hourFishInfo.getCathecticUserNum() ))
        .replace("HOUR_BETTING_USER", format( hourBettingUser ))
        .replace("HOUR_BETTING_NUM", format( hourSumInfo.getCathecticNum()+hourFishInfo.getCathecticNum() ))
        .replace("HOUR_AWARD_RATE", (hourSumInfo.getCathecticMoney()+hourFishInfo.getCathecticMoney())==0L?"0%":
   	     NumberUtils.format(BigDecimalUtil.div(hourSumInfo.getWinMoney()+hourFishInfo.getWinMoney(),
   	     hourSumInfo.getCathecticMoney()+hourFishInfo.getCathecticMoney(),4),"#.##%"));
    }
    
    private Integer getBettingUsers(List<Long> allUserIds,List<Long> fishUserIds){
    	if(CollectionUtils.isEmpty(allUserIds)&&CollectionUtils.isEmpty(fishUserIds)){
    		return 0;
    	}
    	if(CollectionUtils.isEmpty(allUserIds)) {
    	  return fishUserIds.size();  
    	}
    	if(CollectionUtils.isEmpty(fishUserIds)){
    	  return allUserIds.size();
    	}
    	Integer count=allUserIds.size()+fishUserIds.size();
    	for(Long id:allUserIds){
    		if(fishUserIds.contains(id)){
    			count--;
    		}
    	}
    	return count;
    }
    
}
