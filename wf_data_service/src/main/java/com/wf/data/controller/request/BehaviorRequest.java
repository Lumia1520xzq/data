package com.wf.data.controller.request;

import javax.validation.constraints.NotNull;

public class BehaviorRequest {
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