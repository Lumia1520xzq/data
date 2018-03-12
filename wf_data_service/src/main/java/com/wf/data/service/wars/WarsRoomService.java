package com.wf.data.service.wars;

import com.wf.core.service.CrudService;
import com.wf.data.dao.wars.WarsRoomDao;
import com.wf.data.dao.wars.entity.WarsRoom;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WarsRoomService extends CrudService<WarsRoomDao, WarsRoom> {

	public Double getTotalWarsProfitByUser(Map<String, Object> baseParam) {
		return dao.getTotalWarsProfitByUser(baseParam);
	}

	public Double getTotalWarsBettingByUser(Map<String, Object> baseParam) {
		return dao.getTotalWarsBettingByUser(baseParam);
	}
}
