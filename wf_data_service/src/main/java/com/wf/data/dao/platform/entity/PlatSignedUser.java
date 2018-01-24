package com.wf.data.dao.platform.entity;


import com.wf.core.persistence.DataEntity;

public class PlatSignedUser extends DataEntity {
	private static final long serialVersionUID = -1;
	private Integer days;
	private Long userId;
	private Long amount;
	private Integer status;
	private Long channelId;
}