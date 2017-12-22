package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.uic.entity.UicUser;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.elasticsearch.EsUicIntroduceService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 金山和澳客推送邮件
 *
 * @author jianjian huang
 *  2017年12月8日
 */
public class DailyIntroduceJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final EsUicIntroduceService introduceService = SpringContextHolder.getBean(EsUicIntroduceService.class);

    private static final String TABLE_BEGIN = "<table border='1' style='text-align:center;border-collapse:collapse'>" +
            "<tr style='background-color:yellow'><td>user_id</td><td>third_id</td><td>是否当天注册</td></tr>";
    private static final String TABLE_END = "</table>";
    private static final Long JINSHAN = 100001L;
    private static final Long OKOOO = 100006L;
    private static final int TIMES = 5;
    private static final String COMMA = ",";


    public void execute() {
        logger.info("开始金山和澳客推送名单分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        String date = DateUtils.getYesterdayDate();
        while (count <= TIMES) {
            try {
                String receivers = dataConfigService.findByName(DataConstants.DAILY_INTRODUCE_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    for (String to : receivers.split(COMMA)) {
                        try {
                            emailHander.sendHtml(to, String.format("金山推送名单(%s)", DateUtils.getDate()), buildLotteryInfo(date));
                            emailHander.sendHtml(to, String.format("澳客推送名单(%s)", DateUtils.getDate()), buildOkoooInfo(date));
                        } catch (MessagingException e) {
                            logger.error("金山澳客推送名单发送失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        }
                    }
                } else {
                    logger.error("金山澳客推送名单未设置收件人，traceId={}", TraceIdUtils.getTraceId());
                }
                logger.info("金山澳客推送名单发送成功:traceId={}", TraceIdUtils.getTraceId());
                break;
            } catch (Exception e) {
                count++;
                if (count <= TIMES) {
                    logger.error("金山澳客推送名单分析异常,重新执行{}，ex={}，traceId={}", count, LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                } else {
                    logger.error("金山澳客推送名单分析异常，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                }
            }
        }
    }

    private String buildLotteryInfo(String date){
        StringBuilder content = new StringBuilder();
        content.insert(0, date + "金山推送如下" + "<br/><br/>");
        String temp = getTemp(date,JINSHAN);
        content.append(temp);
        return content.toString();
    }

    private String buildOkoooInfo(String date){
        StringBuilder content = new StringBuilder();
        content.insert(0, date + "澳客推送如下" + "<br/><br/>");
        String temp = getTemp(date,OKOOO);
        content.append(temp);
        return content.toString();
    }

    private String getTemp(String date,Long channelId){
        StringBuffer sb = new StringBuffer();
        sb.append(TABLE_BEGIN);
        sb.append(getTemplate(date,channelId));
        sb.append(TABLE_END);
        return sb.toString();
    }

    private String getTemplate(String date,Long channelId){
        StringBuffer sb = new StringBuffer();
        Base64 base64 = new Base64();
        // 昨日活跃用户的list
        List<Long> activeUserIds = introduceService.getActiveUserIds(date,channelId);
        if (CollectionUtils.isNotEmpty(activeUserIds)) {
            for (Long userId : activeUserIds) {
                String temp = "<tr><td>userId</td><td>thirdId</td><td>isRegToday</td></tr>";
                UicUser user = introduceService.getByUserId(userId);
                String val = "";
                if (user != null){
                    if(JINSHAN.equals(channelId)){
                        String thirdId = user.getThirdId();
                        Long regChannelId = user.getRegChannelId();
                        if (thirdId != null && regChannelId != null && JINSHAN.equals(regChannelId)){
                            try {
                                val = new String(base64.decode(thirdId), "UTF-8");
                            } catch (Exception e) {
                                logger.error("用户ID:{}解码失败，traceId={}",userId ,TraceIdUtils.getTraceId());
                            }
                        }
                    }
                    else if(OKOOO.equals(channelId)){
                        val = user.getNickname();
                    }
                }
                //是否当天注册
                Long minUserId = introduceService.getMinUserId(date);
                String isRegToday = "否";
                if (userId.longValue() >= minUserId.longValue()){
                    isRegToday = "是";
                }
                 temp = temp.replace("userId", userId.toString())
                        .replace("thirdId", val)
                        .replace("isRegToday", isRegToday);
                sb.append(temp);
            }
        }
        return sb.toString();
    }

}
