package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class DatawareUserSignDay extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private java.util.Date checkDate;
    private Integer userGroup;
    private Long channelId;
    private String signHour;
    private String signDate;
    private Long parentId;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Integer userGroup) {
        this.userGroup = userGroup;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getSignHour() {
        return signHour;
    }

    public void setSignHour(String signHour) {
        this.signHour = signHour;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
}