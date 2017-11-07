package com.wf.data.rpc.dto;

import java.io.Serializable;
import java.util.Date;

public class BuryingPointDto implements Serializable {
    private Long userId;
    private Integer buryingType;    //埋点类型
    private Integer gameType;
    private Long channelId;
    private Integer countNum;

    protected Date createTime;    // 创建日期
    protected Date updateTime;    // 更新日期
    protected int deleteFlag;    // 删除标记（0：正常；1：删除；2：审核）

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getBuryingType() {
        return buryingType;
    }

    public void setBuryingType(Integer buryingType) {
        this.buryingType = buryingType;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "BuryingPointDto{" +
                "userId=" + userId +
                ", buryingType=" + buryingType +
                ", gameType=" + gameType +
                ", channelId=" + channelId +
                ", countNum=" + countNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}