package com.wf.data.task.dataclean;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.data.entity.ReportGrayUser;
import com.wf.data.dao.uic.entity.UicGroup;
import com.wf.data.dao.uic.entity.UicUser;
import com.wf.data.dao.uic.entity.UicUserLog;
import com.wf.data.service.*;
import com.wf.data.service.elasticsearch.EsGrayService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 拉取过往的灰名单用户并存入report_gray_user
 *
 * @author jianjian huang
 *         2017年8月23日
 */
public class GrayUserJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UicUserLogService logService = SpringContextHolder.getBean(UicUserLogService.class);
    private final ReportGrayUserService reportGrayUserService = SpringContextHolder.getBean(ReportGrayUserService.class);
    private final EsGrayService grayService = SpringContextHolder.getBean(EsGrayService.class);
    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);

    public void execute() {
        logger.info("开始灰名单用户监控分析:traceId={}",TraceIdUtils.getTraceId());
        byte count = 0;
        while (count <= 5) {
            try {
                //所有的灰名单用户
                List<UicGroup> grayList = getGrayUsersList();
                //
                if(CollectionUtils.isNotEmpty(grayList)) {
                    List<ReportGrayUser> users = new ArrayList<ReportGrayUser>();
                    for (UicGroup uicGroup:grayList) {
                        Long userId = uicGroup.getUserId();
                        ReportGrayUser entity = reportGrayUserService.getByUserId(userId);
                        if (entity == null) {
                            entity = new ReportGrayUser();
                            //1、用户id
                            entity.setUserId(userId);
                            //2、注册时间
                            UicUser user = grayService.getNewUser(userId);
                            if (user == null) {
                                user = new UicUser();
                            }
                            entity.setRegTime(user.getCreateTime());
                            //3、拉灰时间
                            entity.setGrayTime(uicGroup.getCreateTime());
                            //4、注册渠道
                            entity.setRegChannelId(user.getRegChannelId());
                        }
                        //5、IP
                        List<String> ips = logService.findIpByUserId(userId);
                        if (CollectionUtils.isNotEmpty(ips)) {
                            ips.removeAll(getWfIpsList());
                            UicUserLog uicUserLog = logService.getUserCountByIp(ips);
                            entity.setIp(uicUserLog.getIp());
                            entity.setIpUserCount(uicUserLog.getUserCount());
                        }
                        //6、总充值
                        double sumRecharge = transConvertService.findUserSumRecharge(userId);
                        entity.setSumRecharge(sumRecharge);
                        //7、拉灰后充值
                        Map<String,Object> params = new HashMap<>();
                        params.put("beginDate", DateUtils.formatDateTime(uicGroup.getCreateTime()));
                        params.put("endDate", DateUtils.formatCurrentDate());
                        params.put("userId", userId);
                        double afterGrayRecharge = transConvertService.getIncomeAmount(params);
                        entity.setAfterGrayRecharge(afterGrayRecharge);
                        users.add(entity);
                    }
                    logger.info("拉灰用户名单: traceId={}, jsonObject={},", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(users));
                    try {
                        reportGrayUserService.batchSave(users);
                    }catch (Exception e){
                        logger.error("report_gray_user表保存失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                    }
                }
                logger.info("灰名单用户监控分析结束:traceId={}",TraceIdUtils.getTraceId());
                break;
            } catch (Exception e) {
                count++;
                if (count <= 5) {
                    logger.error("灰名单用户监控分析异常，重新执行{},ex={},traceId={}",count, LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                } else {
                    logger.error("灰名单用户监控分析异常，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                }
            }
        }
    }

    //所有的灰名单用户
    private List<UicGroup> getGrayUsersList(){
        Long grayListGroup = UserGroupContents.GRAY_LIST_GROUP;
        List<UicGroup> grayList = uicGroupService.getInGroupByUserId(null,Arrays.asList(grayListGroup));
        return grayList;
    }

    private List<String> getWfIpsList(){
        // 获取公司ip
        String wfIps = dataConfigService.findByName(DataConstants.MONITOR_RISK_WF_IPS).getValue();
        String[] wfIpsArr = wfIps.split(",");
        List<String> wfIpsList = Arrays.asList(wfIpsArr);
        return  wfIpsList;
    }

}
