package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.type.NumberUtils;

import java.util.List;

public class ReportGameInfo extends DataEntity {
	private static final long serialVersionUID = -1;
	
	private Integer channelType;	//渠道
	private Integer gameType;		//游戏类型
	private long userDAU;			//DAU
	private int cathecticUserNum;	//投注人数
	private long cathecticNum ;		//投注笔数
	private long cathecticMoney; 	//投注金额
	private double cathecticARPU;	//投注ARPU
	private double cathecticASP;	//投注ASP
	private int rechargeUserNum;	//充值人数
	private long rechargeNum;		//充值笔数
	private double rechargeMoney;		//充值金额
	private long winMoney;			//赢钱金额
	private double rechargeARPU;	//充值ARPU
	private double rewardRatio; 	//返奖率
	private double infiRatio; 		//付费渗透率
	private long hsCardTotal;		//发放彩金卡
	private String beginDate;		//开始时间
	private String endDate;			//结束时间
	private String searchDate;		//查询时间
	private String orderColumn;		//倒序排列字段
	private Integer searchType;     // 按天查询or按小时查询
	private Long channelId;			//渠道ID
	private Long doyoChannelId;
	private Long parentId;          //主渠道
	private List<Long> userIds;     //内部用户id
	private String startSearchDate;		//开始时间
	private String endSearchDate;			//结束时间
	
	
	
	public String getStartSearchDate() {
		return startSearchDate;
	}
	public void setStartSearchDate(String startSearchDate) {
		this.startSearchDate = startSearchDate;
	}
	public String getEndSearchDate() {
		return endSearchDate;
	}
	public void setEndSearchDate(String endSearchDate) {
		this.endSearchDate = endSearchDate;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getSearchType() {
		return searchType;
	}
	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}
	
	public Integer getChannelType() {
		return channelType;
	}
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	public long getUserDAU() {
		return userDAU;
	}
	public void setUserDAU(long userDAU) {
		this.userDAU = userDAU;
	}
	public int getCathecticUserNum() {
		return cathecticUserNum;
	}
	public void setCathecticUserNum(int cathecticUserNum) {
		this.cathecticUserNum = cathecticUserNum;
	}
	public long getCathecticNum() {
		return cathecticNum;
	}
	public void setCathecticNum(long cathecticNum) {
		this.cathecticNum = cathecticNum;
	}
	public long getCathecticMoney() {
		return cathecticMoney;
	}
	public void setCathecticMoney(long cathecticMoney) {
		this.cathecticMoney = cathecticMoney;
	}
	public double getCathecticARPU() {
		return cathecticARPU;
	}
	public void setCathecticARPU(double cathecticARPU) {
		this.cathecticARPU = cathecticARPU;
	}
	public double getCathecticASP() {
		return cathecticASP;
	}
	public void setCathecticASP(double cathecticASP) {
		this.cathecticASP = cathecticASP;
	}
	public int getRechargeUserNum() {
		return rechargeUserNum;
	}
	public void setRechargeUserNum(int rechargeUserNum) {
		this.rechargeUserNum = rechargeUserNum;
	}
	public long getRechargeNum() {
		return rechargeNum;
	}
	public void setRechargeNum(long rechargeNum) {
		this.rechargeNum = rechargeNum;
	}
	public double getRechargeMoney() {
		return rechargeMoney;
	}
	public void setRechargeMoney(double rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}
	public long getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(long winMoney) {
		this.winMoney = winMoney;
	}
	public double getRechargeARPU() {
		return rechargeARPU;
	}
	public void setRechargeARPU(double rechargeARPU) {
		this.rechargeARPU = rechargeARPU;
	}
	public double getRewardRatio() {
		return rewardRatio;
	}
	public void setRewardRatio(double rewardRatio) {
		this.rewardRatio = rewardRatio;
	}
	public double getInfiRatio() {
		return infiRatio;
	}
	public void setInfiRatio(double infiRatio) {
		this.infiRatio = infiRatio;
	}
	public long getHsCardTotal() {
		return hsCardTotal;
	}
	public void setHsCardTotal(long hsCardTotal) {
		this.hsCardTotal = hsCardTotal;
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
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	
	public Long getDoyoChannelId() {
		return doyoChannelId;
	}
	public void setDoyoChannelId(Long doyoChannelId) {
		this.doyoChannelId = doyoChannelId;
	}
	
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	//投注计算
	public void setCathectic() {
		if(this.cathecticUserNum != 0){
			double res = (double)this.cathecticMoney/this.cathecticUserNum;
			this.cathecticARPU = NumberUtils.getDoubleFormat2Even(res);
		}
		if(this.cathecticNum != 0){
			double res = (double)this.cathecticMoney/this.cathecticNum;
			this.cathecticASP = NumberUtils.getDoubleFormat2Even(res);
		}
		if(this.cathecticMoney != 0){
			double res = (double)this.winMoney/this.cathecticMoney;
			this.rewardRatio = NumberUtils.getDoubleFormatEven(res,4);
		}
	}
	
	public void setDate(){
		this.beginDate = this.startSearchDate;
		this.endDate = this.endSearchDate;
	}
	
	//充值计算
	public void setRecharge() {
		if(this.rechargeUserNum != 0){
			double res =  (double)this.rechargeMoney/this.rechargeUserNum;
			this.rechargeARPU = NumberUtils.getDoubleFormat2Even(res);
		}
		if(this.userDAU != 0){
			double res = (double)this.rechargeUserNum/this.userDAU;
			this.infiRatio = NumberUtils.getDoubleFormatEven(res,4);
		}
	}
	@Override
	public String toString() {
		return "TransGameInfo [channelType=" + channelType + ", gameType=" + gameType + ", userDAU=" + userDAU
				+ ", cathecticUserNum=" + cathecticUserNum + ", cathecticNum=" + cathecticNum + ", cathecticMoney="
				+ cathecticMoney + ", cathecticARPU=" + cathecticARPU + ", cathecticASP=" + cathecticASP
				+ ", rechargeUserNum=" + rechargeUserNum + ", rechargeNum=" + rechargeNum + ", rechargeMoney="
				+ rechargeMoney + ", winMoney=" + winMoney + ", rechargeARPU=" + rechargeARPU + ", rewardRatio="
				+ rewardRatio + ", infiRatio=" + infiRatio + ", hsCardTotal=" + hsCardTotal + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", searchDate=" + searchDate + ", orderColumn=" + orderColumn
				+ ", searchType=" + searchType + ", channelId=" + channelId + ", doyoChannelId=" + doyoChannelId
				+ ", parentId=" + parentId + ", userIds=" + userIds + "]";
	}
}