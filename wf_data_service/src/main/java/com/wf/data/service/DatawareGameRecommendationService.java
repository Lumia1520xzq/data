package com.wf.data.service;

import com.wf.core.service.CrudService;
import com.wf.data.dao.datarepo.DatawareGameRecommendationDao;
import com.wf.data.dao.datarepo.entity.DatawareGameRecommendation;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shihui
 * @date 2018/8/14
 */

@Service
public class DatawareGameRecommendationService extends
        CrudService<DatawareGameRecommendationDao, DatawareGameRecommendation> {
    public List<DatawareGameRecommendation> getRecommendationGameIdByUser(long userId, String yesterDateString) {
        return dao.getRecommendationGameIdByUser(userId, yesterDateString);
    }
}
