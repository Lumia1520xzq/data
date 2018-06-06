package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

public class DatawareFinalRechargeStatistic extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long parentId;
    private Long channelId;
    private String businessDate;
    private Long newRechargeCount;
    private Double newPayCovCycle;
    private Double rechargeRepRate;

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

    public Long getNewRechargeCount() {
        return newRechargeCount;
    }

    public void setNewRechargeCount(Long newRechargeCount) {
        this.newRechargeCount = newRechargeCount;
    }

    public Double getNewPayCovCycle() {
        return newPayCovCycle;
    }

    public void setNewPayCovCycle(Double newPayCovCycle) {
        this.newPayCovCycle = newPayCovCycle;
    }

    public Double getRechargeRepRate() {
        return rechargeRepRate;
    }

    public void setRechargeRepRate(Double rechargeRepRate) {
        this.rechargeRepRate = rechargeRepRate;
    }
}
