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
import com.wf.data.service.*;
import com.wf.data.service.elasticsearch.EsUicChannelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日各渠道关键数据
 *
 * @author jianjian huang
 *         2017年8月23日
 */
public class ChannelDataJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ReportChangeNoteService reportChangeNoteService = SpringContextHolder.getBean(ReportChangeNoteService.class);
    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final EsUicChannelService channelService = SpringContextHolder.getBean(EsUicChannelService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final ReportFishBettingInfoService fishService = SpringContextHolder.getBean(ReportFishBettingInfoService.class);

    private static final String FISH_PREFIX = "report_fish_betting_info_";
    private static final String OCTOBER = "2017-10-01";
    private final String CONTENT_TEMP_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse'>"
            +"<tr style='font-weight:bold'><td>渠道</td><td>充值金额</td><td>日活</td><td>投注用户数</td><td>投注转化率</td><td>充值用户数</td><td>付费渗透率</td><td>投注流水</td><td>返奖流水</td><td>返奖率</td><td>新增人数</td><td>新用户投注转化率</td><td>新用户次留</td></tr>";
    private final String TABLE_END = "</table>";
    private final String TEMPLATE="<tr><td>channel</td><td>rechargeSum</td><td>activeUser</td><td>bettingUser</td><td>bettingRate</td><td>rechargeUser</td><td>payRate</td><td>cathecticMoney</td><td>winMoney</td><td>winMoneyRate</td><td>newUser</td><td>newBettingRate</td><td>newRemainRate</td></tr>";


    public void execute() {
        logger.info("开始每日各渠道关键数据分析");
        byte count = 0;
        // 昨天的开始时间
        String date = DateUtils.getYesterdayDate();
        while (count <= 5) {
            try {
                // 获取收件人 ------ 收件人要改
                String receivers = dataConfigService.findByName(DataConstants.CHANNEL_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(CONTENT_TEMP_ONE);
                    content.append(buildJSData(date));
                    content.append(buildOkoooData(date));
                    content.append(buildIosData(date));
                    content.append(buildGetuiData(date));
                    content.append(buildWeituoData(date));
                    content.append(buildDoyoData(date));
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
        return getTemplate(date,100001L,null).replace("channel","奖多多(100001)");
    }

    private String buildOkoooData(String date){
        return getTemplate(date,100006L,null).replace("channel","澳客(100006)");
    }

    private String buildIosData(String date){
        return getTemplate(date,null,300001L).replace("channel", "ios(300001)");
    }

    private String buildGetuiData(String date){
        return getTemplate(date,null,200001001L).replace("channel", "应用宝(200001001)");
    }

    private String buildWeituoData(String date){
        return getTemplate(date,null,200001021L).replace("channel", "微拓(200001021)");
    }

    private String buildDoyoData(String date){
        return getTemplate(date,100002L,null).replace("channel", "逗游(100002)");
    }

    /**
     * 各渠道数据模板
     * @param date
     * @param parentId
     */
    private String getTemplate(String date,Long parentId,Long channelId) {
        StringBuffer sb=new StringBuffer();
        String temp = TEMPLATE;
        // 1、渠道(稍后计算)
        // 2、充值金额
        Double rechargeSum = transConvertService.findSumRechargeByTime(toMap(date,parentId,channelId));
        // 3、日活
        Integer activeUser = channelService.getActiveUser(date,parentId,channelId);

        ReportGameInfo gameInfo = getBettingInfo(date,parentId,channelId);
        ReportGameInfo fishInfo = getFishBettingInfo(date, parentId, channelId);

        // 4、投注用户数
        List<Long> bettingUsers = getBettingUsers(date, parentId, channelId);
        List<Long> fishBettingUsers = getFishBettingUsers(date, parentId, channelId);
        Integer bettingUser= getCathecticCount(bettingUsers, fishBettingUsers);
        // 5、投注转化率
        String bettingRate = activeUser == 0?"0%":NumberUtils.format(BigDecimalUtil.div(bettingUser,activeUser,4),"#.##%");
        // 6、充值用户数
        Integer rechargeUser=getRechargeUser(date,parentId,channelId);
        // 7、付费渗透率(充值用户数/投注用户数)
        String payRate=bettingUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(rechargeUser,bettingUser,4),"#.##%");
        // 8、投注流水
        Long  cathecticMoney = gameInfo.getCathecticMoney() + fishInfo.getCathecticMoney();
        // 9、返奖流水
        Long winMoney = gameInfo.getWinMoney() + fishInfo.getWinMoney();
        // 10、返奖率
        String winMoneyRate=cathecticMoney == 0?"0%":NumberUtils.format(BigDecimalUtil.div(winMoney,cathecticMoney,4),"#.##%");
        // 11、新增用户
        Integer	newUser= channelService.getNewUser(date,parentId,channelId);
        // 新增用户中的投注人数
        Integer newBettingUser = channelService.getNewBettingUser(date,parentId,channelId);
        // 12、新增投注转化率
        String newBettingRate=newUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(newBettingUser,newUser,4),"#.##%");
        // 13、新增次日留存
        String newRemainRate=channelService.getRemainRate(date,parentId,channelId);
        String result=temp
                .replace("rechargeSum",rechargeSum.toString()).replace("activeUser", activeUser.toString()).replace("bettingUser", bettingUser.toString())
                .replace("bettingRate", bettingRate).replace("rechargeUser", rechargeUser.toString()).replace("payRate", payRate.toString())
                .replace("cathecticMoney", cathecticMoney.toString()).replaceFirst("winMoney", winMoney.toString()).replace("winMoneyRate", winMoneyRate)
                .replace("newUser", newUser.toString()).replace("newBettingRate",newBettingRate.toString()).replace("newRemainRate", newRemainRate.toString());
        sb.append(result);
        return sb.toString();
    }

    //趋势=>充值用户数
    private Integer getRechargeUser(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("beginDate", date+" 00:00:00");
        params.put("endDate", date+" 23:59:59");
        params.put("parentId", parentId);
        params.put("channelId",channelId);
        List<Long> list=transConvertService.getRechargeUserIdsByDay(params);
        return list.size();
    }

    private Map<String,Object> toMap(String date,Long parentId,Long channelId){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("beginDate",date+" 00:00:00");
        map.put("endDate",date+" 23:59:59");
        map.put("parentId",parentId);
        map.put("channelId",channelId);
        map.put("userIds",getInternalUserIds());
        return map;
    }

    //投注信息
    private ReportGameInfo getBettingInfo(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("beginDate",date+" 00:00:00");
        params.put("endDate",date+" 23:59:59");
        params.put("userIds",getInternalUserIds());
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        ReportGameInfo bettingInfo = reportChangeNoteService.findCathecticListByDate(params);
        if(bettingInfo == null) {
            bettingInfo = new ReportGameInfo();
        }
        return bettingInfo;
    }

    //投注信息(捕鱼)
    private ReportGameInfo getFishBettingInfo(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("fishTable", getFishTable(date));
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        params.put("userIds",getInternalUserIds());
        params.put("searchDate",date);
        ReportGameInfo bettingInfo = fishService.findBettingInfoByDate(params);
        if(bettingInfo == null) {
            bettingInfo = new ReportGameInfo();
        }
        return bettingInfo;
    }

    //查询内部人员的Id
    private List<Long> getInternalUserIds(){
        List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
        return userIds;
    }

    /**
     * 捕鱼动态表后缀
     */
    public static String getFishTable(String date){
        if (StringUtils.isEmpty(date)){
            return null;
        }
        String formatDate = date.substring(0,10);
        Date d = DateUtils.parseDate(formatDate);
        if (d.before(DateUtils.parseDate(OCTOBER))) {
            return null;
        }
        return FISH_PREFIX+DateUtils.formatDate(d,"yyyyMM");
    }

    //投注用户
    private List<Long> getBettingUsers(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("beginDate",date+" 00:00:00");
        params.put("endDate",date+" 23:59:59");
        params.put("userIds",getInternalUserIds());
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        List<Long> userIds = reportChangeNoteService.findBettingUsersByDate(params);
        return userIds;
    }

    //投注用户(捕鱼)
    private List<Long> getFishBettingUsers(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("fishTable", getFishTable(date));
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        params.put("userIds",getInternalUserIds());
        params.put("searchDate",date);
        List<Long> userIds = fishService.findBettingUsersByDate(params);
        return userIds;
    }

    private int getCathecticCount(List<Long> sumUserIds,List<Long> fishUserIds) {
        if(CollectionUtils.isEmpty(sumUserIds)&&CollectionUtils.isEmpty(fishUserIds)){
            return 0;
        }
        if(CollectionUtils.isEmpty(sumUserIds)){
            return fishUserIds.size();
        }
        if(CollectionUtils.isEmpty(fishUserIds)){
            return sumUserIds.size();
        }
        int count = 0 ;
        for(Long userId:fishUserIds) {
            if (sumUserIds.contains(userId)) {
                count++;
            }
        }
        return sumUserIds.size()+fishUserIds.size()-count;
    }

}
