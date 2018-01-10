package com.wf.data.dao.mall.entity;


import com.wf.core.persistence.DataEntity;

public class InventoryPhyAwardsInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private String name;
	private Integer type;
	private String description;
	private Long goldAmount;
	private Double rmbAmount;
	private Integer awardsSize;
	private Integer totalNum;
	
	//管理端属性
	private Integer notAssignNum;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getGoldAmount() {
		return goldAmount;
	}
	public void setGoldAmount(Long goldAmount) {
		this.goldAmount = goldAmount;
	}
	public Double getRmbAmount() {
		return rmbAmount;
	}
	public void setRmbAmount(Double rmbAmount) {
		this.rmbAmount = rmbAmount;
	}
	public Integer getAwardsSize() {
		return awardsSize;
	}
	public void setAwardsSize(Integer awardsSize) {
		this.awardsSize = awardsSize;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getNotAssignNum() {
		return notAssignNum;
	}
	public void setNotAssignNum(Integer notAssignNum) {
		this.notAssignNum = notAssignNum;
	}
}