package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;

public class UserDetailsDto implements Serializable {
    private Long userId;
    private String userName;
    private String businessDate;
    private Long channelId;
    private String channelName;
    private Double kindCost;
    private Double rechargeAmount;
    private Double costRate;
    private Long bettingCount;
    private Double bettingAmount;
    private Double resultAmount;
    private Double returnRate;
    private String games;
    private Double profitAmount;
    private Double noUseGoldAmount;

    @ExcelField(title = "用户ID", type = 1, align = 2, sort = 3)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @ExcelField(title = "用户昵称", type = 1, align = 2, sort = 2)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ExcelField(title = "日期", type = 1, align = 2, sort = 1)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @ExcelField(title = "渠道", type = 1, align = 2, sort = 4)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ExcelField(title = "成本", type = 1, align = 2, sort = 5)
    public Double getKindCost() {
        return kindCost;
    }

    public void setKindCost(Double kindCost) {
        this.kindCost = kindCost;
    }

    @ExcelField(title = "充值金额", type = 1, align = 2, sort = 6)
    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    @ExcelField(title = "成本占比", type = 1, align = 2, sort = 7)
    public Double getCostRate() {
        return costRate;
    }

    public void setCostRate(Double costRate) {
        this.costRate = costRate;
    }

    @ExcelField(title = "投注笔数", type = 1, align = 2, sort = 8)
    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

    @ExcelField(title = "投注流水", type = 1, align = 2, sort = 9)
    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    public Double getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(Double resultAmount) {
        this.resultAmount = resultAmount;
    }

    @ExcelField(title = "返奖率", type = 1, align = 2, sort = 10)
    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    @ExcelField(title = "投注游戏", type = 1, align = 2, sort = 11)
    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    @ExcelField(title = "P", type = 1, align = 2, sort = 12)
    public Double getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(Double profitAmount) {
        this.profitAmount = profitAmount;
    }

    @ExcelField(title = "S3", type = 1, align = 2, sort = 13)
    public Double getNoUseGoldAmount() {
        return noUseGoldAmount;
    }

    public void setNoUseGoldAmount(Double noUseGoldAmount) {
        this.noUseGoldAmount = noUseGoldAmount;
    }
}
