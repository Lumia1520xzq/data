package com.wf.data.service.data;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.service.CrudService;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.DatawareFinalRechargeTagAnalysisDao;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;
import com.wf.data.service.ChannelInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lcs
 */
@Service
public class DatawareFinalRechargeTagAnalysisService extends CrudService<DatawareFinalRechargeTagAnalysisDao, DatawareFinalRechargeTagAnalysis> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;

    /**
     * job调用入口
     */
    public void toDoAnalysis() {
        logger.info("用户分层分析开始:traceId={}", TraceIdUtils.getTraceId());
        //昨天
        String yesterdayDate = DateUtils.getYesterdayDate();
        //前天
        String twoDayBefore = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -2));

        try {
            //汇总全部渠道数据
            dataKettle(null, yesterdayDate, twoDayBefore);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                //汇总各个主渠道数据
                dataKettle(item, yesterdayDate, twoDayBefore);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
        logger.info("用户分层分析结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void dataKettle(ChannelInfo channelInfo, String yesterdayDate, String twoDayBefore) {
        DatawareFinalRechargeTagAnalysis info = new DatawareFinalRechargeTagAnalysis().toInit();
        Map<String, Object> userParams = new HashMap<>();

        info.setBusinessDate(yesterdayDate);
        if (channelInfo != null) {
            userParams.put("parentId", channelInfo.getParentId());

            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            userParams.remove("parentId");

            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }
        userParams.put("businessDate", yesterdayDate);
        List<Long> dauUserList = datawareBuryingPointDayService.getUserIdListByChannel(userParams);


    }


    private void newUser(DatawareFinalRechargeTagAnalysis info, List<Long> dauUserList, Map<String, Object> params) {

        List<Long> registerdList = datawareUserInfoService.getNewUserByDate(params);

        if (CollectionUtils.isNotEmpty(registerdList)) {
            if (CollectionUtils.isNotEmpty(dauUserList)) {
                info = getDauData(info, registerdList, dauUserList);
                info.setLoginCount(loginCount(registerdList, params));
            }
        }
        info.setUserTag(0);
        save(info);


    }

    /**
     * 活跃数据
     *
     * @param dto
     * @param userList
     * @param dauUserList
     * @return
     */
    private DatawareFinalRechargeTagAnalysis getDauData(DatawareFinalRechargeTagAnalysis dto, List<Long> userList, List<Long> dauUserList) {

        Collection interColl = CollectionUtils.intersection(userList, dauUserList);
        //新用户且活跃的用户
        List<Long> newUserDauList = (List<Long>) interColl;
        if (null != userList) {
            dto.setDau(Long.valueOf(newUserDauList.size()));
        }
        dto.setDauRate(BigDecimalUtil.div(dto.getDau() * 100, dauUserList.size(), 2));
        dto.setTotalUserCount(Long.valueOf(userList.size()));
        dto.setTotalUserRate(BigDecimalUtil.div(dto.getDau() * 100, dto.getTotalUserCount(), 2));
        return dto;
    }


    /**
     * 人均登陆次数
     * 一千个用户循环一次
     *
     * @param entitys
     * @param params
     * @return
     */
    private Long loginCount(List<Long> entitys, Map<String, Object> params) {
        Long loginCount = 0L;
        int i = 0;

        while (i < entitys.size()) {
            int var10002 = i;
            i += 1000;
            loginCount += doGetLoginCount(entitys, params, var10002, i >= entitys.size() ? entitys.size() : i);
        }
        return loginCount;

    }

    private Long doGetLoginCount(List<Long> entitys, Map<String, Object> params, int startIndex, int endIndex) {
        List<Long> userList = entitys.subList(startIndex, endIndex);
        params.put("userList", userList);
        return datawareBuryingPointDayService.getUserPointCount(params);

    }

}