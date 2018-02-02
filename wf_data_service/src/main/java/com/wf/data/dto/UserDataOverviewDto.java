package com.wf.data.dto;

import java.io.Serializable;

/**
 * @author shihui
 * @date 2018/2/2
 */
public class UserDataOverviewDto implements Serializable {
    private static final long serialVersionUID = -5143124113247873284L;

    private String businessDate;//日期
    private Long dau;//dau
    private Long bettingUserCount;//投注人数
    private Double bettingAmount;//投注金额
    private Long bettingCount;//投注笔数
    private Double bettingArpu;//投注ARPU
    private Double bettingAsp;//投注ASP
    private Double diffAmount;//流水差
    private Double resultAmount;//返奖金额
    private String returnRate;//返奖率
    private Long recharUserCount;//充值人数
    private Long rechargeCount;//充值笔数
    private Double rechargeAmount;//充值金额
    private Double rechargeArpu;//充值ARPU
    private Double rechargeArppu;//充值ARPPU
    private Long signedUserNum;//签到人数
    private String signedConversionRate;//签到转化率
    private String bettingConversionRate;//投注转化率
    private String dauPayConversionRate;//DAU付费转化率
    private String bettingPayConversionRate;//投注付费转化率
    private Long newUsersNum;//新增用户数


    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public Long getDau() {
        return dau;
    }

    public void setDau(Long dau) {
        this.dau = dau;
    }

    public Long getBettingUserCount() {
        return bettingUserCount;
    }

    public void setBettingUserCount(Long bettingUserCount) {
        this.bettingUserCount = bettingUserCount;
    }

    public Double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(Double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }

    public Long getBettingCount() {
        return bettingCount;
    }

    public void setBettingCount(Long bettingCount) {
        this.bettingCount = bettingCount;
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

    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    public Double getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(Double resultAmount) {
        this.resultAmount = resultAmount;
    }

    public String getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
    }

    public Long getRecharUserCount() {
        return recharUserCount;
    }

    public void setRecharUserCount(Long recharUserCount) {
        this.recharUserCount = recharUserCount;
    }

    public Long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Double getRechargeArpu() {
        return rechargeArpu;
    }

    public void setRechargeArpu(Double rechargeArpu) {
        this.rechargeArpu = rechargeArpu;
    }

    public Double getRechargeArppu() {
        return rechargeArppu;
    }

    public void setRechargeArppu(Double rechargeArppu) {
        this.rechargeArppu = rechargeArppu;
    }

    public Long getSignedUserNum() {
        return signedUserNum;
    }

    public void setSignedUserNum(Long signedUserNum) {
        this.signedUserNum = signedUserNum;
    }

    public String getSignedConversionRate() {
        return signedConversionRate;
    }

    public void setSignedConversionRate(String signedConversionRate) {
        this.signedConversionRate = signedConversionRate;
    }

    public String getBettingConversionRate() {
        return bettingConversionRate;
    }

    public void setBettingConversionRate(String bettingConversionRate) {
        this.bettingConversionRate = bettingConversionRate;
    }

    public String getDauPayConversionRate() {
        return dauPayConversionRate;
    }

    public void setDauPayConversionRate(String dauPayConversionRate) {
        this.dauPayConversionRate = dauPayConversionRate;
    }

    public String getBettingPayConversionRate() {
        return bettingPayConversionRate;
    }

    public void setBettingPayConversionRate(String bettingPayConversionRate) {
        this.bettingPayConversionRate = bettingPayConversionRate;
    }

    public Long getNewUsersNum() {
        return newUsersNum;
    }

    public void setNewUsersNum(Long newUsersNum) {
        this.newUsersNum = newUsersNum;
    }
}
