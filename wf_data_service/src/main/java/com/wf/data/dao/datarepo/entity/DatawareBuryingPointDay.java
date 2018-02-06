package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareBuryingPointDay extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Integer gameType;
    private String gameName;
    private Integer pointCount;
    private Integer userGroup;
    private Long channelId;
    private String buryingDate;
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

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
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

    public String getBuryingDate() {
        return buryingDate;
    }

    public void setBuryingDate(String buryingDate) {
        this.buryingDate = buryingDate;
    }
}