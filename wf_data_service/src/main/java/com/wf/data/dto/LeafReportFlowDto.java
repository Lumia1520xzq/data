package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;

/**
 * 金叶子流转报表导出Excel用Dto
 */
public class LeafReportFlowDto implements Serializable {
    private static final long serialVersionUID = -1;
    private String businessDate;
    private String channelName;
    private Double otherwaysGoldAmount;
    private Double consumeAmount;
    private Double returnAmount;
    private Double diffAmount;

    @ExcelField(title = "日期", type = 0, align = 1, sort = 1)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    @ExcelField(title = "渠道", type = 0, align = 1, sort = 2)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ExcelField(title = "金叶子其他来源", type = 0, align = 1, sort = 3)
    public Double getOtherwaysGoldAmount() {
        return otherwaysGoldAmount;
    }

    public void setOtherwaysGoldAmount(Double otherwaysGoldAmount) {
        this.otherwaysGoldAmount = otherwaysGoldAmount;
    }

    @ExcelField(title = "金叶子消耗", type = 0, align = 1, sort = 4)
    public Double getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(Double consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    @ExcelField(title = "返奖金叶子", type = 0, align = 1, sort = 5)
    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    @ExcelField(title = "投注流水差", type = 0, align = 1, sort = 6)
    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }
}
