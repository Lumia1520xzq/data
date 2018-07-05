package com.wf.data.controller.admin.business;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.extjs.data.DataGrid;
import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareChannelFinanceCheckDay;
import com.wf.data.dto.LeafReportFlowDto;
import com.wf.data.dto.LeafReportRechargeDto;
import com.wf.data.dto.LeafReportSurplusDto;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareChannelFinanceCheckDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金叶子流转数据报表
 *
 */
@RestController
@RequestMapping(value = "/data/admin/business/leafDaily")
public class LeafDailyController extends ExtJsController {

    @Autowired
    private DatawareChannelFinanceCheckDayService datawareChannelFinanceCheckDayService;

    @Autowired
    private ChannelInfoService channelInfoService;

    private static String DATETIME_PATTEN = "yyyyMMddHHmmss";

    private static String EXCEL_SUFFIX = ".xlsx";

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        JSONObject json = getRequestJson();
        JSONObject data = json.getJSONObject("data");
        List<Long> parentId = new ArrayList<>();
        String beginDate = null;
        String endDate = null;
        Integer showChannel = null;
        Integer searchType = null;
        if (data != null) {
            String js = JSONObject.toJSONString(data.getJSONArray("parentId"),
                    SerializerFeature.WriteClassName);
            parentId = JSONObject.parseArray(js, Long.class);
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            // “全部”渠道的场合
            if (parentId.isEmpty()) {
                // 不显示渠道
                showChannel = 0;
            } else {
                showChannel = data.getInteger("showChannel");
            }
            searchType = data.getInteger("searchType");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", parentId);
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("showChannel", showChannel);
        map.put("searchType", searchType);
        // 获取所有符合条件的记录
        List<DatawareChannelFinanceCheckDay > leafDailyList = datawareChannelFinanceCheckDayService.getLeafDailyList(map);
        // 查询结果为空的场合
        if (leafDailyList == null || leafDailyList.isEmpty()) {
            return new DataGrid<>();
        }
        // 获取请求对应的的页数据-----start
        String sStart = json.getString("start");
        String sLength = json.getString("limit");
        int start = 0;
        int length = 0;
        if (sStart != null) {
            start = Integer.valueOf(sStart);
        }
        if (sLength != null) {
            length = Integer.valueOf(sLength);
        }
        List<DatawareChannelFinanceCheckDay> pageData = new ArrayList<>();
        int total = leafDailyList.size();
        if (start <= total) {
            int end = start + length;
            if (end > total) {
                end = total;
            }
            pageData = leafDailyList.subList(start, end);
        }
        Page<DatawareChannelFinanceCheckDay > page = new Page<>();
        page.setStart(start);
        page.setLength((long) length);
        page.setData(pageData);
        page.setCount(total);
        // 获取请求对应的的页数据-----end
        return dataGrid(page);
    }

    /**
     * 导出Excel文件
     * @param parentId
     * @param showChannel
     * @param searchType
     * @param endDate
     * @param beginDate
     * @param tabId
     * @param response
     */
    @RequestMapping("/export")
    public void export(@RequestParam List<Long> parentId, @RequestParam String showChannel,
                       @RequestParam String searchType, @RequestParam String endDate,
                       @RequestParam String beginDate, @RequestParam String tabId, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", parentId);
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("showChannel", showChannel);
        map.put("searchType", searchType);
        try {
            List<DatawareChannelFinanceCheckDay > leafDailyList = datawareChannelFinanceCheckDayService.getLeafDailyList(map);
            ChannelInfo cInfo;
            String fileName;
            // 充值
            if ("rechargeTab".equals(tabId)) {
                List<LeafReportRechargeDto> recharges = new ArrayList<>();
                LeafReportRechargeDto recharge;
                for (DatawareChannelFinanceCheckDay item : leafDailyList) {
                    recharge = new LeafReportRechargeDto();
                    recharge.setBusinessDate(item.getBusinessDate());
                    recharge.setRechargeAmountRmb(item.getRechargeAmountRmb());
                    recharge.setRechargePresentedAmount(item.getRechargePresentedAmount());
                    cInfo = channelInfoService.get(item.getChannelId());
                    recharge.setChannelName(cInfo.getName() + "(" + cInfo.getId() + ")");
                    recharges.add(recharge);
                }
                fileName = "金叶子流转数据报表（充值）" + DateUtils.getDate(DATETIME_PATTEN) +EXCEL_SUFFIX;
                new ExportExcel("金叶子流转数据报表（充值）", LeafReportRechargeDto.class).setDataList(recharges).write(response, fileName).dispose();
            } else if ("flowTab".equals(tabId)) { // 流转
                List<LeafReportFlowDto> flows = new ArrayList<>();
                LeafReportFlowDto flow;
                for (DatawareChannelFinanceCheckDay item : leafDailyList) {
                    flow = new LeafReportFlowDto();
                    flow.setBusinessDate(item.getBusinessDate());
                    flow.setConsumeAmount(item.getConsumeAmount());
                    flow.setDiffAmount(item.getDiffAmount());
                    flow.setOtherwaysGoldAmount(item.getOtherwaysGoldAmount());
                    flow.setReturnAmount(item.getReturnAmount());
                    cInfo = channelInfoService.get(item.getChannelId());
                    flow.setChannelName(cInfo.getName() + "(" + cInfo.getId() + ")");
                    flows.add(flow);
                }
                fileName = "金叶子流转数据报表（流转）" + DateUtils.getDate(DATETIME_PATTEN) + EXCEL_SUFFIX;
                new ExportExcel("金叶子流转数据报表（流转）", LeafReportFlowDto.class).setDataList(flows).write(response, fileName).dispose();
            } else { // 存量
                List<LeafReportSurplusDto> surplusDtos = new ArrayList<>();
                LeafReportSurplusDto surplusDto;
                for (DatawareChannelFinanceCheckDay item : leafDailyList) {
                    surplusDto = new LeafReportSurplusDto();
                    surplusDto.setBusinessDate(item.getBusinessDate());
                    surplusDto.setSurplusAmount(item.getSurplusAmount());
                    cInfo = channelInfoService.get(item.getChannelId());
                    surplusDto.setChannelName(cInfo.getName() + "(" + cInfo.getId() + ")");
                    surplusDtos.add(surplusDto);
                }
                fileName = "金叶子流转数据报表（存量）" + DateUtils.getDate(DATETIME_PATTEN) + EXCEL_SUFFIX;
                new ExportExcel("金叶子流转数据报表（存量）", LeafReportSurplusDto.class).setDataList(surplusDtos).write(response, fileName).dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
