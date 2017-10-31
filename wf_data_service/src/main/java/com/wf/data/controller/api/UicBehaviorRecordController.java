package com.wf.data.controller.api;

import com.wf.core.web.base.BaseController;
import com.wf.data.dao.entity.mycat.UicBehaviorRecord;
import com.wf.data.dao.entity.mysql.UicBehaviorType;
import com.wf.data.service.MycatUicBehaviorRecordService;
import com.wf.data.service.UicBehaviorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 埋点内部接口  -- 新的埋点
 * 
 * @author jijie.chen
 *
 */
@RequestMapping("/ms/uic/behaviorrecord")
@RestController
public class UicBehaviorRecordController extends BaseController {
	@Autowired
	private MycatUicBehaviorRecordService mycatUicBehaviorRecordService;
	@Autowired
	private UicBehaviorTypeService uicBehaviorTypeService;

	/**
	 * 保存埋点
	 */
	@RequestMapping("save")
	public void save(@RequestBody UicBehaviorRecord uicBehaviorRecord) {
		UicBehaviorType uicBehaviorType = uicBehaviorTypeService.getByEventId(uicBehaviorRecord.getBehaviorEventId());
		if (uicBehaviorType != null) {
			String name = uicBehaviorType.getFullName();
			uicBehaviorRecord.setBehaviorName(name);
			mycatUicBehaviorRecordService.save(uicBehaviorRecord);
		}
	}

}
