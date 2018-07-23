package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;

/**
 * 金叶子剩余报表导出Excel用Dto
 */
public class LeafReportSurplusDto implements Serializable {
    private static final long serialVersionUID = -1;
    private String businessDate;
    private String channelName;
    private Double surplusAmount;

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

    @ExcelField(title = "用户金叶子存量", type = 0, align = 1, sort = 3)
    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
