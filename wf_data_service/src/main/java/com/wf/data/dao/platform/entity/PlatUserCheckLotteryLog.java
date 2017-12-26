package com.wf.data.dao.platform.entity;


import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class PlatUserCheckLotteryLog extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Long configId;
    private Integer checkDay;
    private java.util.Date checkDate;
    private Integer activityDay;
    private Integer isCheck;
    private Integer type;
    private Integer isReceive;
    private java.util.Date receiveDate;
    private Long channelId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Integer getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(Integer checkDay) {
        this.checkDay = checkDay;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getActivityDay() {
        return activityDay;
    }

    public void setActivityDay(Integer activityDay) {
        this.activityDay = activityDay;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}