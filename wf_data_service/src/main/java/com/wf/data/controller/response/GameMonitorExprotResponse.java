package com.wf.data.controller.response;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

/**
 * @author shihui
 * @date 2018/3/28
 */
public class GameMonitorExprotResponse extends DataEntity {
    private static final long serialVersionUID = -1;

    private String businessDate;
    private String businessHour;
    private Long parentId;
    private String channelName;
    private String gameType;

    private Long hourDau;
    private Long hourUserBettingCount;
    private Long hourBettingCount;
    private Double hourBettingAmount;
    private Double hourDiffAmount;
    private String hourReturnRate;

    private Long dau;
    private Long userBettingCount;
    private Long bettingCount;
    private Double bettingAmount;
    private Double diffAmount;
    private String returnRate;

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

    @ExcelField(title = "渠道ID", type = 0, align = 1, sort = 30)
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

    @ExcelField(title = "游戏类型", type = 0, align = 1, sort = 50)
    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @ExcelField(title = "DAU", type = 0, align = 1, sort = 60)
    public Long getHourDau() {
        return hourDau;
    }

    public void setHourDau(Long hourDau) {
        this.hourDau = hourDau;
    }

    @ExcelField(title = "投注人数", type = 0, align = 1, sort = 70)
    public Long getHourUserBettingCount() {
        return hourUserBettingCount;
    }

    public void setHourUserBettingCount(Long hourUserBettingCount) {
        this.hourUserBettingCount = hourUserBettingCount;
    }

    @ExcelField(title = "投注笔数", type = 0, align = 1, sort = 80)
    public Long getHourBettingCount() {
        return hourBettingCount;
    }

    public void setHourBettingCount(Long hourBettingCount) {
        this.hourBettingCount = hourBettingCount;
    }

    @ExcelField(title = "投注流水", type = 0, align = 1, sort = 90)
    public Double getHourBettingAmount() {
        return hourBettingAmount;
    }

    public void setHourBettingAmount(Double hourBettingAmount) {
        this.hourBettingAmount = hourBettingAmount;
    }

    @ExcelField(title = "流水差", type = 0, align = 1, sort = 100)
    public Double getHourDiffAmount() {
        return hourDiffAmount;
    }

    public void setHourDiffAmount(Double hourDiffAmount) {
        this.hourDiffAmount = hourDiffAmount;
    }

    @ExcelField(title = "返奖率(%)", type = 0, align = 1, sort = 110)
    public String getHourReturnRate() {
        return hourReturnRate;
    }

    public void setHourReturnRate(String hourReturnRate) {
        this.hourReturnRate = hourReturnRate;
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

    @ExcelField(title = "累计投注笔数", type = 0, align = 1, sort = 140)
    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

    @ExcelField(title = "累计投注流水", type = 0, align = 1, sort = 150)
    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    @ExcelField(title = "累计流水差", type = 0, align = 1, sort = 160)
    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    @ExcelField(title = "累计返奖率(%)", type = 0, align = 1, sort = 170)
    public String getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
    }
}
