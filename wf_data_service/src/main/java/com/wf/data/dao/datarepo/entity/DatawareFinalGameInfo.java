package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

public class DatawareFinalGameInfo extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long parentId;
    private Long channelId;
    private Integer gameType;
    private Long dau;
    private Long bettingUserCount;
    private Double bettingConversion;
    private Double bettingAmount;
    private Double returnAmount;
    private Double diffAmount;
    private Double returnRate;
    private Long bettingCount;
    private Double bettingArpu;
    private Double bettingAsp;
    private Double oneDayRetention;
    private Double threeDayRetention;
    private Double sevenDayRetention;
    private Long newUserCount;
    private Long newUserBettingUserCount;
    private Double newUserBettingConversion;
    private Double newUserReturnAmount;
    private Double newUserBettingAmount;
    private Double newUserDiffAmount;
    private Double newUserReturnRate;
    private Long newUserBettingCount;
    private Double newUserBettingArpu;
    private Double newUserBettingAsp;
    private Double newUserOneDayRetention;
    private Double newUserThreeDayRetention;
    private Double newUserSevenDayRetention;
    private Double importRate;
    private Long totalUserCount;
    private String businessDate;

    private String parentIdName;
    private String gameName;


    public DatawareFinalGameInfo init() {
        DatawareFinalGameInfo info = new DatawareFinalGameInfo();
        info.setDau(0L);
        info.setBettingUserCount(0L);
        info.setBettingConversion(0.0);
        info.setBettingAmount(0.0);
        info.setReturnAmount(0.0);
        info.setDiffAmount(0.0);
        info.setReturnRate(0.0);
        info.setBettingCount(0L);
        info.setBettingArpu(0.0);
        info.setBettingAsp(0.0);
        info.setOneDayRetention(0.0);
        info.setThreeDayRetention(0.0);
        info.setSevenDayRetention(0.0);
        info.setNewUserCount(0L);
        info.setNewUserBettingUserCount(0L);
        info.setNewUserBettingConversion(0.0);
        info.setNewUserReturnAmount(0.0);
        info.setNewUserBettingAmount(0.0);
        info.setNewUserDiffAmount(0.0);
        info.setNewUserReturnRate(0.0);
        info.setNewUserBettingCount(0L);
        info.setNewUserBettingArpu(0.0);
        info.setNewUserBettingAsp(0.0);
        info.setNewUserOneDayRetention(0.0);
        info.setNewUserThreeDayRetention(0.0);
        info.setNewUserSevenDayRetention(0.0);
        info.setImportRate(0.0);
        info.setTotalUserCount(0L);


        return info;
    }

    @ExcelField(title = "渠道名称", type = 1, align = 2, sort = 2)
    public String getParentIdName() {
        return parentIdName;
    }

    public void setParentIdName(String parentIdName) {
        this.parentIdName = parentIdName;
    }

    @ExcelField(title = "游戏名称", type = 1, align = 2, sort = 3)
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    @ExcelField(title = "DAU", type = 1, align = 2, sort = 4)
    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    @ExcelField(title = "投注人数", type = 1, align = 2, sort = 5)
    public Long getBettingUserCount() {
        return bettingUserCount;
    }

    public void setBettingUserCount(Long bettingUserCount) {
        this.bettingUserCount = bettingUserCount;
    }

    @ExcelField(title = "投注转化率", type = 1, align = 2, sort = 6)
    public Double getBettingConversion() {
        return bettingConversion;
    }

    public void setBettingConversion(Double bettingConversion) {
        this.bettingConversion = bettingConversion;
    }

    @ExcelField(title = "投注流水", type = 1, align = 2, sort = 11)
    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    @ExcelField(title = "流水差", type = 1, align = 2, sort = 12)
    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    @ExcelField(title = "返奖率", type = 1, align = 2, sort = 13)
    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    @ExcelField(title = "投注笔数", type = 1, align = 2, sort = 14)
    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
    }

    @ExcelField(title = "投注ARPU", type = 1, align = 2, sort = 15)
    public Double getBettingArpu() {
        return bettingArpu;
    }

    public void setBettingArpu(Double bettingArpu) {
        this.bettingArpu = bettingArpu;
    }

    @ExcelField(title = "投注ASP", type = 1, align = 2, sort = 16)
    public Double getBettingAsp() {
        return bettingAsp;
    }

    public void setBettingAsp(Double bettingAsp) {
        this.bettingAsp = bettingAsp;
    }

    public Double getOneDayRetention() {
        return oneDayRetention;
    }

    public void setOneDayRetention(Double oneDayRetention) {
        this.oneDayRetention = oneDayRetention;
    }

    public Double getThreeDayRetention() {
        return threeDayRetention;
    }

    public void setThreeDayRetention(Double threeDayRetention) {
        this.threeDayRetention = threeDayRetention;
    }

    public Double getSevenDayRetention() {
        return sevenDayRetention;
    }

    public void setSevenDayRetention(Double sevenDayRetention) {
        this.sevenDayRetention = sevenDayRetention;
    }

    @ExcelField(title = "新增用户数", type = 1, align = 2, sort = 7)
    public Long getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Long newUserCount) {
        this.newUserCount = newUserCount;
    }

    @ExcelField(title = "新增用户投注人数", type = 1, align = 2, sort = 8)
    public Long getNewUserBettingUserCount() {
        return newUserBettingUserCount;
    }

    public void setNewUserBettingUserCount(Long newUserBettingUserCount) {
        this.newUserBettingUserCount = newUserBettingUserCount;
    }

    @ExcelField(title = "新增用户投注转化率", type = 1, align = 2, sort = 9)
    public Double getNewUserBettingConversion() {
        return newUserBettingConversion;
    }

    public void setNewUserBettingConversion(Double newUserBettingConversion) {
        this.newUserBettingConversion = newUserBettingConversion;
    }

    public Double getNewUserReturnAmount() {
        return newUserReturnAmount;
    }

    public void setNewUserReturnAmount(Double newUserReturnAmount) {
        this.newUserReturnAmount = newUserReturnAmount;
    }

    public Double getNewUserBettingAmount() {
        return newUserBettingAmount;
    }

    public void setNewUserBettingAmount(Double newUserBettingAmount) {
        this.newUserBettingAmount = newUserBettingAmount;
    }

    public Double getNewUserDiffAmount() {
        return newUserDiffAmount;
    }

    public void setNewUserDiffAmount(Double newUserDiffAmount) {
        this.newUserDiffAmount = newUserDiffAmount;
    }

    public Double getNewUserReturnRate() {
        return newUserReturnRate;
    }

    public void setNewUserReturnRate(Double newUserReturnRate) {
        this.newUserReturnRate = newUserReturnRate;
    }

    public Long getNewUserBettingCount() {
        return newUserBettingCount;
    }

    public void setNewUserBettingCount(Long newUserBettingCount) {
        this.newUserBettingCount = newUserBettingCount;
    }

    public Double getNewUserBettingArpu() {
        return newUserBettingArpu;
    }

    public void setNewUserBettingArpu(Double newUserBettingArpu) {
        this.newUserBettingArpu = newUserBettingArpu;
    }

    public Double getNewUserBettingAsp() {
        return newUserBettingAsp;
    }

    public void setNewUserBettingAsp(Double newUserBettingAsp) {
        this.newUserBettingAsp = newUserBettingAsp;
    }

    @ExcelField(title = "新增次留", type = 1, align = 2, sort = 10)
    public Double getNewUserOneDayRetention() {
        return newUserOneDayRetention;
    }

    public void setNewUserOneDayRetention(Double newUserOneDayRetention) {
        this.newUserOneDayRetention = newUserOneDayRetention;
    }

    public Double getNewUserThreeDayRetention() {
        return newUserThreeDayRetention;
    }

    public void setNewUserThreeDayRetention(Double newUserThreeDayRetention) {
        this.newUserThreeDayRetention = newUserThreeDayRetention;
    }

    public Double getNewUserSevenDayRetention() {
        return newUserSevenDayRetention;
    }

    public void setNewUserSevenDayRetention(Double newUserSevenDayRetention) {
        this.newUserSevenDayRetention = newUserSevenDayRetention;
    }

    @ExcelField(title = "导入率", type = 1, align = 2, sort = 17)
    public Double getImportRate() {
        return importRate;
    }

    public void setImportRate(Double importRate) {
        this.importRate = importRate;
    }

    @ExcelField(title = "累计用户数", type = 1, align = 2, sort = 18)
    public Long getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(Long totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    @ExcelField(title = "日期", type = 1, align = 2, sort = 1)
    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
}