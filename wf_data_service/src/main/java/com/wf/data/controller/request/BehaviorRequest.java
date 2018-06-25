package com.wf.data.controller.request;

import javax.validation.constraints.NotNull;

/**
 * 新埋点Request
 */
public class BehaviorRequest {
	private static final long serialVersionUID = -1;
	/**
	 * 新埋点eventId
	 */
	@NotNull(message = "behaviorEventId不能为空")
	private Long behaviorEventId;

	public Long getBehaviorEventId() {
		return behaviorEventId;
	}

	public void setBehaviorEventId(Long behaviorEventId) {
		this.behaviorEventId = behaviorEventId;
	}

}