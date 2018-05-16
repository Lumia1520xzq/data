package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareFinalChannelCost extends DataEntity {
    private static final long serialVersionUID = -1;
    private Double rechargeAmount;
    private Double fragmentCost;
    private Double kindCost;
    private Double totalCost;
    private Double costRate;
    private String channelName;
    private Long parentId;
    private Long channelId;
    private String businessDate;
    private Long dau;
    private Long activityUsers;
    private Double activityRate;
    private Double avrActivityCost;
    private Integer activityType;
    private String activityName;

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Double getFragmentCost() {
        return fragmentCost;
    }

    public void setFragmentCost(Double fragmentCost) {
        this.fragmentCost = fragmentCost;
    }

    public Double getKindCost() {
        return kindCost;
    }

    public void setKindCost(Double kindCost) {
        this.kindCost = kindCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getCostRate() {
        return costRate;
    }

    public void setCostRate(Double costRate) {
        this.costRate = costRate;
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

    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    public Long getActivityUsers() {
        return activityUsers;
    }

    public void setActivityUsers(Long activityUsers) {
        this.activityUsers = activityUsers;
    }

    public Double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(Double activityRate) {
        this.activityRate = activityRate;
    }

    public Double getAvrActivityCost() {
        return avrActivityCost;
    }

    public void setAvrActivityCost(Double avrActivityCost) {
        this.avrActivityCost = avrActivityCost;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}