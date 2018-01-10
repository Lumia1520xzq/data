package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;

public class TransFragmentConvertRecord extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long fragmentId;
	private Long phyAwardsId;
	private Integer num;
	private String receiverName;
	private String receiverAddress;
	private String receiverMobile;
	private Integer receiveStatus;
	private String receiveRemark;
	private Long channelId;
	private Integer status;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(Long fragmentId) {
		this.fragmentId = fragmentId;
	}

	public Long getPhyAwardsId() {
		return phyAwardsId;
	}

	public void setPhyAwardsId(Long phyAwardsId) {
		this.phyAwardsId = phyAwardsId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public Integer getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(Integer receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public String getReceiveRemark() {
		return receiveRemark;
	}

	public void setReceiveRemark(String receiveRemark) {
		this.receiveRemark = receiveRemark;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}