package com.wf.data.controller.request;

import com.wf.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

public class BehaviorRequest extends DataEntity {
	private static final long serialVersionUID = -1;
	@NotNull(message = "behaviorEventId不能为空")
	private Long behaviorEventId;

	public Long getBehaviorEventId() {
		return behaviorEventId;
	}

	public void setBehaviorEventId(Long behaviorEventId) {
		this.behaviorEventId = behaviorEventId;
	}

}