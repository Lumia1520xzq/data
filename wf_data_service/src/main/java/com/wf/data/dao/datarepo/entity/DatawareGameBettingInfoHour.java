package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

public class DatawareGameBettingInfoHour extends DataEntity {
    private static final long serialVersionUID = -1l;
    private Long parentId;
    private Long channelId;
    private Integer gameType;
    private Long hourDau;
    private Long dau;
    private Double hourBettingAmount;
    private Double bettingAmount;
    private Long hourBettingCount;
    private Long bettingCount;
    private Long hourBettingUserCount;
    private Long bettingUserCount;
    private Double hourReturnAmount;
    private Double returnAmount;
    private String businessHour;
    private String businessDate;

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long value) {
        this.parentId = value;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long value) {
        this.channelId = value;
    }

    public Integer getGameType() {
        return this.gameType;
    }

    public void setGameType(Integer value) {
        this.gameType = value;
    }

    public Long getHourDau() {
        return this.hourDau;
    }

    public void setHourDau(Long value) {
        this.hourDau = value;
    }

    public Long getDau() {
        return this.dau;
    }

    public void setDau(Long value) {
        this.dau = value;
    }

    public Double getHourBettingAmount() {
        return this.hourBettingAmount;
    }

    public void setHourBettingAmount(Double value) {
        this.hourBettingAmount = value;
    }

    public Long getHourBettingCount() {
        return this.hourBettingCount;
    }

    public void setHourBettingCount(Long value) {
        this.hourBettingCount = value;
    }

    public Long getHourBettingUserCount() {
        return this.hourBettingUserCount;
    }

    public void setHourBettingUserCount(Long value) {
        this.hourBettingUserCount = value;
    }

    public Long getBettingUserCount() {
        return this.bettingUserCount;
    }

    public void setBettingUserCount(Long value) {
        this.bettingUserCount = value;
    }

    public Double getHourReturnAmount() {
        return this.hourReturnAmount;
    }

    public void setHourReturnAmount(Double value) {
        this.hourReturnAmount = value;
    }

    public String getBusinessHour() {
        return this.businessHour;
    }

    public void setBusinessHour(String value) {
        this.businessHour = value;
    }

    public String getBusinessDate() {
        return this.businessDate;
    }

    public void setBusinessDate(String value) {
        this.businessDate = value;
    }

    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }
}

