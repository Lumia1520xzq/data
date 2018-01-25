package com.wf.data.dto;

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
	private Integer days;
	private Long avgDau;
	private Double avgDarpu;
	private Double sumCost;
	private Double costRate;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

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

	public Long getAvgDau() {
		return avgDau;
	}

	public void setAvgDau(Long avgDau) {
		this.avgDau = avgDau;
	}

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

	public Double getSumCost() {
		return sumCost;
	}

	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}
}