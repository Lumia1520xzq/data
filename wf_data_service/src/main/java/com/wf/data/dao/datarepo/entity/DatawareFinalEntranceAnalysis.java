package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getEntranceDau() {
        return entranceDau;
    }

    public void setEntranceDau(Long entranceDau) {
        this.entranceDau = entranceDau;
    }

    public Double getEntranceDauRate() {
        return entranceDauRate;
    }

    public void setEntranceDauRate(Double entranceDauRate) {
        this.entranceDauRate = entranceDauRate;
    }

    public Long getEntranceSign() {
        return entranceSign;
    }

    public void setEntranceSign(Long entranceSign) {
        this.entranceSign = entranceSign;
    }

    public Double getEntranceSignRate() {
        return entranceSignRate;
    }

    public void setEntranceSignRate(Double entranceSignRate) {
        this.entranceSignRate = entranceSignRate;
    }

    public Long getEntranceBetting() {
        return entranceBetting;
    }

    public void setEntranceBetting(Long entranceBetting) {
        this.entranceBetting = entranceBetting;
    }

    public Double getEntranceBettingRate() {
        return entranceBettingRate;
    }

    public void setEntranceBettingRate(Double entranceBettingRate) {
        this.entranceBettingRate = entranceBettingRate;
    }

    public Long getEntrancePay() {
        return entrancePay;
    }

    public void setEntrancePay(Long entrancePay) {
        this.entrancePay = entrancePay;
    }

    public Double getEntrancePayRate() {
        return entrancePayRate;
    }

    public void setEntrancePayRate(Double entrancePayRate) {
        this.entrancePayRate = entrancePayRate;
    }

    public Double getEntranceDayRetention() {
        return entranceDayRetention;
    }

    public void setEntranceDayRetention(Double entranceDayRetention) {
        this.entranceDayRetention = entranceDayRetention;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
}