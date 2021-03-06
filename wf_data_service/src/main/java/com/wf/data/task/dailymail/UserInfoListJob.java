package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.EmailContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.uic.entity.UicUser;
import com.wf.data.dto.UicDto;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.elasticsearch.EsTransCommonService;
import com.wf.data.service.elasticsearch.EsUicCommonService;
import com.wf.data.service.elasticsearch.EsUicPlatformService;
import com.wf.email.mq.SendEmailDto;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class UserInfoListJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EsUicPlatformService esUicPlatformService = SpringContextHolder.getBean(EsUicPlatformService.class);
    private final EsTransCommonService esTransCommonService = SpringContextHolder.getBean(EsTransCommonService.class);
    private final EsUicCommonService esUicCommonService = SpringContextHolder.getBean(EsUicCommonService.class);
    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final RabbitTemplate rabbitTemplate = SpringContextHolder.getBean(RabbitTemplate.class);

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
        sendEmail(getNewUsersByDay(channelId), String.format("澳客前1日新注册用户(%s)", DateUtils.getDate()),1);
        sendEmail(getBettingUsers(channelId), String.format("澳客近7日内投注用户(%s)", DateUtils.getDate()),1);
        sendEmail(getUnActiveUserIds(channelId), String.format("澳客近7日未活跃用户(%s)", DateUtils.getDate()),1);
        sendEmail(getUnActiveByDays(channelId), String.format("澳客近14天未活跃(%s)", DateUtils.getDate()),1);
        sendEmail(getActive(channelId), String.format("澳客近30天活跃(%s)", DateUtils.getDate()),1);
        sendEmail(getUnRechargeUserIds(channelId), String.format("澳客近30天活跃且未充值用户(%s)", DateUtils.getDate()),1);
        sendEmail(getRechargeList(channelId), String.format("澳客全量充值用户(%s)", DateUtils.getDate()),1);
    }

    private void buildJddUserInfo(Long channelId) {
        sendEmail(getNewUsersByDay(channelId), String.format("金山前1日新注册用户(%s)", DateUtils.getDate()),2);
        sendEmail(getBettingUsers(channelId), String.format("金山近7日内投注用户(%s)", DateUtils.getDate()),2);
        sendEmail(getUnActiveUserIds(channelId), String.format("金山近7日未活跃用户(%s)", DateUtils.getDate()),2);
        sendEmail(getUnActiveByDays(channelId), String.format("金山近14天未活跃(%s)", DateUtils.getDate()),2);
        sendEmail(getUnRechargeUserIds(channelId), String.format("金山近30天活跃且未充值用户(%s)", DateUtils.getDate()),2);

    }

    private String getRechargeList(Long channelId) {
        String userTemplate = "<table border='1'>";
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        try {
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
                    logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("获取充值用户: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }

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
        try {
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
                    logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("近30天活跃: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }
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
        try {
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
                        logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                    }
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("近30天活跃且未充值用户: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }
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
        try {

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
                        logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                    }
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("查询14天未活跃用户: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }
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
        try {

            // 第8天日活
            logger.info("查询第8日的活跃用户条件: traceId={}, jsonObject={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(note));
            List<Long> users = esUicCommonService.getDauList(note);
            note.setBeginDate(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getPrevDate(DateUtils.parseDate(DateUtils.getYesterdayDate()), 6)), DateUtils.DATE_TIME_PATTERN));
            note.setEndDate(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(DateUtils.getYesterdayDate())), DateUtils.DATE_TIME_PATTERN));
            // 从昨天到第7天的日活
            logger.info("从昨天到第7天的日活条件: traceId={}, jsonObject={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(note));
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
                        logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                    }
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("查询7天未活跃用户: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }
    }

    /**
     * 前1日新注册用户
     *
     * @param channelId
     * @return
     */
    private String getNewUsersByDay(Long channelId) {
        try {
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
                    logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                }
            }
            userTemplate += "</table>";
            return userTemplate;

        } catch (Exception e) {
            logger.error("获取昨天渠道的新用户失败: traceId={}, channelId={},ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(channelId), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }

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
        try {

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
                    logger.error("彩票ID解码失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
                }
            }
            userTemplate += "</table>";
            return userTemplate;
        } catch (Exception e) {
            logger.error("获取近7日内投注用户失败: traceId={}, params={},ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(params), LogExceptionStackTrace.erroStackTrace(e));
            throw e;
        }
    }

    private void sendEmail(String template, String title, int channelType) {
        try {
            SendEmailDto sendEmailDto = new SendEmailDto();
            sendEmailDto.setTitle(title);
            sendEmailDto.setContent(template);
            sendEmailDto.setType("html");
            if (channelType == 1){
                sendEmailDto.setAlias(EmailContents.AK_USER_DATA_ALIAS);
            }else if (channelType == 2){
                sendEmailDto.setAlias(EmailContents.JS_USER_DATA_ALIAS);
            }
            rabbitTemplate.convertAndSend(EmailContents.EMAIL_RABBITMQ_NAME, sendEmailDto);
        } catch (Exception e) {
            logger.error("用户信息列表分析异常重新执行失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }
}
