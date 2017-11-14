package com.wf.data.task.dailymail;

import com.wf.base.rpc.ConfigRpcService;
import com.wf.core.email.EmailHander;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.entity.dto.UicDto;
import com.wf.data.dao.entity.mycat.UicUser;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.elasticsearch.EsTransCommonService;
import com.wf.data.service.elasticsearch.EsUicCommonService;
import com.wf.data.service.elasticsearch.EsUicPlatformService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Component
public class UserInfoListJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EsUicPlatformService esUicPlatformService;
    @Autowired
    private EsTransCommonService esTransCommonService;
    @Autowired
    private EsUicCommonService esUicCommonService;
    @Autowired
    private TransConvertService transConvertService;

    public void execute() {
        logger.info("用户信息列表分析开始。。。。。。。。");

        List<Long> channelIdList = Arrays.asList(100001l, 100006l);
        for (Long channelId : channelIdList) {
            if (channelId == 100001) {
                buildJddUserInfo(channelId);
            } else if (channelId == 100006) {
                buildOkoooUserInfo(channelId);
            }
        }
        logger.info("用户信息列表分析结束。。。。。。。。");
    }

    private void buildOkoooUserInfo(Long channelId) {
        sendEmail(getNewUsersByDay(channelId), String.format("澳客前1日新注册用户(%s)", DateUtils.getDate()));
        sendEmail(getBettingUsers(channelId), String.format("澳客近7日内投注用户(%s)", DateUtils.getDate()));
        sendEmail(getUnActiveUserIds(channelId), String.format("澳客近7日未活跃用户(%s)", DateUtils.getDate()));
        sendEmail(getUnActiveByDays(channelId), String.format("澳客近14天未活跃(%s)", DateUtils.getDate()));
        sendEmail(getActive(channelId), String.format("澳客近30天活跃(%s)", DateUtils.getDate()));
        sendEmail(getUnRechargeUserIds(channelId), String.format("澳客近30天活跃且未充值用户(%s)", DateUtils.getDate()));
        sendEmail(getRechargeList(channelId), String.format("澳客全量充值用户(%s)", DateUtils.getDate()));
    }

    private void buildJddUserInfo(Long channelId) {
        sendEmail(getNewUsersByDay(channelId), String.format("金山前1日新注册用户(%s)", DateUtils.getDate()));
        sendEmail(getBettingUsers(channelId), String.format("金山近7日内投注用户(%s)", DateUtils.getDate()));
        sendEmail(getUnActiveUserIds(channelId), String.format("金山近7日未活跃用户(%s)", DateUtils.getDate()));
        sendEmail(getUnActiveByDays(channelId), String.format("金山近14天未活跃(%s)", DateUtils.getDate()));
        sendEmail(getUnRechargeUserIds(channelId), String.format("金山近30天活跃且未充值用户(%s)", DateUtils.getDate()));

    }

    private String getRechargeList(Long channelId) {
        String userTemplate = "<table border='1'>";
        Map<String,Object> map = new HashMap<>();
        map.put("channelId",channelId);
        List<Long> logList = transConvertService.getRechargeUserIdsByDay(map);
        for (Long userId : logList) {
            UicUser user = esUicPlatformService.get(userId);
            if (user == null) continue;
            if (!channelId.equals(user.getRegChannelId())) continue;
            String thirdId = user.getThirdId();
            try {
                if (channelId == 100001) {
                    if (StringUtils.isNotEmpty(thirdId))
                        thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                    userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                } else if (channelId == 100006) {
                    userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                }
            } catch (Exception e) {
                logger.error("彩票ID解码失败 ", e);
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},充值列表分析结束", channelId);
        return userTemplate;
    }

    /**
     * 近30天活跃
     *
     * @param channelId
     * @return
     */
    private String getActive(Long channelId) {
        UicDto note = new UicDto();
        String userTemplate = "<table border='1'>";

        note.setChannelId(channelId);
        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 29)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
        List<Long> usersList = esUicCommonService.getDauList(note);
        for (Long userId : usersList) {
            UicUser user = esUicPlatformService.get(userId);
            if (user == null) continue;
            if (!channelId.equals(user.getRegChannelId())) continue;
            String thirdId = user.getThirdId();
            try {
                if (channelId == 100001) {
                    if (StringUtils.isNotEmpty(thirdId))
                        thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                    userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                } else if (channelId == 100006) {
                    userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                }
            } catch (Exception e) {
                logger.error("彩票ID解码失败 ", e);
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},近30天活跃分析结束", channelId);
        return userTemplate;
    }

    /**
     * 近30天活跃且未充值用户
     *
     * @param channelId
     * @return
     */
    private String getUnRechargeUserIds(Long channelId) {
        UicDto note = new UicDto();
        String userTemplate = "<table border='1'>";

        note.setChannelId(channelId);
        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 29)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
        List<Long> usersList = esUicCommonService.getDauList(note);
        for (Long userId : usersList) {

            double sumRecharge = transConvertService.findUserSumRecharge(userId);
            if (sumRecharge <= 0) {
                UicUser user = esUicPlatformService.get(userId);
                if (user == null) continue;
                if (!channelId.equals(user.getRegChannelId())) continue;
                String thirdId = user.getThirdId();
                try {
                    if (channelId == 100001) {
                        if (StringUtils.isNotEmpty(thirdId))
                            thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                        userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                    } else if (channelId == 100006) {
                        userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                    }
                } catch (Exception e) {
                    logger.error("彩票ID解码失败 ", e);
                }
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},近30天活跃且未充值用户分析结束", channelId);
        return userTemplate;
    }

    /**
     * 查询14天未活跃用户
     *
     * @param channelId
     * @return
     */
    private String getUnActiveByDays(Long channelId) {
        UicDto note = new UicDto();
        String userTemplate = "<table border='1'>";

        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 14)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 14)), DateUtils.DATE_TIME_PATTERN));
        note.setChannelId(channelId);
        // 第15天日活
        List<Long> users = esUicCommonService.getDauList(note);
        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 13)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
        // 从昨天到第14天的日活
        List<Long> usersList = esUicCommonService.getDauList(note);
        for (Long userId : users) {
            if (!usersList.contains(userId)) {
                UicUser user = esUicPlatformService.get(userId);
                if (user == null) continue;
                if (!channelId.equals(user.getRegChannelId())) continue;
                String thirdId = user.getThirdId();
                try {
                    if (channelId == 100001) {
                        if (StringUtils.isNotEmpty(thirdId))
                            thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                        userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                    } else if (channelId == 100006) {
                        userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                    }
                } catch (Exception e) {
                    logger.error("彩票ID解码失败 ", e);
                }
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},查询14天未活跃用户分析结束", channelId);
        return userTemplate;
    }

    /**
     * 查询7天未活跃用户
     *
     * @param channelId
     * @return
     */
    private String getUnActiveUserIds(Long channelId) {
        UicDto note = new UicDto();
        String userTemplate = "<table border='1'>";

        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 7)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 7)), DateUtils.DATE_TIME_PATTERN));
        note.setChannelId(channelId);
        // 第8天日活
        List<Long> users = esUicCommonService.getDauList(note);
        note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 6)), DateUtils.DATE_TIME_PATTERN));
        note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
        // 从昨天到第7天的日活
        List<Long> usersList = esUicCommonService.getDauList(note);
        for (Long userId : users) {
            if (!usersList.contains(userId)) {
                UicUser user = esUicPlatformService.get(userId);
                if (user == null) continue;
                if (!channelId.equals(user.getRegChannelId())) continue;
                String thirdId = user.getThirdId();
                try {
                    if (channelId == 100001) {
                        if (StringUtils.isNotEmpty(thirdId))
                            thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                        userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                    } else if (channelId == 100006) {
                        userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                    }
                } catch (Exception e) {
                    logger.error("彩票ID解码失败 ", e);
                }
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},查询7天未活跃用户分析结束", channelId);
        return userTemplate;
    }

    /**
     * 前1日新注册用户
     *
     * @param channelId
     * @return
     */
    private String getNewUsersByDay(Long channelId) {
        List<UicUser> userList = esUicPlatformService.getNewUserInfoList(DateUtils.getYesterdayDate(), channelId);

        String userTemplate = "<table border='1'>";

        for (UicUser uicUser : userList) {
            String thirdId = uicUser.getThirdId();
            try {
                if (channelId == 100001) {
                    if (StringUtils.isNotEmpty(thirdId))
                        thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                    userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                } else if (channelId == 100006) {
                    userTemplate += "<tr><td>" + thirdId + "</td><td>" + uicUser.getNickname() + "</td></tr>";
                }
            } catch (Exception e) {
                logger.error("彩票ID解码失败 ", e);
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},前1日新注册用户分析结束", channelId);
        return userTemplate;
    }

    /**
     * 近7日内投注用户
     *
     * @param channelId
     * @return
     */
    private String getBettingUsers(Long channelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("beginTime", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 6)), DateUtils.DATE_TIME_PATTERN));
        params.put("endTime", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
        List<Long> userList = esTransCommonService.getBettingUserList(params);
        String userTemplate = "<table border='1'>";
        for (Long userId : userList) {
            UicUser user = esUicPlatformService.get(userId);
            if (user == null) continue;
            if (!channelId.equals(user.getRegChannelId())) continue;
            String thirdId = user.getThirdId();
            try {
                if (channelId == 100001) {
                    if (StringUtils.isNotEmpty(thirdId))
                        thirdId = new String(Base64.decodeBase64(thirdId.getBytes("UTF-8")));
                    userTemplate += "<tr><td>" + thirdId + "</td></tr>";
                } else if (channelId == 100006) {
                    userTemplate += "<tr><td>" + thirdId + "</td><td>" + user.getNickname() + "</td></tr>";
                }
            } catch (Exception e) {
                logger.error("彩票ID解码失败 ", e);
            }
        }
        userTemplate += "</table>";
        logger.debug("渠道：{},前1日新注册用户分析结束", channelId);
        return userTemplate;
    }

    private void sendEmail(String template, String title) {
        ConfigRpcService configRpcService = SpringContextHolder.getBean(ConfigRpcService.class);
        EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
        try {
            // 获取收件人
            String receivers = configRpcService.findByName(DataConstants.USERINFO_DATA_RECEIVER).getValue();
            if (StringUtils.isNotEmpty(receivers)) {
                // 发送邮件
                for (String to : receivers.split(",")) {
                    try {

                        emailHander.sendHtml(to, title, template);
                    } catch (MessagingException e) {
                        logger.error("用户信息列表分析发送失败：" + to);
                    }
                }
            } else {
                logger.error(">>>>>>>>>>>>用户信息列表分析接收人未设置");
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>>>用户信息列表分析异常重新执行失败", e);
        }
    }
}
