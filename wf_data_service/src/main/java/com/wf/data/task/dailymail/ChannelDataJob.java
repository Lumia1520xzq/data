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
import com.wf.data.dao.entity.mysql.ReportGameInfo;
import com.wf.data.service.ReportChangeNoteService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.elasticsearch.EsUicChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日各渠道关键数据 
 * @author jianjian huang
 * 2017年8月23日 
 */
@Component
public class ChannelDataJob  {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ReportChangeNoteService reportChangeNoteService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private TransConvertService transConvertService;
    @Autowired
    private EsUicChannelService channelService;

    private final String CONTENT_TEMP_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse'>" 
    +"<tr style='font-weight:bold'><td>渠道</td><td>充值金额</td><td>日活</td><td>投注用户数</td><td>投注转化率</td><td>充值用户数</td><td>付费渗透率</td><td>投注流水</td><td>返奖流水</td><td>返奖率</td><td>新增人数</td><td>新用户投注转化率</td><td>新用户次留</td></tr>";
    private final String TABLE_END = "</table>";
    private final String TEMPLATE="<tr><td>channel</td><td>rechargeSum</td><td>activeUser</td><td>bettingUser</td><td>bettingRate</td><td>rechargeUser</td><td>payRate</td><td>cathecticMoney</td><td>winMoney</td><td>winMoneyRate</td><td>newUser</td><td>newBettingRate</td><td>newRemainRate</td></tr>";
    

    public void execute() {
	logger.info("开始每日各渠道关键数据分析");
	ConfigRpcService configRpcService = SpringContextHolder.getBean(ConfigRpcService.class);
	EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);

		byte count = 0;
	// 昨天的开始时间
	String date = DateUtils.getYesterdayDate();
	while (count <= 5) {
	    try {
		// 获取收件人 ------ 收件人要改
		String receivers = configRpcService.findByName(DataConstants.CHANNEL_DATA_RECEIVER).getValue();
		if (StringUtils.isNotEmpty(receivers)) {
		    StringBuilder content = new StringBuilder();
		    content.append(CONTENT_TEMP_ONE);
		    content.append(buildJSData(date));
		    content.append(buildOkoooData(date));
		    content.append(buildIosData(date));
		    content.append(buildGetuiData(date));
		    content.append(buildWeituoData(date));
		    content.append(TABLE_END);
		    content.insert(0, date +"数据如下" + "<br/><br/>");
		    // 发送邮件
		    for (String to : receivers.split(",")) {
			try {
			    emailHander.sendHtml(to,String.format("每日各渠道关键数据分析汇总(%s)",DateUtils.getDate()),content.toString());
			} catch (MessagingException e) {
			    logger.error("每日各渠道关键数据分析发送失败：" + to);
			}
		    }
		} else {
		    logger.error(">>>>>>>>>>>>每日各渠道关键数据分析接收人未设置");
		}
		break;
	    } catch (Exception e) {
		count++;
		if (count <= 5) {
		    logger.error(">>>>>>>>>>>>>>每日各渠道关键数据分析异常，重新执行 " + count, e);
		} else {
		    logger.error(">>>>>>>>>>>>>>每日各渠道关键数据分析异常重新执行失败，停止分析", e);
		}

	    }
	}
    }
    
    private String buildJSData(String date){
    	return getTemplate(date,100001L).replace("channel","奖多多(100001)");
    }
    
    private String buildOkoooData(String date){
    	return getTemplate(date,100006L).replace("channel","澳客(100006)");
    }
    
    private String buildIosData(String date){
    	return getTemplate(date, 300001L).replace("channel", "ios(300001)");
    }
    
    private String buildGetuiData(String date){
    	return getTemplate(date, 200001001L).replace("channel", "应用宝(200001001)");
    }
    
    private String buildWeituoData(String date){
    	return getTemplate(date, 200001021L).replace("channel", "微拓(200001021)");
    }

	/**
	 * 各渠道数据模板
	 * @param date
	 * @param channelId
	 * @return
	 */
    private String getTemplate(String date,Long channelId) {
    	StringBuffer sb=new StringBuffer();
    	String temp = TEMPLATE;
    		// 1、渠道(稍后计算)
    		// 2、充值金额
    	    Double rechargeSum = transConvertService.findSumRechargeByTime(toMap(date,channelId));
    	    // 3、日活
    	    Integer activeUser = channelService.getActiveUser(date,channelId); 
    		ReportGameInfo info = getBettingInfo(date,channelId);
    		if(info==null){
    		info=new ReportGameInfo();
    		}
    		// 4、投注用户数
    		Integer bettingUser= info.getCathecticUserNum();
    		// 5、投注转化率
    		String bettingRate=activeUser==0?"0%": NumberUtils.format(BigDecimalUtil.div(bettingUser,activeUser,4),"#.##%");
    		// 6、充值用户数
    		Integer rechargeUser=getRechargeUser(date,channelId);
    		// 7、付费渗透率(充值用户数/投注用户数)
    		String payRate=bettingUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(rechargeUser,bettingUser,4),"#.##%");
    		// 8、投注流水
    		Long  cathecticMoney = info.getCathecticMoney();
    		// 9、返奖流水
    		Long winMoney=info.getWinMoney();
    		// 10、返奖率
    		String winMoneyRate=cathecticMoney==0?"0%":NumberUtils.format(BigDecimalUtil.div(winMoney,cathecticMoney,4),"#.##%");   		
    		// 11、新增用户
    		Integer	newUser= channelService.getNewUser(date,channelId);
    		// 新增用户中的投注人数
    		Integer newBettingUser = channelService.getNewBettingUser(date,channelId);
    		// 12、新增投注转化率
    		String newBettingRate=newUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(newBettingUser,newUser,4),"#.##%");
    		// 13、新增次日留存
    		String newRemainRate=channelService.getRemainRate(date,channelId);
    		String result=temp
    		.replace("rechargeSum",rechargeSum.toString()).replace("activeUser", activeUser.toString()).replace("bettingUser", bettingUser.toString())
    		.replace("bettingRate", bettingRate).replace("rechargeUser", rechargeUser.toString()).replace("payRate", payRate.toString())
    		.replace("cathecticMoney", cathecticMoney.toString()).replaceFirst("winMoney", winMoney.toString()).replace("winMoneyRate", winMoneyRate)
    		.replace("newUser", newUser.toString()).replace("newBettingRate",newBettingRate.toString()).replace("newRemainRate", newRemainRate.toString());
    		sb.append(result);
    	return sb.toString();
    }
    
    //趋势=>充值用户数
    private Integer getRechargeUser(String date,Long channelId){
    	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("beginDate", date+" 00:00:00");
    	params.put("endDate", date+" 23:59:59");
    	params.put("channelId",channelId);
    	List<Long> list=transConvertService.getRechargeUserIdsByDay(params);
    	return list.size();
    }
    
    private Map<String,Object> toMap(String date,Long channelId){
    	Map<String,Object> map=new HashMap<String,Object>();
    	map.put("beginDate",date+" 00:00:00");
    	map.put("endDate", date+" 23:59:59");
    	map.put("channelId",channelId);
    	map.put("userIds", getInternalUserIds());
    	return map;
    } 
    
    //投注信息
    private ReportGameInfo getBettingInfo(String date,Long channelId){
      	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("beginDate",date+" 00:00:00");
    	params.put("endDate",date+" 23:59:59");
    	params.put("userIds",getInternalUserIds());
    	params.put("channelId",channelId);
    	ReportGameInfo bettingInfo = reportChangeNoteService.findCathecticListByDate(params);
    	return bettingInfo;
    }
    
	//查询内部人员的Id
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }

}
