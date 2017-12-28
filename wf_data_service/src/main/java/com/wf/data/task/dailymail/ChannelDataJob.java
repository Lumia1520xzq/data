package com.wf.data.task.dailymail;

import com.google.common.collect.Lists;
import com.wf.core.cache.CacheHander;
import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.ReportGameInfo;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.*;
import com.wf.data.service.elasticsearch.EsTcardChannelService;
import com.wf.data.service.elasticsearch.EsUicChannelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.*;

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
    private final EsTcardChannelService tcardService =  SpringContextHolder.getBean(EsTcardChannelService.class);
    private final ChannelInfoService channelInfoService = SpringContextHolder.getBean(ChannelInfoService.class);
    private final CacheHander cacheHander = SpringContextHolder.getBean(CacheHander.class);

    private static final String FISH_PREFIX = "report_fish_betting_info_";
    private static final String OCTOBER = "2017-10-01";


    private static final Integer TIMES = 5;
    private static final String COMMA = ",";

    public void execute() {
        logger.info("开始每日各渠道关键数据分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        // 昨天的开始时间
        String date = DateUtils.getYesterdayDate();
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
        return getTemplate(date,parentId,channelId,getGroupList());
    }

    /**
     * 去除特定用户(灰黑内)走缓存
     */
    private List<Long> getGroupList() {
        List<Long> uicGroupList = Lists.newArrayList();
        String value = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(value)) {
            String[] uicGroupArr = value.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = cacheHander.cache(DataCacheKey.DATA_DATAWARE_UIC_GROUP.key(), () -> uicGroupService.findGroupUsers(userGroup));
        }
        return uicGroupList;
    }

    /**
     * 各渠道数据模板
     */
    private String getTemplate(String date,Long parentId,Long channelId,List<Long> userIds) {
        String template = "<tr><td>channelName</td><td>channelId</td><td>rechargeSum</td><td>activeUser</td><td>bettingUser</td><td>rechargeUser</td><td>newUser</td><td>cathecticMoney</td><td>bettingRate</td><td>payRate</td><td>newBettingRate</td><td>newRemainRate</td><td>winMoney</td><td>winMoneyRate</td></tr>";
        StringBuilder sb = new StringBuilder();
        // 2、充值金额
        Double rechargeSum = transConvertService.findSumRechargeByTime(toMap(date,parentId,channelId));
        // 3、日活(在代码中去重)
        List<Long> activeUserList = channelService.getActiveUserList(date,parentId,channelId);
        Integer activeUser = CollectionUtils.disjunction(activeUserList,CollectionUtils.intersection(activeUserList,userIds)).size();
        ReportGameInfo gameInfo = getBettingInfo(date,parentId,channelId);
        ReportGameInfo fishInfo = getFishBettingInfo(date, parentId, channelId);
        // 4、投注用户数(其他游戏+捕鱼+三张)
        List<Long> bettingUsers = getBettingUsers(date, parentId, channelId);
        List<Long> fishBettingUsers = getFishBettingUsers(date, parentId, channelId);
        List<Long> tcardBettingUsers = tcardService.getTcardBettingUsers(date,parentId,channelId);
        tcardBettingUsers = (List<Long>)CollectionUtils.disjunction(tcardBettingUsers,CollectionUtils.intersection(tcardBettingUsers,userIds));

        List<Long> sumBettingUsers = getBettingUserList(bettingUsers,fishBettingUsers,tcardBettingUsers);
        Integer bettingUser = sumBettingUsers.size();
        // 5、投注转化率
        String bettingRate = activeUser == 0?"0%":NumberUtils.format(BigDecimalUtil.div(bettingUser,activeUser,4),"#.##%");
        // 6、充值用户数
        Integer rechargeUser = getRechargeUser(date,parentId,channelId);
        // 7、付费渗透率(充值用户数/投注用户数)
        String payRate=bettingUser==0?"0%":NumberUtils.format(BigDecimalUtil.div(rechargeUser,bettingUser,4),"#.##%");
        // 8、投注流水(其他游戏+捕鱼+三张)
        Long  cathecticMoney = gameInfo.getCathecticMoney() + fishInfo.getCathecticMoney() + getTcardBetting(date,parentId,channelId,userIds);
        // 9、返奖流水(其他游戏+捕鱼+三张)
        Long winMoney = gameInfo.getWinMoney() + fishInfo.getWinMoney() + getTcardAward(date,parentId,channelId,userIds);
        // 10、返奖率
        String winMoneyRate=cathecticMoney == 0?"0%":NumberUtils.format(BigDecimalUtil.div(winMoney,cathecticMoney,4),"#.##%");
        // 11、新增用户
        List<Long> newUserIds = channelService.getNewUserIds(date,parentId,channelId);
        newUserIds = (List<Long>)CollectionUtils.disjunction(newUserIds,CollectionUtils.intersection(newUserIds,userIds));
        Integer	newUser= newUserIds.size();
        // 新增用户中的投注人数
        Integer newBettingUser = CollectionUtils.intersection(newUserIds,sumBettingUsers).size();
        // 12、新增投注转化率
        String newBettingRate = newUser == 0?"0%":NumberUtils.format(BigDecimalUtil.div(newBettingUser,newUser,4),"#.##%");
        // 13、新增次日留存
        String yesDate = DateUtils.formatDate(DateUtils.getPrevDate(DateUtils.parseDate(date),1));
        String newRemainRate = channelService.getRemainRate(yesDate,parentId,channelId,userIds);
        String result = template
                .replace("rechargeSum",rechargeSum.toString()).replace("activeUser", activeUser.toString()).replace("bettingUser", bettingUser.toString())
                .replace("bettingRate", bettingRate).replace("rechargeUser", rechargeUser.toString()).replace("payRate", payRate)
                .replace("cathecticMoney", cathecticMoney.toString()).replaceFirst("winMoney", winMoney.toString()).replace("winMoneyRate", winMoneyRate)
                .replace("newUser", newUser.toString()).replace("newBettingRate",newBettingRate).replace("newRemainRate", newRemainRate);
        sb.append(result);
        return sb.toString();
    }

    /**
     * 去除灰黑内(三张投注流水)
     */
    private Long getTcardBetting(String date,Long parentId,Long channelId,List<Long> userIds) {
        List<TransChangeNote> list = tcardService.getBettingDetails(date,parentId,channelId);
        double betting = 0L;
        if(CollectionUtils.isNotEmpty(list)) {
            for(TransChangeNote transChangeNote:list){
                if(!userIds.contains(transChangeNote.getUserId())){
                betting += transChangeNote.getChangeMoney();
                }
            }
        }
        return (long)betting;
    }



    /**
     * 去除灰黑内(三张返奖流水)
     */
    private Long getTcardAward(String date,Long parentId,Long channelId,List<Long> userIds) {
        List<TransChangeNote> list = tcardService.getAwardDetails(date,parentId,channelId);
        double betting = 0L;
        if(CollectionUtils.isNotEmpty(list)) {
            for(TransChangeNote transChangeNote:list){
                if(!userIds.contains(transChangeNote.getUserId())){
                    betting += transChangeNote.getChangeMoney();
                }
            }
        }
        return (long)betting;
    }

    /**
     * 充值用户数
     */
    private Integer getRechargeUser(String date,Long parentId,Long channelId) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("beginDate", date+" 00:00:00");
        params.put("endDate", date+" 23:59:59");
        params.put("parentId", parentId);
        params.put("channelId",channelId);
        List<Long> list = transConvertService.getRechargeUserIdsByDay(params);
        return list.size();
    }

    private Map<String,Object> toMap(String date,Long parentId,Long channelId) {
        Map<String,Object> map = new HashMap<>(5);
        map.put("beginDate",date+" 00:00:00");
        map.put("endDate",date+" 23:59:59");
        map.put("parentId",parentId);
        map.put("channelId",channelId);
        map.put("userIds",getGroupList());
        return map;
    }

    /**
     * 投注信息(其他游戏)
     */
    private ReportGameInfo getBettingInfo(String date,Long parentId,Long channelId) {
        Map<String,Object> params=new HashMap<>(5);
        params.put("beginDate",date+" 00:00:00");
        params.put("endDate",date+" 23:59:59");
        params.put("userIds",getGroupList());
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        ReportGameInfo bettingInfo = reportChangeNoteService.findCathecticListByDate(params);
        if(bettingInfo == null) {
            bettingInfo = new ReportGameInfo();
        }
        return bettingInfo;
    }

    /**
     * 投注信息(捕鱼)
     */
    private ReportGameInfo getFishBettingInfo(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<>(5);
        params.put("fishTable", getFishTable(date));
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        params.put("userIds",getGroupList());
        params.put("searchDate",date);
        ReportGameInfo bettingInfo = fishService.findBettingInfoByDate(params);
        if(bettingInfo == null) {
            bettingInfo = new ReportGameInfo();
        }
        return bettingInfo;
    }

    /**
     * 捕鱼动态表后缀
     */
    private static String getFishTable(String date) {
        if (StringUtils.isEmpty(date)){
            return null;
        }
        String formatDate = date.substring(0,10);
        Date d = DateUtils.parseDate(formatDate);
        if (d.before(DateUtils.parseDate(OCTOBER))) {
            return null;
        }
        return FISH_PREFIX + DateUtils.formatDate(d,"yyyyMM");
    }

    /**
     * 投注用户
     */
    private List<Long> getBettingUsers(String date,Long parentId,Long channelId){
        Map<String,Object> params=new HashMap<>(5);
        params.put("beginDate",date+" 00:00:00");
        params.put("endDate",date+" 23:59:59");
        params.put("userIds",getGroupList());
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        return reportChangeNoteService.findBettingUsersByDate(params);
    }

    /**
     * 投注用户(捕鱼)
     */
    private List<Long> getFishBettingUsers(String date,Long parentId,Long channelId) {
        Map<String,Object> params=new HashMap<>(5);
        params.put("fishTable", getFishTable(date));
        params.put("parentId",parentId);
        params.put("channelId",channelId);
        params.put("userIds",getGroupList());
        params.put("searchDate",date);
        return fishService.findBettingUsersByDate(params);
    }


    private List<Long> getBettingUserList(List<Long> sumUserIds,List<Long> fishUserIds,List<Long> tcardUserIds) {
        if(CollectionUtils.isEmpty(sumUserIds)){
            sumUserIds = new ArrayList<>();
        }
        if(CollectionUtils.isEmpty(fishUserIds)){
            fishUserIds = new ArrayList<>();
        }
        if(CollectionUtils.isEmpty(tcardUserIds)){
            tcardUserIds = new ArrayList<>();
        }
        return (List<Long>)CollectionUtils.union(CollectionUtils.union(sumUserIds,fishUserIds),tcardUserIds);
    }

}
