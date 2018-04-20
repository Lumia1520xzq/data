package com.wf.data.service.data;

import com.wf.core.service.CrudService;
import com.wf.data.dao.data.GrowTestDao;
import com.wf.data.dao.data.entity.GrowTest;
import org.springframework.stereotype.Service;

@Service
public class GrowTestService extends CrudService<GrowTestDao, GrowTest> {
}
