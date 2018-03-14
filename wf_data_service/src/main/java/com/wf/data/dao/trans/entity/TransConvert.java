package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class TransConvert extends DataEntity {

    private Long userId;
    private String orderSn;
    private Double amount;
    private String thirdOrderSn;
    private Double thirdAmount;
    private Integer source;
    private Integer payType;
    private Integer payMethod;
    private Integer status;
    private String merchantCode;
    private Integer auditVersion;
    private Long channelId;
    private Integer gameId;
    private Integer notifyUrl;
    private String gameProp;
    private Integer bizType;
    private String requestOrderId;
    private Integer requestOrderNotifyStatus;
    private Date successTime;
    private String tradeType;

    private Long bizTarget;
    private String ip;

    // 管理平台
    private String startTime;
    private String endTime;
    private Long parentId;
    private String userName;


    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(Integer notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getGameProp() {
        return gameProp;
    }

    public void setGameProp(String gameProp) {
        this.gameProp = gameProp;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Long getBizTarget() {
        return bizTarget;
    }

    public void setBizTarget(Long bizTarget) {
        this.bizTarget = bizTarget;
    }

    public Integer getAuditVersion() {
        return auditVersion;
    }

    public void setAuditVersion(Integer auditVersion) {
        this.auditVersion = auditVersion;
    }

    //是否为沙盒测试订单
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getThirdOrderSn() {
        return thirdOrderSn;
    }

    public void setThirdOrderSn(String thirdOrderSn) {
        this.thirdOrderSn = thirdOrderSn;
    }

    public Double getThirdAmount() {
        return thirdAmount;
    }

    public void setThirdAmount(Double thirdAmount) {
        this.thirdAmount = thirdAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getRequestOrderId() {
        return requestOrderId;
    }

    public void setRequestOrderId(String requestOrderId) {
        this.requestOrderId = requestOrderId;
    }

    public Integer getRequestOrderNotifyStatus() {
        return requestOrderNotifyStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRequestOrderNotifyStatus(Integer requestOrderNotifyStatus) {
        this.requestOrderNotifyStatus = requestOrderNotifyStatus;
    }
}