package com.wf.data.task.dailymail;

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
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.elasticsearch.EsUicPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台数据报表 
 * @author jianjian huang
 * 2017年8月23日 
 */
public class PlatformDataJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());


	private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
	private final ReportChangeNoteService reportChangeNoteService = SpringContextHolder.getBean(ReportChangeNoteService.class);
	private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
	private final EsUicPlatformService platformService = SpringContextHolder.getBean(EsUicPlatformService.class);
	private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
	private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);

    private final String CONTENT_TEMP_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse'>" 
    +"<tr><td rowspan='27' bgcolor='#DDDDDD' width='120'><span>平台数据报表</span><br/><span>dateTime日</span></td><td colspan='10' bgcolor='#DDDDDD'>基础数据</td></tr>" 
    +"<tr style='font-weight:bold' ><td>新增用户 </td><td>活跃用户 </td><td>累计用户 </td><td>新增充值人数 </td><td>累计充值人数 </td><td></td><td></td><td></td><td></td><td></td></tr>" 
    +"<tr><td>newUser</td><td>activeUser</td><td>sumUser</td><td>newRechargeUser</td><td>sumRechargeUser</td><td></td><td></td><td></td><td></td><td></td></tr>" 
    +"<tr><td colspan='10' bgcolor='#DDDDDD'>流水数据</td></tr>" 
    +"<tr style='font-weight:bold'><td>投注流水</td><td>返奖流水</td><td>应用流水差</td><td>返奖率</td><td>投注人数</td><td>投注ARPU</td><td>投注笔数</td><td>人均频次</td><td></td><td></td></tr>" 
    +"<tr><td >cathecticMoney</td><td>winMoney</td><td>moneyGap</td><td>winMoneyRate</td><td>cathecticUserCount</td><td>cathecticARPU</td><td>cathecticNum</td><td>averageNum</td><td></td><td></td></tr>" 
    +"<tr><td colspan='10'  bgcolor='#DDDDDD'>趋势</td></tr>" 
    +"<tr style='font-weight:bold'><td>日期</td><td>日活</td><td>投注用户数</td><td>投注转化率</td><td>充值用户数</td><td>付费渗透率</td><td>新增用户</td><td>新增用户投注人数</td><td>新用户投注转化率</td><td>新增次留</td></tr>"; 

    private final String TABLE_END = "</table>";

    public void execute() {
	logger.info("开始平台数据报表分析");
	byte count = 0;
	// 昨天的开始时间
	String date = DateUtils.getYesterdayDate();
	while (count <= 5) {
	    try {
		// 获取收件人 ------ 收件人要改
		String receivers = dataConfigService.findByName(DataConstants.PLATFORM_DATA_RECEIVER).getValue();
		if (StringUtils.isNotEmpty(receivers)) {
		    StringBuilder content = new StringBuilder();
		    content.append(buildPlatformInfo(date));
		    content.insert(0, date +"数据如下" + "<br/><br/>");
		    // 发送邮件
		    for (String to : receivers.split(",")) {
			try {
			    emailHander.sendHtml(to,String.format("平台数据报表分析汇总(%s)",DateUtils.getDate()),content.toString());
			} catch (MessagingException e) {
			    logger.error("平台数据报表分析发送失败：" + to);
			}
		    }
		} else {
		    logger.error(">>>>>>>>>>>>平台数据报表分析接收人未设置");
		}
		break;
	    } catch (Exception e) {
		count++;
		if (count <= 5) {
		    logger.error(">>>>>>>>>>>>>>平台数据报表分析异常，重新执行 " + count, e);
		} else {
		    logger.error(">>>>>>>>>>>>>>平台数据报表分析异常重新执行失败，停止分析", e);
		}

	    }
	}
    }

    
    //平台数据报表
    private String buildPlatformInfo(String date) {
    	String temp = getTemp(date);
    	temp = temp.replace("gameName", "平台数据报表");
    	temp = temp.replace("dateTime", date);
    	return temp;
    }
    
    //投注信息
    private ReportGameInfo getBettingInfo(String date){
      	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("beginDate",date+" 00:00:00");
    	params.put("endDate", date+" 23:59:59");
    	params.put("userIds", getInternalUserIds());
    	ReportGameInfo bettingInfo = reportChangeNoteService.findCathecticListByDate(params);
    	return bettingInfo;
    }
    
  //投注用户数 
    private Integer getCathecticUserNum(String date){
      	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("beginDate",date+" 00:00:00");
    	params.put("endDate", date+" 23:59:59");
    	params.put("userIds", getInternalUserIds());
    	Integer cathecticUserNum = reportChangeNoteService.getCathecticUserNum(params);
    	return cathecticUserNum;
    }
    
	//查询内部人员的Id
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }
    
    /**
     * 整合
     * @param date
     * @return
     */
    private String getTemp(String date){
    	StringBuffer sb=new StringBuffer();
    	sb.append(getTempOne(date));
    	sb.append(getTempTwo(date));
    	sb.append(TABLE_END);
    	return sb.toString();
    }
    
    
    /**
     * 基础+流水数据
     * @param date
     * @return
     */
    private String getTempOne(String date){
	// 1、新增用户数
    Integer	newUser= platformService.getNewUser(date);
	// 2、活跃用户
	Integer activeUser = platformService.getActiveUser(date);
	// 3、累计用户
	Integer sumUser = platformService.getSumUser();
	// 4、新增充值人数
	Integer newRechargeUser =platformService.getNewRechargeUser(date);
	// 5、累计充值人数
	Integer sumRechargeUser = platformService.getSumRechargeUser();
	// 投注信息
	ReportGameInfo info=getBettingInfo(date);
	if(info==null){
		info=new ReportGameInfo();
	}
	// 6、投注流水
	Long cathecticMoney = info.getCathecticMoney();
	// 7、返奖流水
	Long winMoney=info.getWinMoney();
	// 8、应用流水差
	Long moneyGap=cathecticMoney-winMoney;
	// 9、返奖率
	String winMoneyRate=cathecticMoney==0?"0%": NumberUtils.format(BigDecimalUtil.div(winMoney,cathecticMoney,4),"#.##%");
	// 10、投注人数
	Integer cathecticUserCount=info.getCathecticUserNum();
	// 11、投注ARPU
	String cathecticARPU=cathecticUserCount==0?"0":NumberUtils.format(BigDecimalUtil.div(cathecticMoney,cathecticUserCount,4),"#.#");
	// 12、投注笔数
	Long cathecticNum=info.getCathecticNum();
	// 13、人均频次
	String averageNum=cathecticUserCount==0?"0":NumberUtils.format(BigDecimalUtil.div(cathecticNum,cathecticUserCount,4),"#.#");
	String temp = CONTENT_TEMP_ONE
		.replace("newUser", newUser.toString())
		.replace("activeUser", activeUser.toString())
		.replace("sumUser", sumUser.toString())
		.replace("newRechargeUser", newRechargeUser.toString())
		.replace("sumRechargeUser", sumRechargeUser.toString())
		
		.replace("cathecticMoney", cathecticMoney.toString())
		.replaceFirst("winMoney", winMoney.toString())
		.replace("moneyGap", moneyGap.toString())
		.replace("winMoneyRate", winMoneyRate.toString())
		.replace("cathecticUserCount", cathecticUserCount.toString())
		.replace("cathecticARPU", cathecticARPU.toString())
		.replace("cathecticNum", cathecticNum.toString())
		.replace("averageNum", averageNum.toString());
	return temp;
    }
    
    /**
     * 趋势(前7天~前1天)
     */
    private String getTempTwo(String date) {
    	StringBuffer sb=new StringBuffer();
    	String beginDate= DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),10));
    	String endDate=DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),1));
    	List<String> list=DateUtils.getDateList(beginDate, endDate);
    	String temp = 
    	"<tr><td>date</td><td>activeUser</td><td>bettingUser</td><td>bettingRate</td><td>rechargeUser</td>"
    	+"<td>payRate</td><td>newUser</td><td>newBettingUser</td><td>newBettingRate</td><td>newRemainRate</td></tr>";
    	for(String dat:list){
    		// 1、日期
    		// 2、活跃用户
    		Integer activeUser = platformService.getActiveUser(dat);
    		// 3、投注用户数
    		Integer bettingUser= getCathecticUserNum(dat);
    		// 4、投注转化率
    		String bettingRate=activeUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(bettingUser,activeUser,4),"#.##%");
    		// 7、新增用户
    		Integer	newUser= platformService.getNewUser(dat);
    		// 8、新增用户中的投注人数
    		Integer newBettingUser = platformService.getNewBettingUser(dat);
    		// 9、新增投注转化率
    		String newBettingRate=newUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(newBettingUser,newUser,4),"#.##%");
    		// 10、新增次日留存
    		String newRemainRate=platformService.getRemainRate(dat);
    		// 5、充值用户数
    		Integer rechargeUser=getRechargeUser(dat);
    		// 6、付费渗透率(充值用户数/投注用户数)
    		String payRate=bettingUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(rechargeUser,bettingUser,4),"#.##%");
    		String result=temp
    		.replace("date", dat).replace("activeUser", activeUser.toString()).replace("bettingUser", bettingUser.toString())
    		.replace("bettingRate", bettingRate).replace("newUser", newUser.toString()).replace("newBettingUser", newBettingUser.toString())
    		.replace("newBettingRate", newBettingRate).replace("newRemainRate", newRemainRate)
    		.replace("rechargeUser", rechargeUser.toString()).replace("payRate", payRate);
    		sb.append(result);
    	}
    	return sb.toString();
    }
    
    //趋势=>充值用户数
    private Integer getRechargeUser(String date){
    	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("beginDate", date+" 00:00:00");
    	params.put("endDate", date+" 23:59:59");
    	List<Long> list=transConvertService.getRechargeUserIdsByDay(params);
    	return list.size();
    }
    

}
