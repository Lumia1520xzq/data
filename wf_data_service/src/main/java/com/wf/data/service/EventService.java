package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.DataEventDao;
import com.wf.data.dao.data.entity.DataEvent;
import org.springframework.stereotype.Service;

/**
 * @author shihui
 * @date 2018/1/29
 */

@Service
public class EventService extends CrudService<DataEventDao,DataEvent> {
}
