package com.wf.data.dao.entity.mysql;


import com.wf.core.persistence.DataEntity;

public class UicGroupType extends DataEntity {
	private static final long serialVersionUID = -1;
	private String name;
	private Long parentId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}