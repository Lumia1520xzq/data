package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.MapUtils;
import com.wf.data.dao.datarepo.entity.DatawareBuryingPointHour;
import com.wf.data.dao.mycatdata.BuryingPointDao;
import com.wf.data.dao.mycatdata.entity.BuryingPoint;
import com.wf.data.dao.mycattrans.TransDealDao;
import com.wf.data.dao.mycattrans.entity.TransDeal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransDealService extends CrudService<TransDealDao, TransDeal> {

}
