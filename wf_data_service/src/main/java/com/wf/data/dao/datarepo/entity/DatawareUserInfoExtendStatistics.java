package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class DatawareUserInfoExtendStatistics extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Double totalBettingAmount;
    private Double totalResultAmount;
    private Double totalDiffAmount;
    private Long totalBettingCount;
    private Double averageBettingAmount;
    private Double profitAmount;
    private Double totalWarsProfit;
    private Double totalWarsBetting;
    private Integer rechargeType;
    private Integer rechargeTypeEve;
    private String firstRechargeTime;
    private String lastRechargeTime;
    private Double totalRechargeAmount;
    private Long totalRechargeCount;
    private Double averageRechargeAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalBettingAmount() {
        return totalBettingAmount;
    }

    public void setTotalBettingAmount(Double totalBettingAmount) {
        this.totalBettingAmount = totalBettingAmount;
    }

    public Double getTotalResultAmount() {
        return totalResultAmount;
    }

    public void setTotalResultAmount(Double totalResultAmount) {
        this.totalResultAmount = totalResultAmount;
    }

    public Double getTotalDiffAmount() {
        return totalDiffAmount;
    }

    public void setTotalDiffAmount(Double totalDiffAmount) {
        this.totalDiffAmount = totalDiffAmount;
    }

    public Long getTotalBettingCount() {
        return totalBettingCount;
    }

    public void setTotalBettingCount(Long totalBettingCount) {
        this.totalBettingCount = totalBettingCount;
    }

    public Double getAverageBettingAmount() {
        return averageBettingAmount;
    }

    public void setAverageBettingAmount(Double averageBettingAmount) {
        this.averageBettingAmount = averageBettingAmount;
    }

    public Double getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(Double profitAmount) {
        this.profitAmount = profitAmount;
    }

    public Double getTotalWarsProfit() {
        return totalWarsProfit;
    }

    public void setTotalWarsProfit(Double totalWarsProfit) {
        this.totalWarsProfit = totalWarsProfit;
    }

    public Double getTotalWarsBetting() {
        return totalWarsBetting;
    }

    public void setTotalWarsBetting(Double totalWarsBetting) {
        this.totalWarsBetting = totalWarsBetting;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Integer getRechargeTypeEve() {
        return rechargeTypeEve;
    }

    public void setRechargeTypeEve(Integer rechargeTypeEve) {
        this.rechargeTypeEve = rechargeTypeEve;
    }

    public String getFirstRechargeTime() {
        return firstRechargeTime;
    }

    public void setFirstRechargeTime(String firstRechargeTime) {
        this.firstRechargeTime = firstRechargeTime;
    }

    public String getLastRechargeTime() {
        return lastRechargeTime;
    }

    public void setLastRechargeTime(String lastRechargeTime) {
        this.lastRechargeTime = lastRechargeTime;
    }

    public Double getTotalRechargeAmount() {
        return totalRechargeAmount;
    }

    public void setTotalRechargeAmount(Double totalRechargeAmount) {
        this.totalRechargeAmount = totalRechargeAmount;
    }

    public Long getTotalRechargeCount() {
        return totalRechargeCount;
    }

    public void setTotalRechargeCount(Long totalRechargeCount) {
        this.totalRechargeCount = totalRechargeCount;
    }

    public Double getAverageRechargeAmount() {
        return averageRechargeAmount;
    }

    public void setAverageRechargeAmount(Double averageRechargeAmount) {
        this.averageRechargeAmount = averageRechargeAmount;
    }

}