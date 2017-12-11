package com.wf.data.mqc.processor;

import com.google.common.collect.Lists;
import com.wf.core.cache.CacheHander;
import com.wf.core.cache.CacheKey;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.DataIpInfo;
import com.wf.data.dao.data.entity.DataUserGroupLog;
import com.wf.data.dao.data.entity.DataUserLog;
import com.wf.data.dao.uic.entity.UicGroup;
import com.wf.data.service.*;
import com.wf.uic.rpc.UserGroupRpcService;
import com.wf.uic.rpc.dto.UicGroupDto;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class UserLoginLogProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DataUserLogService dataUserLogService;
    @Resource
    private DataIpInfoService dataIpInfoService;
    @Resource
    private CacheHander cacheHander;
    @Resource
    private DataConfigService dataConfigService;
    @Resource
    private UserGroupRpcService userGroupRpcService;
    @Resource
    private TransConvertService transConvertService;
    @Resource
    private UicGroupService uicGroupService;
    @Resource
    private DataUserGroupLogService dataUserGroupLogService;

    public void process(final Map<String, Object> params) {
        logger.info("mqc对象: traceId={}, jsonObject={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(params.toString()));
        DataUserLog log = toObject(params);
        if (null == log) {
            return;
        }
        try {
            dataUserLogService.save(log);
        } catch (Exception e) {
            logger.error("dataUserLogService保存失败: traceId={}, jsonObject={}, ex={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(log.toString()), LogExceptionStackTrace.erroStackTrace(e));
        }


        if (null == log.getIp()) {
            return;
        }
        DataIpInfo entity = new DataIpInfo();
        entity.setIp(log.getIp());
        entity.setIpCount(1l);
        if (null == log.getCreateTime()) {
            log.setCreateTime(new Date());
        }
        entity.setLoginDate(DateUtils.formatDate(log.getCreateTime(), DateUtils.DATE_PATTERN));
        if (null != log.getChannelId()) {
            entity.setChannelId(log.getChannelId());
        }

        List<Long> userList = cacheHander.get(DataCacheKey.DATA_IP_USER.key(log.getIp(), entity.getLoginDate()));
        if (CollectionUtils.isNotEmpty(userList)) {
            if (!userList.contains(log.getUserId())) {
                userList.add(log.getUserId());
                cacheHander.set(DataCacheKey.DATA_IP_USER.key(log.getIp(), entity.getLoginDate()), userList, CacheKey.DAY_2);
                dataIpInfoService.updateIpCount(entity);


                // 获取公司ip
                String wfIps = dataConfigService.findByName(DataConstants.MONITOR_RISK_WF_IPS).getValue();
                String[] wfIpsArr = wfIps.split(",");
                List<String> wfIpsList = Arrays.asList(wfIpsArr);
                if (wfIpsList.contains(log.getIp())) {
                    logger.info("公司ip不风控: traceId={}, ip={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(log.getIp()));
                    return;
                }

                int flag = getFlog(log.getIp(), entity.getLoginDate());
                if (0 == flag) {
                    risk(userList, log.getIp(), entity.getLoginDate());

                } else {

                    double rechargeBase = dataConfigService.getDoubleValueByName(DataConstants.MONITOR_RISK_USER_RECHARGE);

                    List<Long> userGroup = Arrays.asList(UserGroupContents.GRAY_LIST_GROUP);
                    List<UicGroup> grayUicGroupList = uicGroupService.getInGroupByUserId(log.getUserId(), userGroup);
                    if (CollectionUtils.isNotEmpty(grayUicGroupList)) {
                        logger.info("用户已在灰名单: traceId={}, userId={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(log.getUserId()));
                        return;
                    }
                    double recharge = transConvertService.findUserSumRecharge(log.getUserId());
                    if (recharge >= rechargeBase) {
                        logger.info("充值金额={}不风控: traceId={},userId={}", GfJsonUtil.toJSONString(recharge), TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(log.getUserId()));
                        return;
                    }
                    UicGroupDto uicGroup = new UicGroupDto();
                    uicGroup.setUserId(log.getUserId());
                    uicGroup.setGroupTypeId(UserGroupContents.GRAY_LIST_GROUP);
                    uicGroup.setGroupTypeParentId(UserGroupContents.BLACK_WHITE_LIST_GROUP);
                    uicGroup.setUserData("ip风控,ip数:" + userList.size() + ",充值:" + recharge + "元");

                    DataUserGroupLog groupLog = new DataUserGroupLog();
                    groupLog.setUserId(log.getUserId());
                    groupLog.setGroupTypeId(UserGroupContents.GRAY_LIST_GROUP);
                    groupLog.setRemark("ip风控,ip数:" + userList.size() + ",充值:" + recharge + "元");
                    try {
                        userGroupRpcService.saveToGroup(uicGroup);
                        dataUserGroupLogService.save(groupLog);

                    } catch (Exception e) {
                        logger.error("单用户加入灰名单,rpc调用失败,error:{},traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        throw e;
                    }

                }


            }

        } else {
            List<Long> userIds = Lists.newArrayList();
            userIds.add(log.getUserId());
            cacheHander.set(DataCacheKey.DATA_IP_USER.key(log.getIp(), entity.getLoginDate()), userIds, CacheKey.DAY_2);
            dataIpInfoService.updateIpCount(entity);
        }


    }


    private void risk(List<Long> userIds, String ip, String date) {

        int ipCount = dataConfigService.getIntValueByName(DataConstants.MONITOR_IP_COUNT);
        if (ipCount == 0) {
            logger.error("根据ip分析用户行为iPCount未设置: traceId={}, ipCount={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(ipCount));
            return;
        }

        double rechargeBase = dataConfigService.getDoubleValueByName(DataConstants.MONITOR_RISK_USER_RECHARGE);

        if (userIds.size() >= ipCount) {
            List<UicGroupDto> list = Lists.newArrayList();
            List<DataUserGroupLog> logList = Lists.newArrayList();
            for (Long userId : userIds) {
                List<Long> userGroup = Arrays.asList(UserGroupContents.GRAY_LIST_GROUP);

                List<UicGroup> grayUicGroupList = uicGroupService.getInGroupByUserId(userId, userGroup);
                if (CollectionUtils.isNotEmpty(grayUicGroupList)) {
                    logger.info("用户已在灰名单: traceId={}, userId={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(userId));
                    continue;
                }
                double recharge = transConvertService.findUserSumRecharge(userId);
                if (recharge >= rechargeBase) {
                    logger.info("充值金额={}不风控: traceId={},userId={}", GfJsonUtil.toJSONString(recharge), TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(userId));
                    continue;
                }
                UicGroupDto uicGroup = new UicGroupDto();
                uicGroup.setUserId(userId);
                uicGroup.setGroupTypeId(UserGroupContents.GRAY_LIST_GROUP);
                uicGroup.setGroupTypeParentId(UserGroupContents.BLACK_WHITE_LIST_GROUP);
                uicGroup.setUserData("ip风控,ip数:" + userIds.size() + ",充值:" + recharge + "元");
                list.add(uicGroup);

                DataUserGroupLog groupLog = new DataUserGroupLog();
                groupLog.setUserId(userId);
                groupLog.setGroupTypeId(UserGroupContents.GRAY_LIST_GROUP);
                groupLog.setRemark("ip风控,ip数:" + userIds.size() + ",充值:" + recharge + "元");
                logList.add(groupLog);
            }
            try {
                userGroupRpcService.saveGroupList(list);
                dataUserGroupLogService.batchSave(logList);
                cacheHander.set(DataCacheKey.DATA_RISK_FLAG.key(ip, date), 1, CacheKey.DAY_2);
            } catch (Exception e) {
                cacheHander.set(DataCacheKey.DATA_RISK_FLAG.key(ip, date), 0, CacheKey.DAY_2);
                logger.error("uic-rpc调用失败,error:{},traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                throw e;
            }

        }


    }

    private int getFlog(String ip, String date) {
        Integer flag = cacheHander.get(DataCacheKey.DATA_RISK_FLAG.key(ip, date));
        if (null == flag) {
            cacheHander.set(DataCacheKey.DATA_RISK_FLAG.key(ip, date), 0, CacheKey.DAY_2);
            return 0;
        } else {
            return flag;
        }
    }


    /**
     * map转object
     *
     * @param params
     * @return
     */
    private DataUserLog toObject(Map<String, Object> params) {
        DataUserLog log = new DataUserLog();

        if (null != params.get("userId")) {
            log.setUserId(Long.valueOf(params.get("userId").toString()));
        }
        if (null != params.get("type")) {
            log.setType(Integer.valueOf(params.get("type").toString()));
        }
        if (null != params.get("ip")) {
            log.setIp(params.get("ip").toString());
        }
        if (null != params.get("token")) {
            log.setToken(params.get("token").toString());
        }
        if (null != params.get("channelId")) {
            log.setChannelId(Long.valueOf(params.get("channelId").toString()));
        }
        if (null != params.get("create_time")) {
            log.setCreateTime((Date) params.get("create_time"));
        }

        return log;
    }


}
