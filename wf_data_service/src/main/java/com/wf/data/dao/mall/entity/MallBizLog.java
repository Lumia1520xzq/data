package com.wf.data.dao.mall.entity;


import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 购买记录
 */
public class MallBizLog extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;            //用户ID
    private Long bizId;                //购买ID
    private Long productId;            //商品ID
    private Long productCode;        //商品编号
    private Integer productType;    //商品类型
    private String productName;        //商品名称
    private Double productPrice;    //商品价格
    private Integer productAmount;    //金叶子数
    private Long convertId;            //订单编码
    private Integer status;            //订单购买状态
    private Long regChannelId;      //注册渠道
    private Long channelId;            //渠道
    private Integer version;        //版本
    private String remark;        //订单描述
    //params
    private String beginDate;
    private String endDate;
    private Long parentId;
    private String userName;
    protected Date createTime;    // 创建日期

    @NotNull
    @ExcelField(title = "充值时间", type = 1, align = 1, sort = 40)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @NotNull
    @ExcelField(title = "用户昵称", type = 1, align = 1, sort = 10)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRegChannelId() {
        return regChannelId;
    }

    public void setRegChannelId(Long regChannelId) {
        this.regChannelId = regChannelId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @NotNull
    @ExcelField(title = "游戏用户ID", type = 1, align = 1, sort = 20)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @NotNull
    @ExcelField(title = "充值金额", type = 1, align = 1, sort = 30)
    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Long getConvertId() {
        return convertId;
    }

    public void setConvertId(Long convertId) {
        this.convertId = convertId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


}