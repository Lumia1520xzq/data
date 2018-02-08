package com.wf.data.service.data;


import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareGameBettingInfoHourDao;
import com.wf.data.dao.datarepo.entity.DatawareGameBettingInfoHour;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DatawareGameBettingInfoHourService extends CrudService<DatawareGameBettingInfoHourDao,DatawareGameBettingInfoHour>{

	public long getCountByTime(Map<String, Object> params) {
		return dao.getCountByTime(params);
	}

	public void deleteByDate(Map<String, Object> params) {
		dao.deleteByDate(params);
	}
}

