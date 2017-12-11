package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.BuryingPointContents;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.uic.entity.UicUser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jianjian.huang
 *  2017年12月8日
 */

@Service
public class EsUicIntroduceService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EsClientFactory esClientFactory;


    public Long getMinUserId(String date){
        AggregationBuilder aggsBuilder = EsQueryBuilders.minAggregation("minUser","id");
        Aggregations aggs = esClientFactory.getAggregation(EsContents.UIC_USER, EsContents.UIC_USER, aggsBuilder, userQuery(date));
        Map<String, Aggregation> aggMap = aggs.asMap();
        Min min = (Min) aggMap.get("minUser");
        return (long) min.getValue();
    }

    public UicUser getByUserId(Long userId) {
         return esClientFactory.get(EsContents.UIC_USER, EsContents.UIC_USER, userId.toString(), UicUser.class);
    }

    public List<Long> getActiveUserIds(String date, Long channelId) {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregationTermAsc("userCount", "user_id", 1000000,true);
        List<Long> list = new ArrayList<>();
        Aggregations aggs = esClientFactory.getAggregation(EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT, aggsBuilder, getActiveQuery(date,channelId));
        LongTerms agg = (LongTerms) aggs.get("userCount");
        Iterator<Bucket> it = agg.getBuckets().iterator();
        while (it.hasNext()) {
            Bucket buck = it.next();
            list.add((Long) buck.getKey());
        }
        return list;
    }

    private QueryBuilder getActiveQuery(String date,Long channelId) {
        Map<String, Object> map = new HashMap<>();
        QueryBuilder query;
        map.put("delete_flag", 0);
        map.put("burying_type", BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if (date != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(date + " 00:00:00",DateUtils.DATE_TIME_PATTERN)));
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(date + " 23:59:59",DateUtils.DATE_TIME_PATTERN)));
        }
        if (channelId != null) {
            boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
        }
        boolQuery.mustNot(QueryBuilders.termQuery("user_id",0L));
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }


    private QueryBuilder userQuery(String date) {
        Map<String, Object> map = new HashMap<>();
        QueryBuilder query;
        map.put("delete_flag", 0);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if (date != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(date + " 00:00:00", DateUtils.DATE_TIME_PATTERN)));
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(date + " 23:59:59", DateUtils.DATE_TIME_PATTERN)));
        }
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }

}
