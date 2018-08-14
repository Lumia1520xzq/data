package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;

/**
 * 金叶子充值报表导出Excel用Dto
 */
public class LeafReportRechargeDto implements Serializable {
    private static final long serialVersionUID = -1;
    private String businessDate;
    private String channelName;
    private Double rechargeAmountRmb;
    private Double rechargePresentedAmount;

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

    @ExcelField(title = "充值购买", type = 0, align = 1, sort = 3)
    public Double getRechargeAmountRmb() {
        return rechargeAmountRmb * 1000;
    }

    public void setRechargeAmountRmb(Double rechargeAmountRmb) {
        this.rechargeAmountRmb = rechargeAmountRmb;
    }

    @ExcelField(title = "充值赠送", type = 0, align = 1, sort = 4)
    public Double getRechargePresentedAmount() {
        return rechargePresentedAmount;
    }

    public void setRechargePresentedAmount(Double rechargePresentedAmount) {
        this.rechargePresentedAmount = rechargePresentedAmount;
    }
}
