package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.BuryingPointContents;
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
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 俱乐部渠道日活查询
 * @author jianjian.huang
 * @date 2017年10月23日
 */

@Service
public class EsClubService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;
	
	// 2、活跃用户(游戏)
	public Integer getActiveUser(Integer gameType,String date,List<Long> channelIds){
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> activeUsers=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate), BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE,channelIds);
		return activeUsers.size();
	}
	
	private List<Long> getUserIds(AggregationBuilder aggsBuilder,Integer gameType,String begin,String end,Integer buryingType,List<Long> channelIds){
		List<Long> list=new ArrayList<Long>();
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,aggsBuilder,getActiveQuery(gameType,begin,end,buryingType,channelIds));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		Iterator<Bucket> it=agg.getBuckets().iterator();
		while(it.hasNext()) {
			Bucket buck=it.next();
			list.add((Long)buck.getKey());
		}
		return list;
	}
	
	// 日活用户查询条件
	private QueryBuilder getActiveQuery(Integer gameType,String beginTime,String endTime,Integer buryingType,List<Long> channelIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		if (gameType != null) {
		map.put("game_type",gameType);
		}
		map.put("delete_flag", 0);
		map.put("burying_type",buryingType);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		if(beginTime!=null){
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
		}
		if(endTime!=null){
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lt(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
		}
		if(CollectionUtils.isNotEmpty(channelIds)){
			boolQuery.must(QueryBuilders.termsQuery("channel_id",channelIds));
		}
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}


}
