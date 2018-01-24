package com.wf.data.service.elasticsearch;

import com.wf.core.persistence.Page;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.controller.request.UicBehaviorReq;
import com.wf.data.dao.data.entity.UicBehaviorRecord;
import com.wf.data.service.BehaviorTypeService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author huangjianjian
 * @date 2017年9月29日
 */

@Service
public class EsUserBehaviorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EsClientFactory esClientFactory;
    @Autowired
    private BehaviorTypeService behaviorTypeService;

    public Page<UicBehaviorRecord> findPage(Integer start, Integer length, UicBehaviorReq note) {
        return esClientFactory.findPage(
                EsContents.UIC_BEHAVIOR_RECORD, EsContents.UIC_BEHAVIOR_RECORD, getQuery(note), start, length,
                UicBehaviorRecord.class);
    }

    //用户访问路径查询条件
    private QueryBuilder getQuery(UicBehaviorReq note) {
        Map<String, Object> map = new HashMap<String, Object>();
        QueryBuilder query = null;
        map.put("delete_flag", 0);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        String beginDate = note.getBeginDate();
        String endDate = note.getEndDate();
        Long parentId = note.getParentId();
        Long channelId = note.getChannelId();
        Long userId = note.getUserId();
        if (StringUtils.isNotEmpty(beginDate)) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginDate, DateUtils.DATE_TIME_PATTERN)));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endDate, DateUtils.DATE_TIME_PATTERN)));
        }
        if (channelId != null) {
            boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
        }
        if (parentId != null) {
            boolQuery.must(QueryBuilders.termQuery("parent_channel_id", parentId));
        }
        if (userId != null) {
            boolQuery.must(QueryBuilders.termQuery("user_id", userId));
        }
        Long parentEventId = note.getParentEventId();
        Long eventId = note.getEventId();
        if (eventId != null) {
            boolQuery.must(QueryBuilders.termQuery("behavior_event_id", eventId));
        } else {
            if (parentEventId != null) {
                List<Long> eventIds = behaviorTypeService.findEventIds(parentEventId);
                boolQuery.must(QueryBuilders.termsQuery("behavior_event_id", eventIds));
            }
        }
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }

}
