package com.wf.data.controller.history;

import com.wf.data.common.DataBaseController;
import com.wf.data.controller.request.BehaviorRequest;
import com.wf.data.controller.request.BuryingPointRequest;
import com.wf.data.dao.entity.mycat.BehaviorRecord;
import com.wf.data.dao.entity.mycat.BuryingPoint;
import com.wf.data.dao.entity.mysql.BehaviorType;
import com.wf.data.service.BehaviorRecordService;
import com.wf.data.service.BehaviorTypeService;
import com.wf.data.service.BuryingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Deprecated
@CrossOrigin(maxAge = 3600)
@RequestMapping("/")
@RestController
public class HistoryController extends DataBaseController {
	@Autowired
	private BehaviorRecordService behaviorRecordService;
	@Autowired
	private BehaviorTypeService behaviorTypeService;
	@Autowired
	private BuryingPointService dataBuryingPointService;

	/**
	 * 设置埋点
	 * GameTypeContents
	 * BuryingPointContents
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "api_app/api/uic/buryingPoint", method = RequestMethod.POST)
	public Object point(@Valid@RequestBody BuryingPointRequest request) {
		Long userId = null;

		try {
			userId = getUserIdNoError();
		} catch (Exception e) {
		}

		BuryingPoint point = new BuryingPoint();
		point.setUserId(userId);
		point.setGameType(request.getGameType());
		point.setBuryingType(request.getBuryingType());
		point.setChannelId(getChannelId());
		dataBuryingPointService.save(point);
//        rabbitTemplate.convertAndSend("monitor_rick_buryingpoint", uicBuryingPoint);
		return SUCCESS;
	}

	/**
	 * 设置埋点
	 * GameTypeContents
	 * BuryingPointContents
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "api_app/api/uic/behaviorRecord", method = RequestMethod.POST)
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
