package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalChannelInfoHour extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long hourDau;
    private Double hourRechargeAmount;
    private Long hourRechargeCount;
    private Long hourNewUsers;
    private Long hourUserBettingCount;
    private Long hourUserCount;
    private Long hourBettingCount;
    private Double hourBettingAmount;
    private Double hourResultAmount;
    private Double hourDiffAmount;
    private Long dau;
    private Double rechargeAmount;
    private Long rechargeCount;
    private Long newUsers;
    private Long userBettingCount;
    private Long userCount;
    private Long bettingCount;
    private Double bettingAmount;
    private Double resultAmount;
    private Double diffAmount;
    private String channelName;
    private Long parentId;
    private Long channelId;
    private String businessDate;
    private String businessHour;

    private String dayDauRate;
    private String weekDauRate;
    private String dayUserCountRate;
    private String weekUserCountRate;
    private String dayRechargeCountRate;
    private String weekRechargeCountRate;
    private String dayRechargeAmountRate;
    private String weekRechargeAmountRate;
    private String dayNewUsersRate;
    private String weekNewUsersRate;
    private String dayBettingAmountRate;
    private String weekBettingAmountRate;
    private String dayDiffAmountRate;
    private String weekDiffAmountRate;


    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    public Long getHourDau() {
        return hourDau;
    }

    public void setHourDau(Long hourDau) {
        this.hourDau = hourDau;
    }

    public Double getHourRechargeAmount() {
        return hourRechargeAmount;
    }

    public void setHourRechargeAmount(Double hourRechargeAmount) {
        this.hourRechargeAmount = hourRechargeAmount;
    }

    public Long getHourRechargeCount() {
        return hourRechargeCount;
    }

    public void setHourRechargeCount(Long hourRechargeCount) {
        this.hourRechargeCount = hourRechargeCount;
    }

    public Long getHourNewUsers() {
        return hourNewUsers;
    }

    public void setHourNewUsers(Long hourNewUsers) {
        this.hourNewUsers = hourNewUsers;
    }

    public Long getHourUserBettingCount() {
        return hourUserBettingCount;
    }

    public void setHourUserBettingCount(Long hourUserBettingCount) {
        this.hourUserBettingCount = hourUserBettingCount;
    }

    public Long getHourUserCount() {
        return hourUserCount;
    }

    public void setHourUserCount(Long hourUserCount) {
        this.hourUserCount = hourUserCount;
    }

    public Long getHourBettingCount() {
        return hourBettingCount;
    }

    public void setHourBettingCount(Long hourBettingCount) {
        this.hourBettingCount = hourBettingCount;
    }

    public Double getHourBettingAmount() {
        return hourBettingAmount;
    }

    public void setHourBettingAmount(Double hourBettingAmount) {
        this.hourBettingAmount = hourBettingAmount;
    }

    public Double getHourResultAmount() {
        return hourResultAmount;
    }

    public void setHourResultAmount(Double hourResultAmount) {
        this.hourResultAmount = hourResultAmount;
    }

    public Double getHourDiffAmount() {
        return hourDiffAmount;
    }

    public void setHourDiffAmount(Double hourDiffAmount) {
        this.hourDiffAmount = hourDiffAmount;
    }

    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    public Long getUserBettingCount() {
        return userBettingCount;
    }

    public void setUserBettingCount(Long userBettingCount) {
        this.userBettingCount = userBettingCount;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

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

    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getDayDauRate() {
        return dayDauRate;
    }

    public void setDayDauRate(String dayDauRate) {
        this.dayDauRate = dayDauRate;
    }

    public String getWeekDauRate() {
        return weekDauRate;
    }

    public void setWeekDauRate(String weekDauRate) {
        this.weekDauRate = weekDauRate;
    }

    public String getDayUserCountRate() {
        return dayUserCountRate;
    }

    public void setDayUserCountRate(String dayUserCountRate) {
        this.dayUserCountRate = dayUserCountRate;
    }

    public String getWeekUserCountRate() {
        return weekUserCountRate;
    }

    public void setWeekUserCountRate(String weekUserCountRate) {
        this.weekUserCountRate = weekUserCountRate;
    }

    public String getDayRechargeCountRate() {
        return dayRechargeCountRate;
    }

    public void setDayRechargeCountRate(String dayRechargeCountRate) {
        this.dayRechargeCountRate = dayRechargeCountRate;
    }

    public String getWeekRechargeCountRate() {
        return weekRechargeCountRate;
    }

    public void setWeekRechargeCountRate(String weekRechargeCountRate) {
        this.weekRechargeCountRate = weekRechargeCountRate;
    }

    public String getDayRechargeAmountRate() {
        return dayRechargeAmountRate;
    }

    public void setDayRechargeAmountRate(String dayRechargeAmountRate) {
        this.dayRechargeAmountRate = dayRechargeAmountRate;
    }

    public String getWeekRechargeAmountRate() {
        return weekRechargeAmountRate;
    }

    public void setWeekRechargeAmountRate(String weekRechargeAmountRate) {
        this.weekRechargeAmountRate = weekRechargeAmountRate;
    }

    public String getDayNewUsersRate() {
        return dayNewUsersRate;
    }

    public void setDayNewUsersRate(String dayNewUsersRate) {
        this.dayNewUsersRate = dayNewUsersRate;
    }

    public String getWeekNewUsersRate() {
        return weekNewUsersRate;
    }

    public void setWeekNewUsersRate(String weekNewUsersRate) {
        this.weekNewUsersRate = weekNewUsersRate;
    }

    public String getDayBettingAmountRate() {
        return dayBettingAmountRate;
    }

    public void setDayBettingAmountRate(String dayBettingAmountRate) {
        this.dayBettingAmountRate = dayBettingAmountRate;
    }

    public String getWeekBettingAmountRate() {
        return weekBettingAmountRate;
    }

    public void setWeekBettingAmountRate(String weekBettingAmountRate) {
        this.weekBettingAmountRate = weekBettingAmountRate;
    }

    public String getDayDiffAmountRate() {
        return dayDiffAmountRate;
    }

    public void setDayDiffAmountRate(String dayDiffAmountRate) {
        this.dayDiffAmountRate = dayDiffAmountRate;
    }

    public String getWeekDiffAmountRate() {
        return weekDiffAmountRate;
    }

    public void setWeekDiffAmountRate(String weekDiffAmountRate) {
        this.weekDiffAmountRate = weekDiffAmountRate;
    }
}