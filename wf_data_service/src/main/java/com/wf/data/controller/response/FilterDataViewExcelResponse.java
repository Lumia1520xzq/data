package com.wf.data.controller.response;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

/**
 * @author shihui
 * @date 2018/3/22
 */
public class FilterDataViewExcelResponse extends DataEntity {
    private static final long serialVersionUID = -1;
    private String businessDate;

    //全量用户(dau、进入游戏、投注、充值)
    private Long dauCount;
    private Long gamedauCount;
    private Long bettingCount;
    private Long rechargeCount;

    //新用户(注册、DAU、进入游戏、投注、充值)
    private Long registeredCount;
    private Long dauRegistered;
    private Long gamedauRegistered;
    private Long bettingRegistered;
    private Long rechargeRegistered;

    //老用户(dau、进入游戏、投注、充值)
    private Long dauOlder;
    private Long gamedauOlder;
    private Long bettingOlder;
    private Long rechargeOlder;

    private Long channelId;
    private String channelName;

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

    @ExcelField(title = "dau(全量用户)", type = 0, align = 1, sort = 40)
    public Long getDauCount() {
        return dauCount;
    }

    public void setDauCount(Long dauCount) {
        this.dauCount = dauCount;
    }

    @ExcelField(title = "进入游戏(全量用户)", type = 0, align = 1, sort = 50)
    public Long getGamedauCount() {
        return gamedauCount;
    }

    public void setGamedauCount(Long gamedauCount) {
        this.gamedauCount = gamedauCount;
    }

    @ExcelField(title = "投注(全量用户)", type = 0, align = 1, sort = 60)
    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

    @ExcelField(title = "充值(全量用户)", type = 0, align = 1, sort = 70)
    public Long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    @ExcelField(title = "注册(新用户)", type = 0, align = 1, sort = 80)
    public Long getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(Long registeredCount) {
        this.registeredCount = registeredCount;
    }

    @ExcelField(title = "dau(新用户)", type = 0, align = 1, sort = 90)
    public Long getDauRegistered() {
        return dauRegistered;
    }

    public void setDauRegistered(Long dauRegistered) {
        this.dauRegistered = dauRegistered;
    }

    @ExcelField(title = "进入游戏(新用户)", type = 0, align = 1, sort = 100)
    public Long getGamedauRegistered() {
        return gamedauRegistered;
    }

    public void setGamedauRegistered(Long gamedauRegistered) {
        this.gamedauRegistered = gamedauRegistered;
    }

    @ExcelField(title = "投注(新用户)", type = 0, align = 1, sort = 110)
    public Long getBettingRegistered() {
        return bettingRegistered;
    }

    public void setBettingRegistered(Long bettingRegistered) {
        this.bettingRegistered = bettingRegistered;
    }

    @ExcelField(title = "充值(新用户)", type = 0, align = 1, sort = 120)
    public Long getRechargeRegistered() {
        return rechargeRegistered;
    }

    public void setRechargeRegistered(Long rechargeRegistered) {
        this.rechargeRegistered = rechargeRegistered;
    }

    @ExcelField(title = "dau(老用户)", type = 0, align = 1, sort = 130)
    public Long getDauOlder() {
        return dauOlder;
    }

    public void setDauOlder(Long dauOlder) {
        this.dauOlder = dauOlder;
    }

    @ExcelField(title = "进入游戏(老用户)", type = 0, align = 1, sort = 140)
    public Long getGamedauOlder() {
        return gamedauOlder;
    }

    public void setGamedauOlder(Long gamedauOlder) {
        this.gamedauOlder = gamedauOlder;
    }

    @ExcelField(title = "投注(老用户)", type = 0, align = 1, sort = 150)
    public Long getBettingOlder() {
        return bettingOlder;
    }

    public void setBettingOlder(Long bettingOlder) {
        this.bettingOlder = bettingOlder;
    }

    @ExcelField(title = "充值(老用户)", type = 0, align = 1, sort = 160)
    public Long getRechargeOlder() {
        return rechargeOlder;
    }

    public void setRechargeOlder(Long rechargeOlder) {
        this.rechargeOlder = rechargeOlder;
    }
}
