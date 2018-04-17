package com.wf.data.dto;

import java.io.Serializable;
import java.util.List;

public class UicDto implements Serializable{
	
	private static final long serialVersionUID = -1;
	
	private Integer gameType;		//游戏类型
	private String searchDate;		//查询时间
	private Long channelId;			//渠道ID
	private Long parentId;          //主渠道
	private List<Long> userIds;     //内部用户id
	private String beginDate;	//开始时间
	private String endDate;	//结束时间
	private List<Integer> businessTypes;
	
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<Integer> getBusinessTypes() {
		return businessTypes;
	}
	public void setBusinessTypes(List<Integer> businessTypes) {
		this.businessTypes = businessTypes;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	
}