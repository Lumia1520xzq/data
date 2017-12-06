package com.wf.data.service.elasticsearch.track;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EsBehaviorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EsClientFactory esClientFactory;

    /**
     * 根据eventId查询每天日活数
     * @param date
     * @param eventIdList
     * @return
     */
    public Integer getActiveUser(String date,List<Long> eventIdList){
        AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
        List<Long> activeUsers=new ArrayList<Long>();
        Aggregations aggs = esClientFactory.getAggregation(
                EsContents.UIC_BEHAVIOR_RECORD, EsContents.UIC_BEHAVIOR_RECORD,aggsBuilder,getActiveQuery(date+" 00:00:00",DateUtils.formatDateTime(nextDate),eventIdList));
        LongTerms agg = (LongTerms)aggs.get("userCount");
        Iterator<Terms.Bucket> it=agg.getBuckets().iterator();
        while(it.hasNext()) {
            Terms.Bucket buck=it.next();
            activeUsers.add((Long)buck.getKey());
        }
        return activeUsers.size();
    }

    /**
     * 查询日活DSL
     * @param beginTime
     * @param endTime
     * @param eventIdList
     * @return
     */
    private QueryBuilder getActiveQuery( String beginTime, String endTime,List<Long> eventIdList) {
        Map<String, Object> map = new HashMap<String, Object>();
        QueryBuilder query = null;
        map.put("delete_flag", 0);

        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if(beginTime!=null){
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
        }
        if(endTime!=null){
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lt(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
        }

        if(CollectionUtils.isNotEmpty(eventIdList)){
            boolQuery.must(QueryBuilders.termsQuery("behavior_event_id",eventIdList));
        }

        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }
}