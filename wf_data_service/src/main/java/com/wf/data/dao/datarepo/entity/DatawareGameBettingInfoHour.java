package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

public class DatawareGameBettingInfoHour extends DataEntity {
    private static final long serialVersionUID = -6363910951854025947L;
    private Long parentId;
    private Long channelId;
    private Integer gameType;

    private Long hourDau;
    private Long hourBettingUserCount;
    private Long hourBettingCount;
    private Double hourBettingAmount;
    private Double hourReturnAmount;
    private Double hourMoneyGap;
    private Double hourReturnRate;

    private Long dau;
    private Long bettingUserCount;
    private Long bettingCount;
    private Double bettingAmount;
    private Double returnAmount;
    private Double moneyGap;
    private Double returnRate;

    private String businessHour;
    private String businessDate;

    private String dayDauRate;
    private String dayUserCountRate;
    private String dayBettingCountRate;
    private String dayBettingAmountRate;
    private String dayMoneyGapRate;
    private String dayReturnRate;

    private String weekDauRate;
    private String weekUserCountRate;
    private String weekBettingCountRate;
    private String weekBettingAmountRate;
    private String weekMoneyGapRate;
    private String weekReturnRate;

    public void init(DatawareGameBettingInfoHour info){
        if (info == null){
            return;
        }
        info.setBettingAmount(0.0);
        info.setReturnAmount(0.0);
    }

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

    public String getDayDauRate() {
        return dayDauRate;
    }

    public void setDayDauRate(String dayDauRate) {
        this.dayDauRate = dayDauRate;
    }

    public String getDayUserCountRate() {
        return dayUserCountRate;
    }

    public void setDayUserCountRate(String dayUserCountRate) {
        this.dayUserCountRate = dayUserCountRate;
    }

    public String getDayBettingCountRate() {
        return dayBettingCountRate;
    }

    public void setDayBettingCountRate(String dayBettingCountRate) {
        this.dayBettingCountRate = dayBettingCountRate;
    }

    public String getDayBettingAmountRate() {
        return dayBettingAmountRate;
    }

    public void setDayBettingAmountRate(String dayBettingAmountRate) {
        this.dayBettingAmountRate = dayBettingAmountRate;
    }

    public String getDayMoneyGapRate() {
        return dayMoneyGapRate;
    }

    public void setDayMoneyGapRate(String dayMoneyGapRate) {
        this.dayMoneyGapRate = dayMoneyGapRate;
    }

    public String getDayReturnRate() {
        return dayReturnRate;
    }

    public void setDayReturnRate(String dayReturnRate) {
        this.dayReturnRate = dayReturnRate;
    }

    public String getWeekDauRate() {
        return weekDauRate;
    }

    public void setWeekDauRate(String weekDauRate) {
        this.weekDauRate = weekDauRate;
    }

    public String getWeekUserCountRate() {
        return weekUserCountRate;
    }

    public void setWeekUserCountRate(String weekUserCountRate) {
        this.weekUserCountRate = weekUserCountRate;
    }

    public String getWeekBettingCountRate() {
        return weekBettingCountRate;
    }

    public void setWeekBettingCountRate(String weekBettingCountRate) {
        this.weekBettingCountRate = weekBettingCountRate;
    }

    public String getWeekBettingAmountRate() {
        return weekBettingAmountRate;
    }

    public void setWeekBettingAmountRate(String weekBettingAmountRate) {
        this.weekBettingAmountRate = weekBettingAmountRate;
    }

    public String getWeekMoneyGapRate() {
        return weekMoneyGapRate;
    }

    public void setWeekMoneyGapRate(String weekMoneyGapRate) {
        this.weekMoneyGapRate = weekMoneyGapRate;
    }

    public String getWeekReturnRate() {
        return weekReturnRate;
    }

    public void setWeekReturnRate(String weekReturnRate) {
        this.weekReturnRate = weekReturnRate;
    }

    public Double getHourMoneyGap() {
        return hourMoneyGap;
    }

    public void setHourMoneyGap(Double hourMoneyGap) {
        this.hourMoneyGap = hourMoneyGap;
    }

    public Double getHourReturnRate() {
        return hourReturnRate;
    }

    public void setHourReturnRate(Double hourReturnRate) {
        this.hourReturnRate = hourReturnRate;
    }

    public Double getMoneyGap() {
        return moneyGap;
    }

    public void setMoneyGap(Double moneyGap) {
        this.moneyGap = moneyGap;
    }

    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }


}

