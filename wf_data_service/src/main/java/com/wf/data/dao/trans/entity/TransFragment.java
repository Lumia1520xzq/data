package com.wf.data.dao.trans.entity;


import com.wf.core.persistence.DataEntity;

public class TransFragment extends DataEntity {
	private static final long serialVersionUID = -1;
	private Long phyAwardsId;
	private String name;
	private Long goldAmount;
	private Double rmbAmount;
	private String image;
	private String imageApp;
	private String fragmentSource;
	private Integer changeNum;
	private Integer awardsType;
	private String awardsName;
	private String awardsImage;
	private String awardsImageApp;
	private Long channelId;

	public Long getPhyAwardsId() {
		return phyAwardsId;
	}

	public void setPhyAwardsId(Long phyAwardsId) {
		this.phyAwardsId = phyAwardsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageApp() {
		return imageApp;
	}

	public void setImageApp(String imageApp) {
		this.imageApp = imageApp;
	}

	public String getFragmentSource() {
		return fragmentSource;
	}

	public void setFragmentSource(String fragmentSource) {
		this.fragmentSource = fragmentSource;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}

	public Integer getAwardsType() {
		return awardsType;
	}

	public void setAwardsType(Integer awardsType) {
		this.awardsType = awardsType;
	}

	public String getAwardsName() {
		return awardsName;
	}

	public void setAwardsName(String awardsName) {
		this.awardsName = awardsName;
	}

	public String getAwardsImage() {
		return awardsImage;
	}

	public void setAwardsImage(String awardsImage) {
		this.awardsImage = awardsImage;
	}

	public String getAwardsImageApp() {
		return awardsImageApp;
	}

	public void setAwardsImageApp(String awardsImageApp) {
		this.awardsImageApp = awardsImageApp;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}