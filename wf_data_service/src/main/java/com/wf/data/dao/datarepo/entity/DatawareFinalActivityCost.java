package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

public class DatawareFinalActivityCost extends DataEntity {
    private static final long serialVersionUID = -1;
    private Double kindCost;
    private Double totalCost;
    // 成本份额占比
    private Double costRate;
    private int activityType;
    private String activityName;
    // 出口人数
    private Long activityUsers;
    // 出口人数份额占比
    private Double activityUserRate;
    // 人均出口成本
    private Double avrActivityCost;
    private String businessDate;
    private Long parentId;
    private String channelName;

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

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getActivityUsers() {
        return activityUsers;
    }

    public void setActivityUsers(Long activityUsers) {
        this.activityUsers = activityUsers;
    }

    public Double getActivityUserRate() {
        return activityUserRate;
    }

    public void setActivityUserRate(Double activityUserRate) {
        this.activityUserRate = activityUserRate;
    }

    public Double getAvrActivityCost() {
        return avrActivityCost;
    }

    public void setAvrActivityCost(Double avrActivityCost) {
        this.avrActivityCost = avrActivityCost;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
