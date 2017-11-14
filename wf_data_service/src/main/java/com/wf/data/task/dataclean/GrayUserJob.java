package com.wf.data.task.dataclean;

import com.wf.base.rpc.ConfigRpcService;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.entity.mycat.UicUser;
import com.wf.data.dao.entity.mysql.ReportGrayUser;
import com.wf.data.dao.entity.mysql.UicGroup;
import com.wf.data.dao.entity.mysql.UicUserLog;
import com.wf.data.service.ReportGrayUserService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicGroupService;
import com.wf.data.service.UicUserLogService;
import com.wf.data.service.elasticsearch.EsGrayService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拉取过往的灰名单用户并存入report_gray_user
 *
 * @author jianjian huang
 *         2017年8月23日
 */
@Component
public class GrayUserJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ReportGrayUserService reportGrayUserService;
    @Autowired
    private UicGroupService uicGroupService;
    @Autowired
    private TransConvertService transConvertService;
    @Autowired
    private EsGrayService grayService;
    @Autowired
    private UicUserLogService logService;


    public void execute() {
        logger.info("开始灰名单用户监控分析");
        byte count = 0;
        while (count <= 5) {
            try {
                //所有的灰名单用户
                List<UicGroup> grayList = getGrayUsersList();
                //
                if (CollectionUtils.isNotEmpty(grayList)) {
                    for (UicGroup uicGroup : grayList) {
                        Long userId = uicGroup.getUserId();
                        ReportGrayUser entity = reportGrayUserService.getByUserId(userId);
                        if (entity == null) {
                            entity = new ReportGrayUser();
                        }
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
                        Map<String, Object> params = new HashMap<>();
                        params.put("beginDate", DateUtils.formatDateTime(uicGroup.getCreateTime()));
                        params.put("endDate", DateUtils.formatCurrentDate());
                        params.put("userId", userId);
                        double afterGrayRecharge = transConvertService.getIncomeAmount(params);
                        entity.setAfterGrayRecharge(afterGrayRecharge);
                        reportGrayUserService.save(entity);
                    }
                }
                break;
            } catch (Exception e) {
                count++;
                if (count <= 5) {
                    logger.error(">>>>>>>>>>>>>>灰名单用户监控分析异常，重新执行 " + count, e);
                } else {
                    logger.error(">>>>>>>>>>>>>>灰名单用户监控分析异常，停止分析", e);
                }
            }
        }
    }

    //所有的灰名单用户
    private List<UicGroup> getGrayUsersList() {
        Long grayListGroup = UserGroupContents.GRAY_LIST_GROUP;
        List<UicGroup> grayList = uicGroupService.getInGroupByUserId(null, Arrays.asList(grayListGroup));
        return grayList;
    }

    private List<String> getWfIpsList() {
        ConfigRpcService configRpcService = SpringContextHolder.getBean(ConfigRpcService.class);
        // 获取公司ip
        String wfIps = configRpcService.findByName(DataConstants.MONITOR_RISK_WF_IPS).getValue();
        String[] wfIpsArr = wfIps.split(",");
        List<String> wfIpsList = Arrays.asList(wfIpsArr);
        return wfIpsList;
    }

}
