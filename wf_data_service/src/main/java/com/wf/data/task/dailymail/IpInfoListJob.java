package com.wf.data.task.dailymail;

import com.wf.base.rpc.ConfigRpcService;
import com.wf.core.email.EmailHander;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.uic.entity.UicUserLog;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.UicUserLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

/**
 * 查询相同IP，相同IP登录超过50人的userId
 *
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class IpInfoListJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final UicUserLogService uicUserLogService = SpringContextHolder.getBean(UicUserLogService.class);

    public void execute() {
        logger.info("根据ip分析用户行为开始。。。。。。。。");
        int ipCount = dataConfigService.getIntValueByName(DataConstants.MONITOR_IP_COUNT);
        if (ipCount == 0) {
            logger.error(">>>>>>>>>>>>根据ip分析用户行为iPCount未设置");
            return;
        }
        Date yesterday = DateUtils.getNextDate(new Date(), -1);
        String beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(yesterday), DateUtils.DATE_TIME_PATTERN);
        String endDate = DateUtils.formatDate(DateUtils.getDayEndTime(yesterday), DateUtils.DATE_TIME_PATTERN);
        UicUserLog log = new UicUserLog();
        log.setBeginDate(beginDate);
        log.setEndDate(endDate);
        log.setIpCount(ipCount);
        List<String> ips = uicUserLogService.findIpByIpCount(log);
        String userTemplate = "<table border='1'>";
        if (ips.size() <= 0) {
            logger.info("同ip数量大于{},为空", ipCount);
            userTemplate += "<tr>同ip数量大于" + ipCount + "的数据为空</tr>";
            userTemplate += "</table>";
            sendEmail(userTemplate, String.format("根据ip分析用户信息(%s)", DateUtils.getYesterdayDate()));
            return;
        }
        log.setIps(ips);
        List<UicUserLog> logs = uicUserLogService.findUserLogByIp(log);


        userTemplate += "<tr><td> 用户IP</td><td>用户ID</td></tr>";
        for (UicUserLog userLog : logs) {
            userTemplate += "<tr><td>" + userLog.getIp() + "</td><td>" + userLog.getUserId() + "</td></tr>";
        }
        userTemplate += "</table>";

        sendEmail(userTemplate, String.format("根据ip分析用户信息(%s)", DateUtils.getYesterdayDate()));

        logger.info("根据ip分析用户行为结束。。。。。。。。");
    }

    private void sendEmail(String template, String title) {
        try {
            // 获取收件人
            ConfigRpcService configRpcService = SpringContextHolder.getBean(ConfigRpcService.class);
            EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
            String receivers = configRpcService.findByName(DataConstants.MONITOR_IP_RECEIVER).getValue();
            if (StringUtils.isNotEmpty(receivers)) {
                // 发送邮件
                for (String to : receivers.split(",")) {
                    try {

                        emailHander.sendHtml(to, title, template);
                    } catch (MessagingException e) {
                        logger.error("根据ip分析用户行为发送失败：" + to);
                    }
                }
            } else {
                logger.error(">>>>>>>>>>>>根据ip分析用户行为接收人未设置");
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>>>根据ip分析用户行为异常", e);
        }
    }
}
