package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

public class DatawareFinalEntranceAnalysis extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long eventId;
    private String eventName;
    private Long entranceDau;
    private Double entranceDauRate;
    private Long entranceSign;
    private Double entranceSignRate;
    private Long entranceBetting;
    private Double entranceBettingRate;
    private Long entrancePay;
    private Double entrancePayRate;
    private Double entranceDayRetention;
    private String businessDate;
    private Integer activeUserType;
    private Integer convertUserType;

    @ExcelField(title = "埋点ID", type = 1, align = 2, sort = 3)
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @ExcelField(title = "埋点名称", type = 1, align = 2, sort = 2)
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @ExcelField(title = "入口活跃用户数(DAU)", type = 1, align = 2, sort = 4)
    public Long getEntranceDau() {
        return entranceDau;
    }

    public void setEntranceDau(Long entranceDau) {
        this.entranceDau = entranceDau;
    }

    @ExcelField(title = "入口DAU占比", type = 1, align = 2, sort = 5)
    public Double getEntranceDauRate() {
        return entranceDauRate;
    }

    public void setEntranceDauRate(Double entranceDauRate) {
        this.entranceDauRate = entranceDauRate;
    }

    @ExcelField(title = "入口签到人数", type = 1, align = 2, sort = 6)
    public Long getEntranceSign() {
        return entranceSign;
    }

    public void setEntranceSign(Long entranceSign) {
        this.entranceSign = entranceSign;
    }

    @ExcelField(title = "入口签到转化率", type = 1, align = 2, sort = 7)
    public Double getEntranceSignRate() {
        return entranceSignRate;
    }

    public void setEntranceSignRate(Double entranceSignRate) {
        this.entranceSignRate = entranceSignRate;
    }

    @ExcelField(title = "入口投注人数", type = 1, align = 2, sort = 8)
    public Long getEntranceBetting() {
        return entranceBetting;
    }

    public void setEntranceBetting(Long entranceBetting) {
        this.entranceBetting = entranceBetting;
    }

    @ExcelField(title = "入口投注转化率", type = 1, align = 2, sort = 9)
    public Double getEntranceBettingRate() {
        return entranceBettingRate;
    }

    public void setEntranceBettingRate(Double entranceBettingRate) {
        this.entranceBettingRate = entranceBettingRate;
    }

    @ExcelField(title = "入口付费人数", type = 1, align = 2, sort = 10)
    public Long getEntrancePay() {
        return entrancePay;
    }

    public void setEntrancePay(Long entrancePay) {
        this.entrancePay = entrancePay;
    }

    @ExcelField(title = "入口付费渗透率", type = 1, align = 2, sort = 11)
    public Double getEntrancePayRate() {
        return entrancePayRate;
    }

    public void setEntrancePayRate(Double entrancePayRate) {
        this.entrancePayRate = entrancePayRate;
    }

    @ExcelField(title = "入口次日留存率", type = 1, align = 2, sort = 12)
    public Double getEntranceDayRetention() {
        return entranceDayRetention;
    }

    public void setEntranceDayRetention(Double entranceDayRetention) {
        this.entranceDayRetention = entranceDayRetention;
    }

    @ExcelField(title = "日期", type = 1, align = 2, sort = 1)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public Integer getActiveUserType() {
        return activeUserType;
    }

    public void setActiveUserType(Integer activeUserType) {
        this.activeUserType = activeUserType;
    }

    public Integer getConvertUserType() {
        return convertUserType;
    }

    public void setConvertUserType(Integer convertUserType) {
        this.convertUserType = convertUserType;
    }
}