package com.wf.data.dao.trans.entity;

import com.wf.core.persistence.DataEntity;

public class PayAgentMerchant extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long agentId;
	private String merchantCode;
	private String company;
	private Integer enable;
	private String attach;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
}