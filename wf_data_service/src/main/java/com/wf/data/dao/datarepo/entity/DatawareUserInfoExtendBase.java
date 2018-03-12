package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class DatawareUserInfoExtendBase extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Long channelId;
    private Long parentId;
    private Date registeredTime;
    private Integer newUserFlag;
    private Integer userGroup;
    private Double noUseGoldAmount;
    private Double costAmount;
    private String firstActiveDate;
    private String lastActiveDate;
    private Integer activeDates;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }

    public Integer getNewUserFlag() {
        return newUserFlag;
    }

    public void setNewUserFlag(Integer newUserFlag) {
        this.newUserFlag = newUserFlag;
    }

    public Integer getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Integer userGroup) {
        this.userGroup = userGroup;
    }

    public Double getNoUseGoldAmount() {
        return noUseGoldAmount;
    }

    public void setNoUseGoldAmount(Double noUseGoldAmount) {
        this.noUseGoldAmount = noUseGoldAmount;
    }

    public Double getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(Double costAmount) {
        this.costAmount = costAmount;
    }

    public String getFirstActiveDate() {
        return firstActiveDate;
    }

    public void setFirstActiveDate(String firstActiveDate) {
        this.firstActiveDate = firstActiveDate;
    }

    public String getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(String lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public Integer getActiveDates() {
        return activeDates;
    }

    public void setActiveDates(Integer activeDates) {
        this.activeDates = activeDates;
    }
}