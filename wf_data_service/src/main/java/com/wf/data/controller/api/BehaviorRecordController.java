package com.wf.data.controller.api;

import javax.validation.Valid;

import com.wf.core.web.base.BaseController;
import com.wf.data.common.DataBaseController;
import com.wf.data.controller.request.BehaviorRequest;
import com.wf.data.dao.entity.mycat.UicBehaviorRecord;
import com.wf.data.dao.entity.mysql.UicBehaviorType;
import com.wf.data.service.MycatUicBehaviorRecordService;
import com.wf.data.service.UicBehaviorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RequestMapping("/data/api/behaviorRecord")
@RestController
public class BehaviorRecordController extends DataBaseController {
	@Autowired
	private MycatUicBehaviorRecordService behaviorRecordService;
	@Autowired
	private UicBehaviorTypeService uicBehaviorTypeService;

	/**
	 * 设置埋点
	 * GameTypeContents
	 * BuryingPointContents
	 * @param request
	 * @return
	 */
	@RequestMapping("point")
	public Object behaviorRecord(@Valid@RequestBody BehaviorRequest request) {
		Long userId = null;
		try {
			userId = getUserIdNoError();
		} catch (Exception e) {
		}
		UicBehaviorType uicBehaviorType = uicBehaviorTypeService.getByEventId(request.getBehaviorEventId());
		if (uicBehaviorType != null) {
			UicBehaviorRecord uicBehaviorRecord = new UicBehaviorRecord();
			uicBehaviorRecord.setUserId(userId);
			uicBehaviorRecord.setBehaviorEventId(request.getBehaviorEventId());
			uicBehaviorRecord.setBehaviorName(uicBehaviorType.getFullName());
			uicBehaviorRecord.setChannelId(getChannelId());
			uicBehaviorRecord.setParentChannelId(getParentChannelId());
			behaviorRecordService.save(uicBehaviorRecord);
		}



		return SUCCESS;
	}
	
	 

	
}
