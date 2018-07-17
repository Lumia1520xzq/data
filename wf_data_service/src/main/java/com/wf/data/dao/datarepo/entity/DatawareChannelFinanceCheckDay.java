package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

/**
 * 金叶子流转数据
 */
public class DatawareChannelFinanceCheckDay extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Long channelId;
    private Double syceeAmountRmb;
    private Double rechargeAmountRmb;
    private Double rechargePresentedAmount;
    private Double otherwaysGoldAmount;
    private Double consumeAmount;
    private Double returnAmount;
    private Double diffAmount;
    private Double surplusAmount;
    private String businessDate;

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

    public Double getSyceeAmountRmb() {
        return syceeAmountRmb;
    }

    public void setSyceeAmountRmb(Double syceeAmountRmb) {
        this.syceeAmountRmb = syceeAmountRmb;
    }

    public Double getRechargeAmountRmb() {
        return rechargeAmountRmb;
    }

    public void setRechargeAmountRmb(Double rechargeAmountRmb) {
        this.rechargeAmountRmb = rechargeAmountRmb;
    }

    public Double getRechargePresentedAmount() {
        return rechargePresentedAmount;
    }

    public void setRechargePresentedAmount(Double rechargePresentedAmount) {
        this.rechargePresentedAmount = rechargePresentedAmount;
    }

    public Double getOtherwaysGoldAmount() {
        return otherwaysGoldAmount;
    }

    public void setOtherwaysGoldAmount(Double otherwaysGoldAmount) {
        this.otherwaysGoldAmount = otherwaysGoldAmount;
    }

    public Double getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(Double consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
}
