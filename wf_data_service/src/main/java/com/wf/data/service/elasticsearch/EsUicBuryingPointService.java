package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 
 * @author jianjian.huang
 * @date 2017年8月16日
 */

@Service
public class EsUicBuryingPointService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;

	public Integer getActiveCount(String date,Long channelId){
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,aggsBuilder,getActiveQuery(date, channelId));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		int count =agg.getBuckets().size();
		return count;
	}
	
	public List<Long> getActiveUserIds(String date,Long channelId){
		List<Long> list = new ArrayList<Long>();
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,
		aggsBuilder, getActiveQuery(date, channelId));
		LongTerms agg = (LongTerms) aggs.get("userCount");
		Iterator<Bucket> it = agg.getBuckets().iterator();
		while (it.hasNext()) {
			Bucket buck = it.next();
			list.add((Long) buck.getKey());
		}
		return list;
	}
	

	//日活用户查询条件
	private QueryBuilder getActiveQuery(String date,Long channelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		map.put("delete_flag", 0);
		map.put("burying_type",8);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		String beginTime=date+" 00:00:00";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
		String endTime=date+" 23:59:59";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
		if (channelId != null) {
			boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
		} 
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}
	
	
	
}
