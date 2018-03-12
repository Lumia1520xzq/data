package com.wf.data.dao.wars.entity;

import com.wf.core.persistence.DataEntity;

import java.util.Date;

public class WarsRoom extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long userId;
	private Long inviteeUserId;
	private String roomNum;
	private Integer ownerArmy;
	private Integer inviteeArmy;
	private Integer amount;
	private Integer status;
	private Integer resultStatus;
	private Date battlingTime;
	private Long channelId;

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getInviteeUserId() {
		return inviteeUserId;
	}
	public void setInviteeUserId(Long inviteeUserId) {
		this.inviteeUserId = inviteeUserId;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public Integer getOwnerArmy() {
		return ownerArmy;
	}
	public void setOwnerArmy(Integer ownerArmy) {
		this.ownerArmy = ownerArmy;
	}
	public Integer getInviteeArmy() {
		return inviteeArmy;
	}
	public void setInviteeArmy(Integer inviteeArmy) {
		this.inviteeArmy = inviteeArmy;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(Integer resultStatus) {
		this.resultStatus = resultStatus;
	}

	public Date getBattlingTime() {
		return battlingTime;
	}

	public void setBattlingTime(Date battlingTime) {
		this.battlingTime = battlingTime;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}