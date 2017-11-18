package com.wf.data.dao.fish.entity;


import com.wf.core.persistence.DataEntity;

public class RoomFishInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long roomId;
	private Long fishId;
	private Long userId;
	private Long fishConfigId;
	private Integer amount;
	private Integer returnAmount;
	private String roadId;
	private Integer appearTime;
	private Integer disappearTime;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getFishId() {
		return fishId;
	}

	public void setFishId(Long fishId) {
		this.fishId = fishId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFishConfigId() {
		return fishConfigId;
	}

	public void setFishConfigId(Long fishConfigId) {
		this.fishConfigId = fishConfigId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Integer returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getRoadId() {
		return roadId;
	}

	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}

	public Integer getAppearTime() {
		return appearTime;
	}

	public void setAppearTime(Integer appearTime) {
		this.appearTime = appearTime;
	}

	public Integer getDisappearTime() {
		return disappearTime;
	}

	public void setDisappearTime(Integer disappearTime) {
		this.disappearTime = disappearTime;
	}
}