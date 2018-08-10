package com.wf.data.rpc.dto;


import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

public class DatawareThirdBettingRecordDto implements Serializable {
    private static final long serialVersionUID = -1;
    private Long userCount;
    private Long bettingCount;
    private Double bettingAmount;
    private Double resultAmount;
    private Double resultRate;
    private Double bettingArpu;
    private Double bettingAsp;
    private Integer gameType;
    private String channelName;
    private Long channelId;
    private String businessDate;

    private Long dau;

    /**
     * 新增次留
     */
    private Double newUserSecondRetention;
    /**
     * 全量次留
     */
    private Double allSecondRetention;
    /**
     * 新增三日次留
     */
    private Double newUserThreeDayRetention;
    /**
     * 全量三日次留
     */
    private Double threeDayRetention;

    private Date createTime;    // 创建日期
    private Date updateTime;    // 更新日期
    private int deleteFlag;    // 删除标记（0：正常；1：删除；2：审核）


    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
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

    public Double getResultRate() {
        return resultRate;
    }

    public void setResultRate(Double resultRate) {
        this.resultRate = resultRate;
    }

    public Double getBettingArpu() {
        return bettingArpu;
    }

    public void setBettingArpu(Double bettingArpu) {
        this.bettingArpu = bettingArpu;
    }

    public Double getBettingAsp() {
        return bettingAsp;
    }

    public void setBettingAsp(Double bettingAsp) {
        this.bettingAsp = bettingAsp;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public static DatawareThirdBettingRecordDto toDto(Object object) {
        if (object == null) {
            return null;
        }
        DatawareThirdBettingRecordDto dto = new DatawareThirdBettingRecordDto();
        BeanUtils.copyProperties(object, dto);
        return dto;
    }

    public Double getNewUserSecondRetention() {
        return newUserSecondRetention;
    }

    public void setNewUserSecondRetention(Double newUserSecondRetention) {
        this.newUserSecondRetention = newUserSecondRetention;
    }

    public Double getAllSecondRetention() {
        return allSecondRetention;
    }

    public void setAllSecondRetention(Double allSecondRetention) {
        this.allSecondRetention = allSecondRetention;
    }

    public Double getNewUserThreeDayRetention() {
        return newUserThreeDayRetention;
    }

    public void setNewUserThreeDayRetention(Double newUserThreeDayRetention) {
        this.newUserThreeDayRetention = newUserThreeDayRetention;
    }

    public Double getThreeDayRetention() {
        return threeDayRetention;
    }

    public void setThreeDayRetention(Double threeDayRetention) {
        this.threeDayRetention = threeDayRetention;
    }
}