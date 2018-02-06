package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

public class DatawareFinalRegisteredArpuOut extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long newUsers;
	private Double recharge1;
	private Double arpu1;
	private Double recharge2;
	private Double arpu2;
	private Double recharge3;
	private Double arpu3;
	private Double recharge4;
	private Double arpu4;
	private Double recharge5;
	private Double arpu5;
	private Double recharge6;
	private Double arpu6;
	private Double recharge7;
	private Double arpu7;
	private Double recharge15;
	private Double arpu15;
	private Double recharge30;
	private Double arpu30;
	private Double recharge60;
	private Double arpu60;
	private Double recharge90;
	private Double arpu90;
	private String channelName;
	private Long parentId;
	private Long channelId;
	private String businessDate;



	@ExcelField(title = "新用户数", type = 1, align = 2, sort = 3)
	public Long getNewUsers() {
		return newUsers;
	}

	public void setNewUsers(Long newUsers) {
		this.newUsers = newUsers;
	}

	public Double getRecharge1() {
		return recharge1;
	}

	public void setRecharge1(Double recharge1) {
		this.recharge1 = recharge1;
	}

	@ExcelField(title = "1天", type = 1, align = 2, sort = 4)
	public Double getArpu1() {
		return arpu1;
	}

	public void setArpu1(Double arpu1) {
		this.arpu1 = arpu1;
	}

	public Double getRecharge2() {
		return recharge2;
	}

	public void setRecharge2(Double recharge2) {
		this.recharge2 = recharge2;
	}

	@ExcelField(title = "2天", type = 1, align = 2, sort = 5)
	public Double getArpu2() {
		return arpu2;
	}

	public void setArpu2(Double arpu2) {
		this.arpu2 = arpu2;
	}

	public Double getRecharge3() {
		return recharge3;
	}

	public void setRecharge3(Double recharge3) {
		this.recharge3 = recharge3;
	}

	@ExcelField(title = "3天", type = 1, align = 2, sort = 6)
	public Double getArpu3() {
		return arpu3;
	}

	public void setArpu3(Double arpu3) {
		this.arpu3 = arpu3;
	}

	public Double getRecharge4() {
		return recharge4;
	}

	public void setRecharge4(Double recharge4) {
		this.recharge4 = recharge4;
	}

	@ExcelField(title = "4天", type = 1, align = 2, sort = 7)
	public Double getArpu4() {
		return arpu4;
	}

	public void setArpu4(Double arpu4) {
		this.arpu4 = arpu4;
	}

	public Double getRecharge5() {
		return recharge5;
	}

	public void setRecharge5(Double recharge5) {
		this.recharge5 = recharge5;
	}

	@ExcelField(title = "5天", type = 1, align = 2, sort = 8)
	public Double getArpu5() {
		return arpu5;
	}

	public void setArpu5(Double arpu5) {
		this.arpu5 = arpu5;
	}

	public Double getRecharge6() {
		return recharge6;
	}

	public void setRecharge6(Double recharge6) {
		this.recharge6 = recharge6;
	}

	@ExcelField(title = "6天", type = 1, align = 2, sort = 9)
	public Double getArpu6() {
		return arpu6;
	}

	public void setArpu6(Double arpu6) {
		this.arpu6 = arpu6;
	}

	public Double getRecharge7() {
		return recharge7;
	}

	public void setRecharge7(Double recharge7) {
		this.recharge7 = recharge7;
	}

	@ExcelField(title = "7天", type = 1, align = 2, sort = 10)
	public Double getArpu7() {
		return arpu7;
	}

	public void setArpu7(Double arpu7) {
		this.arpu7 = arpu7;
	}

	public Double getRecharge15() {
		return recharge15;
	}

	public void setRecharge15(Double recharge15) {
		this.recharge15 = recharge15;
	}

	@ExcelField(title = "15天", type = 1, align = 2, sort = 11)
	public Double getArpu15() {
		return arpu15;
	}

	public void setArpu15(Double arpu15) {
		this.arpu15 = arpu15;
	}

	public Double getRecharge30() {
		return recharge30;
	}

	public void setRecharge30(Double recharge30) {
		this.recharge30 = recharge30;
	}

	@ExcelField(title = "30天", type = 1, align = 2, sort = 12)
	public Double getArpu30() {
		return arpu30;
	}

	public void setArpu30(Double arpu30) {
		this.arpu30 = arpu30;
	}

	public Double getRecharge60() {
		return recharge60;
	}

	public void setRecharge60(Double recharge60) {
		this.recharge60 = recharge60;
	}

	@ExcelField(title = "60天", type = 1, align = 2, sort = 13)
	public Double getArpu60() {
		return arpu60;
	}

	public void setArpu60(Double arpu60) {
		this.arpu60 = arpu60;
	}

	public Double getRecharge90() {
		return recharge90;
	}

	public void setRecharge90(Double recharge90) {
		this.recharge90 = recharge90;
	}

	@ExcelField(title = "90天", type = 1, align = 2, sort = 14)
	public Double getArpu90() {
		return arpu90;
	}

	public void setArpu90(Double arpu90) {
		this.arpu90 = arpu90;
	}

	@ExcelField(title = "渠道", type = 1, align = 2, sort = 1)
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	@ExcelField(title = "注册日期", type = 1, align = 2, sort = 2)
	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}



}