package com.wf.data.controller.api;

import com.wf.data.common.DataBaseController;
import com.wf.data.controller.request.BehaviorRequest;
import com.wf.data.dao.data.entity.BehaviorType;
import com.wf.data.dao.mycatdata.entity.BehaviorRecord;
import com.wf.data.service.BehaviorRecordService;
import com.wf.data.service.BehaviorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(maxAge = 3600)
@RequestMapping("/data/api/behaviorRecord")
@RestController
public class BehaviorRecordController extends DataBaseController {
	@Autowired
	private BehaviorRecordService behaviorRecordService;
	@Autowired
	private BehaviorTypeService behaviorTypeService;

	/**
	 * 设置埋点
	 * GameTypeContents
	 * BuryingPointContents
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "point", method = RequestMethod.POST)
	public Object behaviorRecord(@Valid@RequestBody BehaviorRequest request) {
		Long userId = null;
		try {
			userId = getUserIdNoError();
		} catch (Exception e) {
		}
		BehaviorType behaviorType = behaviorTypeService.getByEventId(request.getBehaviorEventId());
		if (behaviorType != null) {
			BehaviorRecord record = new BehaviorRecord();
			record.setUserId(userId);
			record.setBehaviorEventId(request.getBehaviorEventId());
			record.setBehaviorName(behaviorType.getFullName());
			record.setChannelId(getChannelId());
			record.setParentChannelId(getParentChannelId());
			behaviorRecordService.save(record);
		}



		return SUCCESS;
	}
	
	 

	
}
