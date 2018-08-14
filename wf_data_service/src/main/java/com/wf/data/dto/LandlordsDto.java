package com.wf.data.dto;

import java.io.Serializable;

/**
 * 斗地主-场次分析
 */
public class LandlordsDto implements Serializable {

    private static final long serialVersionUID = -1;

    private Long parentId;
    private Long channelId;
    private String startTime;
    private String endTime;

    //日期
    private String searchDate;
    //场次ID
    private int deskType;
    //场次名称
    private String deskTypeName;
    //DAU
    private Integer dauCount;
    //投注人数
    private Integer userCount;
    //投注转化率
    private String conversionRate;
    //返奖流水
    private Double bettingAmount;
    private Double resultAmount;
    //道具
    private Double toolsAmount;
    //桌费
    private Double tableAmount;
    //返奖率
    private String returnRate;
    //投注ARPU
    private Double bettingArpu;
    //流水差ARPU
    private Double amountDiffArpu;
    //局数
    private Integer gameTimes;
    //人均局数
    private Double avgGameTimes;
    //投注ASP
    private Double bettingAsp;

    public Double getToolsAmount() {
        return toolsAmount;
    }

    public void setToolsAmount(Double toolsAmount) {
        this.toolsAmount = toolsAmount;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public int getDeskType() {
        return deskType;
    }

    public void setDeskType(int deskType) {
        this.deskType = deskType;
    }

    public String getDeskTypeName() {
        return deskTypeName;
    }

    public void setDeskTypeName(String deskTypeName) {
        this.deskTypeName = deskTypeName;
    }

    public Integer getDauCount() {
        return dauCount;
    }

    public void setDauCount(Integer dauCount) {
        this.dauCount = dauCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
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

    public Double getTableAmount() {
        return tableAmount;
    }

    public void setTableAmount(Double tableAmount) {
        this.tableAmount = tableAmount;
    }

    public String getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
    }

    public Double getBettingArpu() {
        return bettingArpu;
    }

    public void setBettingArpu(Double bettingArpu) {
        this.bettingArpu = bettingArpu;
    }

    public Double getAmountDiffArpu() {
        return amountDiffArpu;
    }

    public void setAmountDiffArpu(Double amountDiffArpu) {
        this.amountDiffArpu = amountDiffArpu;
    }

    public Integer getGameTimes() {
        return gameTimes;
    }

    public void setGameTimes(Integer gameTimes) {
        this.gameTimes = gameTimes;
    }

    public Double getAvgGameTimes() {
        return avgGameTimes;
    }

    public void setAvgGameTimes(Double avgGameTimes) {
        this.avgGameTimes = avgGameTimes;
    }

    public Double getBettingAsp() {
        return bettingAsp;
    }

    public void setBettingAsp(Double bettingAsp) {
        this.bettingAsp = bettingAsp;
    }
}