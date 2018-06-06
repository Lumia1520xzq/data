package com.wf.data.controller.response;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

/**
 * @author shihui
 * @date 2018/3/21
 */
public class DataViewExcelResponse extends DataEntity {
    private static final long serialVersionUID = -1;

    private String businessDate;
    private Long channelId;
    private String channelName;
    private Long dau;
    private Double rechargeAmount;
    private Double bettingRate;
    private Double dauPayRate;
    private Double bettingPayRate;
    private Long newUsers;
    private Double usersRate;
    private Double userBettingRate;
    private Double firstRechargeRate;
    private Double weekRechargeRate;
    private Double usersDayRetention;
    private Double sevenRetention;
    private Double dayRetention;
    private Long rechargeCount;
    private Double payArpu;
    private Double payArppu;
    private Double totalCost;
    private Double costRate;
    private Long userCount;
    private Double bettingAmount;
    private Double resultRate;
    private Double moneyGap;
    private Long newRechargeCount;
    private Long newPayCovCycle;
    private Double rechargeRepRate;

    @ExcelField(title = "日期", type = 0, align = 1, sort = 10)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    @ExcelField(title = "渠道ID", type = 0, align = 1, sort = 20)
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @ExcelField(title = "渠道名称", type = 0, align = 1, sort = 30)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ExcelField(title = "DAU", type = 0, align = 1, sort = 40)
    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    @ExcelField(title = "充值金额", type = 0, align = 1, sort = 50)
    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    @ExcelField(title = "投注转化率(%)", type = 0, align = 1, sort = 60)
    public Double getBettingRate() {
        return bettingRate;
    }

    public void setBettingRate(Double bettingRate) {
        this.bettingRate = bettingRate;
    }

    @ExcelField(title = "DAU付费转化率(%)", type = 0, align = 1, sort = 70)
    public Double getDauPayRate() {
        return dauPayRate;
    }

    public void setDauPayRate(Double dauPayRate) {
        this.dauPayRate = dauPayRate;
    }

    @ExcelField(title = "投注付费转化率(%)", type = 0, align = 1, sort = 80)
    public Double getBettingPayRate() {
        return bettingPayRate;
    }

    public void setBettingPayRate(Double bettingPayRate) {
        this.bettingPayRate = bettingPayRate;
    }

    @ExcelField(title = "新增用户数", type = 0, align = 1, sort = 90)
    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    @ExcelField(title = "新用户占比(%)", type = 0, align = 1, sort = 100)
    public Double getUsersRate() {
        return usersRate;
    }

    public void setUsersRate(Double usersRate) {
        this.usersRate = usersRate;
    }

    @ExcelField(title = "新用户投注转化率(%)", type = 0, align = 1, sort = 110)
    public Double getUserBettingRate() {
        return userBettingRate;
    }

    public void setUserBettingRate(Double userBettingRate) {
        this.userBettingRate = userBettingRate;
    }

    @ExcelField(title = "首日付费率(%)", type = 0, align = 1, sort = 120)
    public Double getFirstRechargeRate() {
        return firstRechargeRate;
    }

    public void setFirstRechargeRate(Double firstRechargeRate) {
        this.firstRechargeRate = firstRechargeRate;
    }

    @ExcelField(title = "7日付费率(%)", type = 0, align = 1, sort = 130)
    public Double getWeekRechargeRate() {
        return weekRechargeRate;
    }

    public void setWeekRechargeRate(Double weekRechargeRate) {
        this.weekRechargeRate = weekRechargeRate;
    }

    @ExcelField(title = "新用户次留(%)", type = 0, align = 1, sort = 140)
    public Double getUsersDayRetention() {
        return usersDayRetention;
    }

    public void setUsersDayRetention(Double usersDayRetention) {
        this.usersDayRetention = usersDayRetention;
    }

    @ExcelField(title = "新用户7留(%)", type = 0, align = 1, sort = 150)
    public Double getSevenRetention() {
        return sevenRetention;
    }

    public void setSevenRetention(Double sevenRetention) {
        this.sevenRetention = sevenRetention;
    }

    @ExcelField(title = "全量用户次留(%)", type = 0, align = 1, sort = 160)
    public Double getDayRetention() {
        return dayRetention;
    }

    public void setDayRetention(Double dayRetention) {
        this.dayRetention = dayRetention;
    }

    @ExcelField(title = "充值人数", type = 0, align = 1, sort = 170)
    public Long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    @ExcelField(title = "DARPU", type = 0, align = 1, sort = 180)
    public Double getPayArpu() {
        return payArpu;
    }

    public void setPayArpu(Double payArpu) {
        this.payArpu = payArpu;
    }

    @ExcelField(title = "DARPPU", type = 0, align = 1, sort = 190)
    public Double getPayArppu() {
        return payArppu;
    }

    @ExcelField(title = "新增充值用户", type = 0, align = 1, sort = 191)
    public Long getNewRechargeCount() {
        return newRechargeCount;
    }

    public void setNewRechargeCount(Long newRechargeCount) {
        this.newRechargeCount = newRechargeCount;
    }

    @ExcelField(title = "新增付费转化周期", type = 0, align = 1, sort = 192)
    public Long getNewPayCovCycle() {
        return newPayCovCycle;
    }

    public void setNewPayCovCycle(Long newPayCovCycle) {
        this.newPayCovCycle = newPayCovCycle;
    }

    @ExcelField(title = "复购率", type = 0, align = 1, sort = 193)
    public Double getRechargeRepRate() {
        return rechargeRepRate;
    }

    public void setRechargeRepRate(Double rechargeRepRate) {
        this.rechargeRepRate = rechargeRepRate;
    }

    public void setPayArppu(Double payArppu) {
        this.payArppu = payArppu;
    }

    @ExcelField(title = "成本", type = 0, align = 1, sort = 200)
    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @ExcelField(title = "成本占比(%)", type = 0, align = 1, sort = 210)
    public Double getCostRate() {
        return costRate;
    }

    public void setCostRate(Double costRate) {
        this.costRate = costRate;
    }

    @ExcelField(title = "投注人数", type = 0, align = 1, sort = 220)
    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    @ExcelField(title = "投注流水", type = 0, align = 1, sort = 230)
    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    @ExcelField(title = "返奖率(%)", type = 0, align = 1, sort = 240)
    public Double getResultRate() {
        return resultRate;
    }

    public void setResultRate(Double resultRate) {
        this.resultRate = resultRate;
    }

    @ExcelField(title = "流水差", type = 0, align = 1, sort = 250)
    public Double getMoneyGap() {
        return moneyGap;
    }

    public void setMoneyGap(Double moneyGap) {
        this.moneyGap = moneyGap;
    }
}
