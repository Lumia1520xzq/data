package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TransChangeNote extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long accountId;
	private Integer changeType;
	private Double changeMoney;
	private Double changeBefore;
	private Double changeAfter;
	private Integer businessType;
	private Long businessId;
	private Double businessMoney;
	private String remark;
	private Long channelId;
	private String channelName;
	private Long doyoChannelId;
	//主渠道
	private Long parentId;
	//查询属性
	private String beginDate;
	private String endDate;
	private String userName;
	//查询属性
	private Double changeMoneyLow;
	private Double changeMoneyHigh;
	private String unionBusinessType;
	//导出属性
	private String businessTypeName;
	
	@NotNull
	@ExcelField(title="游戏用户ID", type = 1, align = 1, sort = 10)
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	public Double getChangeMoney() {
		return changeMoney;
	}
	public void setChangeMoney(Double changeMoney) {
		this.changeMoney = changeMoney;
	}
	public Double getChangeBefore() {
		return changeBefore;
	}
	public void setChangeBefore(Double changeBefore) {
		this.changeBefore = changeBefore;
	}
	public Double getChangeAfter() {
		return changeAfter;
	}
	public void setChangeAfter(Double changeAfter) {
		this.changeAfter = changeAfter;
	}
	
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	@NotNull
	@ExcelField(title="充值金额", type = 1, align = 1, sort = 40)
	public Double getBusinessMoney() {
		return businessMoney;
	}
	public void setBusinessMoney(Double businessMoney) {
		this.businessMoney = businessMoney;
	}
	
	@ExcelField(title="备注", type = 1, align = 1, sort = 50)
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
	
	@NotNull
	@ExcelField(title="用户昵称", type = 1, align = 1, sort = 20)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getChangeMoneyLow() {
		return changeMoneyLow;
	}
	public void setChangeMoneyLow(Double changeMoneyLow) {
		this.changeMoneyLow = changeMoneyLow;
	}
	public Double getChangeMoneyHigh() {
		return changeMoneyHigh;
	}
	public void setChangeMoneyHigh(Double changeMoneyHigh) {
		this.changeMoneyHigh = changeMoneyHigh;
	}
	public String getUnionBusinessType() {
		return unionBusinessType;
	}
	public void setUnionBusinessType(String unionBusinessType) {
		this.unionBusinessType = unionBusinessType;
	}
	
	
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Long getDoyoChannelId() {
		return doyoChannelId;
	}
	public void setDoyoChannelId(Long doyoChannelId) {
		this.doyoChannelId = doyoChannelId;
	}
	@NotNull
	@ExcelField(title="业务类型", type = 1, align = 1, sort = 30)
	public String getBusinessTypeName() {
		return businessTypeName;
	}
	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}
	
	@Override
	@ExcelField(title="充值时间", type = 1, align = 1, sort = 60)
	public Date getCreateTime() {
		return createTime;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	
}