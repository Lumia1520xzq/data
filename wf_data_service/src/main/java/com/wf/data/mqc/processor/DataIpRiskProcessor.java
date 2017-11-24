package com.wf.data.mqc.processor;

import com.wf.core.event.BettingTaskEvent;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.DateUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.dao.uic.entity.UicGroup;
import com.wf.data.dao.uic.entity.UicUserLog;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.UicUserLogService;
import com.wf.uic.rpc.UserGroupRpcService;
import com.wf.uic.rpc.dto.UicGroupDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DataIpRiskProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DataConfigService dataConfigService;
    @Resource
    private UicUserLogService uicUserLogService;
    @Resource
    private UicGroupService uicGroupService;
    @Resource
    private TransConvertService transConvertService;
    @Resource
    private UserGroupRpcService userGroupRpcService;

    public void process(final BettingTaskEvent event) {
        Long userId = event.getUserId();
        if (userId == null) {
            return;
        }

        List<Long> userGroup = Arrays.asList(UserGroupContents.GRAY_LIST_GROUP);
        List<UicGroup> grayUicGroupList = uicGroupService.getInGroupByUserId(userId, userGroup);
        if (null != grayUicGroupList || grayUicGroupList.size() > 0) {
            logger.info("用户已在灰名单: traceId={}, userId={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(userId));
            return;
        }

        int ipCount = dataConfigService.getIntValueByName(DataConstants.MONITOR_IP_COUNT);
        if (ipCount == 0) {
            logger.error("根据ip分析用户行为iPCount未设置: traceId={}, ipCount={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(ipCount));
            return;
        }
        int day = dataConfigService.getIntValueByName(DataConstants.MONITOR_RISK_IP_DAY);
        if (day == 0) {
            logger.error("根据ip分析用户行为day未设置: traceId={}, day={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(day));
            return;
        }
        double rechargeBase = dataConfigService.getDoubleValueByName(DataConstants.MONITOR_RISK_USER_RECHARGE);
        if (rechargeBase == 0) {
            logger.error("根据ip分析用户行为day未设置: traceId={}, rechargeBase={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(rechargeBase));
            return;
        }

        // 获取公司ip
        String wfIps = dataConfigService.findByName(DataConstants.MONITOR_RISK_WF_IPS).getValue();
        String[] wfIpsArr = wfIps.split(",");
        List<String> wfIpsList = Arrays.asList(wfIpsArr);


        String userIp = uicUserLogService.getLastIpByUserId(userId);
        if (wfIpsList.contains(userIp)) {
            logger.info("公司ip不风控: traceId={},userId={}, ip={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(userId), GfJsonUtil.toJSONString(userIp));
            return;
        }

        double recharge = transConvertService.findUserSumRecharge(userId);

        if (recharge >= rechargeBase) {
            logger.info("充值金额={}不风控: traceId={},userId={}", GfJsonUtil.toJSONString(recharge), TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(userId));
            return;
        }

        Date currentTime = new Date();
        String beginDate = DateUtils.formatDate(DateUtils.getNextDate(currentTime, -day), DateUtils.DATE_TIME_PATTERN);
        String endDate = DateUtils.formatDate(currentTime, DateUtils.DATE_TIME_PATTERN);
        UicUserLog log = new UicUserLog();
        log.setBeginDate(beginDate);
        log.setEndDate(endDate);
        log.setIp(userIp);
        Integer userCount = uicUserLogService.getUserCount(log);

        if (userCount >= ipCount) {
            UicGroupDto uicGroup = new UicGroupDto();
            uicGroup.setUserId(userId);
            uicGroup.setGroupTypeId(UserGroupContents.GRAY_LIST_GROUP);
            uicGroup.setGroupTypeParentId(UserGroupContents.BLACK_WHITE_LIST_GROUP);
            uicGroup.setUserData("ip风控,ip数:" + userCount + ",充值:" + recharge + "元");
            logger.info("ip:{}下的用户数:{},充值:{}: userId={},traceId={}", GfJsonUtil.toJSONString(userIp), GfJsonUtil.toJSONString(userCount), GfJsonUtil.toJSONString(recharge), GfJsonUtil.toJSONString(userId), TraceIdUtils.getTraceId());
            try {
                userGroupRpcService.saveToGroup(uicGroup);
            }catch (Exception e){
                logger.error("userId={},error:{},traceId={}",  GfJsonUtil.toJSONString(userId), LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                throw  e;
            }
        }


    }


}
