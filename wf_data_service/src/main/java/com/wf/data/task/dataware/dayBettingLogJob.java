package com.wf.data.task.dataware;

import com.google.common.collect.Lists;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.DatawareBettingLogHourService;
import com.wf.data.service.UicGroupService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
public class dayBettingLogJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);
    private final UicGroupService uicGroupService = SpringContextHolder.getBean(UicGroupService.class);
    private final DataDictService dataDictService = SpringContextHolder.getBean(DataDictService.class);
    private final DatawareBettingLogHourService datawareBettingLogHourService = SpringContextHolder.getBean(DatawareBettingLogHourService.class);

    public void execute() {
        logger.info("每小时投注汇总开始:traceId={}", TraceIdUtils.getTraceId());
        String bettingDate = DateUtils.getYesterdayDate();
        Date yesterday = DateUtils.parseDate(DateUtils.getYesterdayDate());

        List<Long> uicGroupList = Lists.newArrayList();
        String datawareUicGroup = dataConfigService.getStringValueByName(DataConstants.DATA_DATAWARE_UIC_GROUP);
        if (StringUtils.isNotEmpty(datawareUicGroup)) {
            String[] uicGroupArr = datawareUicGroup.split(",");
            List<String> userGroup = Arrays.asList(uicGroupArr);
            uicGroupList = uicGroupService.findGroupUsers(userGroup);
        } else {
            logger.error("非正常用户规则未设置: traceId={}", TraceIdUtils.getTraceId());
        }



        logger.info("每小时投注汇总结束:traceId={}", TraceIdUtils.getTraceId());
    }





    private int getUserGroup(Long userId, List<Long> uicGroupList) {
        Integer userGroupFlag;
        if (CollectionUtils.isNotEmpty(uicGroupList)) {
            if (uicGroupList.contains(userId)) {
                userGroupFlag = 1;
            } else {
                userGroupFlag = 2;
            }
        } else {
            userGroupFlag = 2;
        }
        return userGroupFlag;
    }
}
