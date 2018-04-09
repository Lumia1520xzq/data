package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;

/**
 * 月度指标监控dto
 * @author JoeH
 */
public class MonthlyDataDto implements Serializable {

	private static final long serialVersionUID = 106633587603934140L;

	private String month;
	private Double sumRecharge;
	private Long sumDau;
	private Long avgDau;
	private Integer days;
	private Double avgDarpu;
	private Double sumCost;
	private Double costRate;
	private Long sumNewUsers;
	private Long avgNewUsers;
	private Long parentId;
	private String channelName;

	@ExcelField(title = "日期", type = 0, align = 1, sort = 10)
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@ExcelField(title = "渠道", type = 0, align = 1, sort = 20)
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@ExcelField(title = "渠道名称", type = 0, align = 1, sort = 30)
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@ExcelField(title = "当月累计充值", type = 0, align = 1, sort = 40)
	public Double getSumRecharge() {
		return sumRecharge;
	}

	public void setSumRecharge(Double sumRecharge) {
		this.sumRecharge = sumRecharge;
	}

	public Long getSumDau() {
		return sumDau;
	}

	public void setSumDau(Long sumDau) {
		this.sumDau = sumDau;
	}

	@ExcelField(title = "当月累计DAU日均值", type = 0, align = 1, sort = 50)
	public Long getAvgDau() {
		return avgDau;
	}

	public void setAvgDau(Long avgDau) {
		this.avgDau = avgDau;
	}

	@ExcelField(title = "当月累计DARPU日均值", type = 0, align = 1, sort = 70)
	public Double getAvgDarpu() {
		return avgDarpu;
	}

	public void setAvgDarpu(Double avgDarpu) {
		this.avgDarpu = avgDarpu;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	@ExcelField(title = "当月累计成本", type = 0, align = 1, sort = 80)
	public Double getSumCost() {
		return sumCost;
	}

	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}

	@ExcelField(title = "当月累计成本占比", type = 0, align = 1, sort = 90)
	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}

	@ExcelField(title = "当月累计新增用户日均值", type = 0, align = 1, sort = 60)
	public Long getAvgNewUsers() {
		return avgNewUsers;
	}

	public void setAvgNewUsers(Long avgNewUsers) {
		this.avgNewUsers = avgNewUsers;
	}

	public Long getSumNewUsers() {
		return sumNewUsers;
	}

	public void setSumNewUsers(Long sumNewUsers) {
		this.sumNewUsers = sumNewUsers;
	}
}