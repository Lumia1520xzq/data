package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

import java.io.Serializable;
import java.util.Date;

public class DatawareUserLabel extends DataEntity implements Serializable{
	private static final long serialVersionUID = -1;
	private String uuid;
	private Long userId;
	private String prediction;
	private String userLevel;
	private String r;
	private String f;
	private String m;
	private String userValueHierarchy;
	private String businessDate;
    private Date createTime;
    private Date updateTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public String getUserValueHierarchy() {
		return userValueHierarchy;
	}

	public void setUserValueHierarchy(String userValueHierarchy) {
		this.userValueHierarchy = userValueHierarchy;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}