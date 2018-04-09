package com.wf.data.controller.response;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

/**
 * @author shihui
 * @date 2018/3/28
 */
public class HourlyMonitorExprotResponse extends DataEntity {
    private static final long serialVersionUID = -1;

    private String businessDate;
    private String businessHour;
    private Long parentId;
    private String channelName;
    private Long hourDau;
    private Long hourUserBettingCount;
    private Long hourRechargeCount;
    private Double hourRechargeAmount;
    private Long hourNewUsers;
    private Double hourBettingAmount;
    private Double hourDiffAmount;
    private Long dau;
    private Long userBettingCount;
    private Long rechargeCount;
    private Double rechargeAmount;
    private Long newUsers;
    private Double bettingAmount;
    private Double diffAmount;

    @ExcelField(title = "日期", type = 0, align = 1, sort = 10)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    @ExcelField(title = "小时", type = 0, align = 1, sort = 20)
    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    @ExcelField(title = "渠道", type = 0, align = 1, sort = 30)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @ExcelField(title = "渠道名称", type = 0, align = 1, sort = 40)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ExcelField(title = "DAU", type = 0, align = 1, sort = 50)
    public Long getHourDau() {
        return hourDau;
    }

    public void setHourDau(Long hourDau) {
        this.hourDau = hourDau;
    }

    @ExcelField(title = "投注人数", type = 0, align = 1, sort = 60)
    public Long getHourUserBettingCount() {
        return hourUserBettingCount;
    }

    public void setHourUserBettingCount(Long hourUserBettingCount) {
        this.hourUserBettingCount = hourUserBettingCount;
    }

    @ExcelField(title = "充值人数", type = 0, align = 1, sort = 70)
    public Long getHourRechargeCount() {
        return hourRechargeCount;
    }

    public void setHourRechargeCount(Long hourRechargeCount) {
        this.hourRechargeCount = hourRechargeCount;
    }

    @ExcelField(title = "充值金额", type = 0, align = 1, sort = 80)
    public Double getHourRechargeAmount() {
        return hourRechargeAmount;
    }

    public void setHourRechargeAmount(Double hourRechargeAmount) {
        this.hourRechargeAmount = hourRechargeAmount;
    }

    @ExcelField(title = "新增用户数", type = 0, align = 1, sort = 90)
    public Long getHourNewUsers() {
        return hourNewUsers;
    }

    public void setHourNewUsers(Long hourNewUsers) {
        this.hourNewUsers = hourNewUsers;
    }

    @ExcelField(title = "投注流水", type = 0, align = 1, sort = 100)
    public Double getHourBettingAmount() {
        return hourBettingAmount;
    }

    public void setHourBettingAmount(Double hourBettingAmount) {
        this.hourBettingAmount = hourBettingAmount;
    }

    @ExcelField(title = "流水差", type = 0, align = 1, sort = 110)
    public Double getHourDiffAmount() {
        return hourDiffAmount;
    }

    public void setHourDiffAmount(Double hourDiffAmount) {
        this.hourDiffAmount = hourDiffAmount;
    }

    @ExcelField(title = "累计DAU", type = 0, align = 1, sort = 120)
    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    @ExcelField(title = "累计投注人数", type = 0, align = 1, sort = 130)
    public Long getUserBettingCount() {
        return userBettingCount;
    }

    public void setUserBettingCount(Long userBettingCount) {
        this.userBettingCount = userBettingCount;
    }

    @ExcelField(title = "累计充值人数", type = 0, align = 1, sort = 140)
    public Long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    @ExcelField(title = "累计充值金额", type = 0, align = 1, sort = 150)
    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    @ExcelField(title = "累计新增用户数", type = 0, align = 1, sort = 160)
    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    @ExcelField(title = "累计投注流水", type = 0, align = 1, sort = 170)
    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    @ExcelField(title = "累计流水差", type = 0, align = 1, sort = 180)
    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }
}
