package com.wf.data.service.business;

import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalRegisteredArpu;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareConvertDayService;
import com.wf.data.service.data.DatawareFinalRegisteredArpuService;
import com.wf.data.service.data.DatawareUserInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2018年01月03日
 */
@Service
public class RegisteredArpuService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalRegisteredArpuService datawareFinalRegisteredArpuService;
    @Autowired
    private DatawareUserInfoService datawareUserInfoService;
    @Autowired
    private DatawareConvertDayService datawareConvertDayService;

    public void toDoAnalysis() {
        logger.info("用户ARPU分析开始:traceId={}", TraceIdUtils.getTraceId());

        String searchDate = DateUtils.getYesterdayDate();
        registeredArpu(searchDate);

        logger.info("用户ARPU分析结束:traceId={}", TraceIdUtils.getTraceId());
    }


    private void registeredArpu(String businessDate) {
        try {
            dataKettle(null, businessDate);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                dataKettle(item, businessDate);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


    private void dataKettle(ChannelInfo channelInfo, String businessDate) {
        DatawareFinalRegisteredArpu info = new DatawareFinalRegisteredArpu().toInit();
        info.setBusinessDate(businessDate);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }

        Map<String, Object> arpuParams = new HashMap<>();
        arpuParams.put("businessDate", businessDate);
        if (channelInfo != null) {
            arpuParams.put("parentId", channelInfo.getId());
        } else {
            arpuParams.put("parentId", 1);
        }

        DatawareFinalRegisteredArpu arpu = datawareFinalRegisteredArpuService.getArpuByDate(arpuParams);
        if (null != arpu) {
            info = arpu;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", businessDate);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        }
        //D1注册用户数
        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(params);
        if (CollectionUtils.isNotEmpty(dayNewUserList)) {
            info.setNewUsers(Long.valueOf(dayNewUserList.size()));
        } else {
            info.setNewUsers(0L);

        }
        //D1注册用户充值金额
        Double rechargeAmount = datawareConvertDayService.getRegisteredConvertByDate(params);
        if (null == rechargeAmount) {
            info.setRecharge1(0.00);
        } else {
            info.setRecharge1(rechargeAmount);
        }

        //D1注册用户充值金额/注册人数
        if (info.getNewUsers() > 0) {
            info.setArpu1(BigDecimalUtil.div(info.getRecharge1(), info.getNewUsers(), 2));
        } else {
            info.setArpu1(0.00);
        }

        try {
            datawareFinalRegisteredArpuService.save(info);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }
        //d2
        daysArpu(channelInfo, businessDate, 1);
        //d3
        daysArpu(channelInfo, businessDate, 2);
        //d4
        daysArpu(channelInfo, businessDate, 3);
        //d5
        daysArpu(channelInfo, businessDate, 4);
        //d6
        daysArpu(channelInfo, businessDate, 5);
        //d7
        daysArpu(channelInfo, businessDate, 6);
        //d15
        daysArpu(channelInfo, businessDate, 14);
        //d30
        daysArpu(channelInfo, businessDate, 29);
        //d60
        daysArpu(channelInfo, businessDate, 59);
        //d90
        daysArpu(channelInfo, businessDate, 89);


    }

    private void daysArpu(ChannelInfo channelInfo, String businessDate, int i) {

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(businessDate), -i)));
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());
        } else {
            params.put("parentId", 1);
        }

        DatawareFinalRegisteredArpu arpu = datawareFinalRegisteredArpuService.getArpuByDate(params);
        if (null == arpu) {
            return;
        }

        Map<String, Object> convertParams = new HashMap<>();
        if (channelInfo != null) {
            convertParams.put("parentId", channelInfo.getId());
        }
        convertParams.put("startDate", arpu.getBusinessDate());
        convertParams.put("endDate", businessDate);
        //D1注册用户充值金额
        Double rechargeAmount = datawareConvertDayService.getRegisteredConvertByDate(convertParams);
        if (null == rechargeAmount) {
            rechargeAmount = 0.00;
        }

        if (i == 1) {
            arpu.setRecharge2(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu2(BigDecimalUtil.div(arpu.getRecharge2(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu2(0.00);
            }
        } else if (i == 2) {
            arpu.setRecharge3(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu3(BigDecimalUtil.div(arpu.getRecharge3(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu3(0.00);
            }
        } else if (i == 3) {
            arpu.setRecharge4(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu4(BigDecimalUtil.div(arpu.getRecharge4(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu4(0.00);
            }
        } else if (i == 4) {
            arpu.setRecharge5(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu5(BigDecimalUtil.div(arpu.getRecharge5(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu5(0.00);
            }
        } else if (i == 5) {
            arpu.setRecharge6(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu6(BigDecimalUtil.div(arpu.getRecharge6(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu6(0.00);
            }
        } else if (i == 6) {
            arpu.setRecharge7(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu7(BigDecimalUtil.div(arpu.getRecharge7(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu7(0.00);
            }
        } else if (i == 14) {
            arpu.setRecharge15(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu15(BigDecimalUtil.div(arpu.getRecharge15(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu15(0.00);
            }
        } else if (i == 29) {
            arpu.setRecharge30(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu30(BigDecimalUtil.div(arpu.getRecharge30(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu30(0.00);
            }
        } else if (i == 59) {
            arpu.setRecharge60(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu60(BigDecimalUtil.div(arpu.getRecharge60(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu60(0.00);
            }
        } else if (i == 89) {
            arpu.setRecharge90(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu90(BigDecimalUtil.div(arpu.getRecharge90(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu90(0.00);
            }
        }

        try {
            datawareFinalRegisteredArpuService.save(arpu);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(arpu.toString()));
        }
    }

    @Async
    public void dataClean(String startTime, String endTime) {

        try {

            if (startTime.equals(endTime)) {
                historyRegisteredArpu(endTime);
            } else {
                List<String> datelist = DateUtils.getDateList(startTime, endTime);
                for (String searchDate : datelist) {
                    historyRegisteredArpu(searchDate);
                }
            }

        } catch (Exception e) {
            logger.error("失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
            return;
        }
        logger.info("老数据清洗结束:traceId={}", TraceIdUtils.getTraceId());
    }

    private void historyRegisteredArpu(String businessDate) {
        try {
            historyDataKettle(null, businessDate);
            List<ChannelInfo> channelInfoList = channelInfoService.findMainChannel();
            for (ChannelInfo item : channelInfoList) {
                historyDataKettle(item, businessDate);
            }
        } catch (Exception e) {
            logger.error("添加渠道汇总记录失败: traceId={}, ex={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }


    private void historyDataKettle(ChannelInfo channelInfo, String businessDate) {
        DatawareFinalRegisteredArpu info = new DatawareFinalRegisteredArpu().toInit();
        info.setBusinessDate(businessDate);
        if (channelInfo != null) {
            info.setChannelName(channelInfo.getName());
            info.setChannelId(channelInfo.getId());
            info.setParentId(channelInfo.getId());
        } else {
            info.setChannelName("全部");
            info.setChannelId(1L);
            info.setParentId(1L);
        }


        Map<String, Object> arpuParams = new HashMap<>();
        arpuParams.put("businessDate", businessDate);
        if (channelInfo != null) {
            arpuParams.put("parentId", channelInfo.getId());
        } else {
            arpuParams.put("parentId", 1);
        }

        DatawareFinalRegisteredArpu arpu = datawareFinalRegisteredArpuService.getArpuByDate(arpuParams);
        if (null != arpu) {
            info = arpu;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", businessDate);
        if (channelInfo != null) {
            params.put("parentId", channelInfo.getId());

        }
        //D1注册用户数
        List<Long> dayNewUserList = datawareUserInfoService.getNewUserByTime(params);
        if (CollectionUtils.isNotEmpty(dayNewUserList)) {
            info.setNewUsers(Long.valueOf(dayNewUserList.size()));
        } else {
            info.setNewUsers(0L);

        }
        //D1注册用户充值金额
        Double rechargeAmount = datawareConvertDayService.getRegisteredConvertByDate(params);
        if (null == rechargeAmount) {
            info.setRecharge1(0.00);
        } else {
            info.setRecharge1(rechargeAmount);
        }

        //D1注册用户充值金额/注册人数
        if (info.getNewUsers() > 0) {
            info.setArpu1(BigDecimalUtil.div(info.getRecharge1(), info.getNewUsers(), 2));
        } else {
            info.setArpu1(0.00);
        }


        //d2
        info = getArpu(channelInfo, businessDate, info, 1);
        //d3
        info = getArpu(channelInfo, businessDate, info, 2);
        //d4
        info = getArpu(channelInfo, businessDate, info, 3);
        //d5
        info = getArpu(channelInfo, businessDate, info, 4);
        //d6
        info = getArpu(channelInfo, businessDate, info, 5);
        //d7
        info = getArpu(channelInfo, businessDate, info, 6);
        //d15
        info = getArpu(channelInfo, businessDate, info, 14);
        //d30
        info = getArpu(channelInfo, businessDate, info, 29);
        //d60
        info = getArpu(channelInfo, businessDate, info, 59);
        //d90
        info = getArpu(channelInfo, businessDate, info, 89);

        try {
            datawareFinalRegisteredArpuService.save(info);
        } catch (Exception e) {
            logger.error("添加记录失败: traceId={}, ex={},data={}", TraceIdUtils.getTraceId(), LogExceptionStackTrace.erroStackTrace(e), GfJsonUtil.toJSONString(info.toString()));
        }
    }

    private DatawareFinalRegisteredArpu getArpu(ChannelInfo channelInfo, String businessDate, DatawareFinalRegisteredArpu arpu, int i) {

        Date endDate = DateUtils.getNextDate(DateUtils.parseDate(businessDate), i);
        String endDateStr = DateUtils.formatDate(endDate);
        if (endDate.getTime() >= DateUtils.parseDate(DateUtils.formatCurrentDateYMD()).getTime()) {
            return arpu;
        }

        Map<String, Object> convertParams = new HashMap<>();
        if (channelInfo != null) {
            convertParams.put("parentId", channelInfo.getId());
        }
        convertParams.put("startDate", businessDate);
        convertParams.put("endDate", endDateStr);
        //注册用户充值金额
        Double rechargeAmount = datawareConvertDayService.getRegisteredConvertByDate(convertParams);
        if (null == rechargeAmount) {
            rechargeAmount = 0.00;
        }

        if (i == 1) {
            arpu.setRecharge2(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu2(BigDecimalUtil.div(arpu.getRecharge2(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu2(0.00);
            }
        } else if (i == 2) {
            arpu.setRecharge3(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu3(BigDecimalUtil.div(arpu.getRecharge3(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu3(0.00);
            }
        } else if (i == 3) {
            arpu.setRecharge4(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu4(BigDecimalUtil.div(arpu.getRecharge4(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu4(0.00);
            }
        } else if (i == 4) {
            arpu.setRecharge5(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu5(BigDecimalUtil.div(arpu.getRecharge5(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu5(0.00);
            }
        } else if (i == 5) {
            arpu.setRecharge6(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu6(BigDecimalUtil.div(arpu.getRecharge6(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu6(0.00);
            }
        } else if (i == 6) {
            arpu.setRecharge7(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu7(BigDecimalUtil.div(arpu.getRecharge7(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu7(0.00);
            }
        } else if (i == 14) {
            arpu.setRecharge15(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu15(BigDecimalUtil.div(arpu.getRecharge15(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu15(0.00);
            }
        } else if (i == 29) {
            arpu.setRecharge30(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu30(BigDecimalUtil.div(arpu.getRecharge30(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu30(0.00);
            }
        } else if (i == 59) {
            arpu.setRecharge60(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu60(BigDecimalUtil.div(arpu.getRecharge60(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu60(0.00);
            }
        } else if (i == 89) {
            arpu.setRecharge90(rechargeAmount);
            if (arpu.getNewUsers() > 0) {
                arpu.setArpu90(BigDecimalUtil.div(arpu.getRecharge90(), arpu.getNewUsers(), 2));
            } else {
                arpu.setArpu90(0.00);
            }
        }


        return arpu;
    }
}
