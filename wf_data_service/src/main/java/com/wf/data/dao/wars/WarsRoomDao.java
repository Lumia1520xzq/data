package com.wf.data.dao.wars;

import com.wf.core.persistence.CrudDao;
import com.wf.core.persistence.MyBatisDao;
import com.wf.data.dao.wars.entity.WarsRoom;

import java.util.Map;

@MyBatisDao(tableName = "wars_room")
public interface WarsRoomDao extends CrudDao<WarsRoom> {

    Double getTotalWarsProfitByUser(Map<String, Object> baseParam);

    Double getTotalWarsBettingByUser(Map<String, Object> baseParam);
}
