package com.wf.data.task.docking;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.docking.SendThirdIdToJddService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时推送奖多多彩票ID
 * 1、游戏平台的全量用户彩票ID
 * 2、昨日新增用户彩票ID
 *
 * @author chengsheng.liu
 * @date 2018年3月5日
 */
public class SendThirdIdToJddJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SendThirdIdToJddService sendThirdIdToJddService = SpringContextHolder.getBean(SendThirdIdToJddService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        String openStr = dataConfigService.getStringValueByName(DataConstants.SEND_THIRDID_OPEN);
        String[] openFlag = openStr.split(",");

        logger.info("推送奖多多彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[0])) {
                sendThirdIdToJddService.toDoAnalysis();
            }
        } catch (Exception e) {
            logger.error("sendThirdId发送失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("推送奖多多彩票ID结束。。。。。。。。");



        logger.info("推送奖多多回流用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[1])) {
                sendThirdIdToJddService.pushBackFlowUser();
            }
        } catch (Exception e) {
            logger.error("pushBackFlowUser发送失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("推送奖多多回流用户彩票ID结束。。。。。。。。");




        logger.info("推送奖多多未付费老用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[2])) {
                sendThirdIdToJddService.pushUnpayUser();
            }
        } catch (Exception e) {
            logger.error("pushUnpayUser发送失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("推送奖多多未付费老用户彩票ID结束。。。。。。。。");




        logger.info("奖多多渠道付费用户，7日内没有付费行为的用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[3])) {
                sendThirdIdToJddService.pushPayedUser();
            }
        } catch (Exception e) {
            logger.error("pushUnpayUser发送失败: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("奖多多渠道付费用户，7日内没有付费行为的用户彩票ID结束。。。。。。。。");



        //新注册用户7天有活跃且未付费
        logger.info("新注册用户7天有活跃且未付费用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[4])) {
                sendThirdIdToJddService.pushNewAndUnpayUser();
            }
        } catch (Exception e) {
            logger.error("pushNewAndUnpayUser: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("新注册用户7天有活跃且未付费用户彩票ID结束。。。。。。。。");


        //奖多多渠道7天未活跃用户
        logger.info("奖多多渠道7天未活跃用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[5])) {
                sendThirdIdToJddService.pushUnActiveLastSevenDay();
            }
        } catch (Exception e) {
            logger.error("pushNewUser: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("奖多多渠道7天未活跃用户彩票ID结束。。。。。。。。");

        //10天活跃投注用户
        logger.info("奖多多渠道10天活跃投注用户彩票ID开始。。。。。。。。");
        try {
            if ("true".equals(openFlag[6])) {
                sendThirdIdToJddService.pushActiveAndBettingLastTenDay();
            }
        } catch (Exception e) {
            logger.error("pushActiveAndBettingLastTenDay: traceId={},date={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(DateUtils.getYesterdayDate()), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("奖多多渠道10天活跃投注用户彩票ID结束。。。。。。。。");

    }

}
