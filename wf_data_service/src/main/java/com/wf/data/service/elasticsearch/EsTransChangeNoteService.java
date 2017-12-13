package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.constants.TransactionContents;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.service.UicGroupService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.metrics.scripted.InternalScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/** 
 * 投注流水
 * @author jianjian.huang
 * 2017年8月16日
 */

@Service
public class EsTransChangeNoteService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private UicGroupService uicGroupService;

	/**
	 * 投注人数
	 */
	public Integer getBettingCount(String date,Long channelId,Integer gameType) {
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE,aggsBuilder,getBettingQuery(date,channelId,gameType));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		int count =agg.getBuckets().size();
		return count;
	}

	/**
	 * 投注流水
	 */
	public Double getBettingAmt(String date,Long channelId,Integer gameType) {
		Script mapScript=new Script("params._agg.transactions.add(-1*doc.change_money.value*doc.change_type.value)");
		AggregationBuilder aggsBuilder = EsQueryBuilders.scriptedMetric("bettingAmt", mapScript);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.TRANS_CHANGE_NOTE,EsContents.TRANS_CHANGE_NOTE,aggsBuilder,getBettingQuery(date,channelId,gameType));
		InternalScriptedMetric bettingAmt=(InternalScriptedMetric)aggs.get("bettingAmt");
	    Double betting=(double)bettingAmt.aggregation();
	    return betting;
	}

	/**
	 * 派奖金叶子
	 */
	public Double getAwardAmt(String date,Long channelId,Integer gameType) {
		AggregationBuilder aggsBuilder = EsQueryBuilders.sumAggregation("businessAmount","change_money");
		Aggregations aggs = esClientFactory.getAggregation
		(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getAwardQuery(date, channelId, gameType));
		Map<String, Aggregation> aggMap = aggs.asMap();
		Sum sum = (Sum) aggMap.get("businessAmount");  
		return sum.getValue();
	}


	/**
	 * 投注条件
	 */
	private QueryBuilder getBettingQuery(String date,Long channelId,Integer gameType) {
		Map<String, Object> map = new HashMap<>();
		QueryBuilder query;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		List<Integer> inBizs=new ArrayList<>();
		if(gameType==null){
		          inBizs=Arrays.asList(
				  TransactionContents.BUSINESS_TYPE_BETTING_DART, TransactionContents.BUSINESS_TYPE_BETTING_CENCEL,
				  TransactionContents.BUSINESS_TYPE_BETTING_BILLIARD,
				  TransactionContents.BUSINESS_TYPE_BETTING_WARS,
				  TransactionContents.BUSINESS_TYPE_BETTING_ARROWS,
				  TransactionContents.BUSINESS_TYPE_FOOTBALL_BETTING,
				  TransactionContents.BUSINESS_TYPE_MOTOR_BETTING,TransactionContents.BUSINESS_TYPE_MOTOR_BRTTING_CANCEL,
				  TransactionContents.BUSINESS_TYPE_KINGDOM_BETTING,TransactionContents.BUSINESS_TYPE_KINGDOM_CANCEL,
				  TransactionContents.BUSINESS_TYPE_FISH_FIRE_FISH
				  );
		}else if(gameType == 1) {
				 inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_DART,TransactionContents.BUSINESS_TYPE_BETTING_CENCEL);
		}else if(gameType == 2) {
			     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_BILLIARD);
		}
		else if(gameType == 3) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_QUOITS);
	    }
		else if(gameType == 4) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_WARS);
	    }else if(gameType == 5) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_ARROWS);
	    }else if(gameType == 7) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FOOTBALL_BETTING);
	    }else if(gameType == 8) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_MOTOR_BETTING,TransactionContents.BUSINESS_TYPE_MOTOR_BRTTING_CANCEL);
	    }else if(gameType == 9) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_KINGDOM_BETTING,TransactionContents.BUSINESS_TYPE_KINGDOM_CANCEL);
	    }else if(gameType == 10) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FISH_FIRE_FISH);
	    }
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		
		String beginTime=date+" 00:00:00";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
		
		String endTime=date+" 23:59:59";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
		
		if (channelId != null) {
		boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
		}		
		//剔除内部用户
		List<Long> internalUserIds=getInternalUserIds();
		boolQuery.mustNot(QueryBuilders.termsQuery("user_id",internalUserIds));
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}
	
	private QueryBuilder getAwardQuery(String date,Long channelId,Integer gameType) {
		Map<String, Object> map = new HashMap<>();
		QueryBuilder query;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		List<Integer> inBizs=new ArrayList<>();
		if(gameType==null){
		          inBizs=Arrays.asList(
				  TransactionContents.BUSINESS_TYPE_WIN_DART,
				  TransactionContents.BUSINESS_TYPE_WIN_BILLIARD,
				  TransactionContents.BUSINESS_TYPE_WIN_WARS,
				  TransactionContents.BUSINESS_TYPE_WIN_ARROWS,
				  TransactionContents.BUSINESS_TYPE_FOOTBALL_RETURN_AWARD,
				  TransactionContents.BUSINESS_TYPE_MOTOR_RETURN_AWARD,
				  TransactionContents.BUSINESS_TYPE_KINGDOM_WIN,
				  TransactionContents.BUSINESS_TYPE_FISH_HIT_FISH
				  );
		}else if(gameType == 1) {
				 inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_WIN_DART);
		}else if(gameType == 2) {
			     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_WIN_BILLIARD);
		}
		else if(gameType == 3) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_WIN_QUOITS);
	    }
		else if(gameType == 4) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_WIN_WARS);
	    }else if(gameType == 5) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_WIN_ARROWS);
	    }else if(gameType == 7) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FOOTBALL_RETURN_AWARD);
	    }else if(gameType == 8) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_MOTOR_RETURN_AWARD);
	    }else if(gameType == 9) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_KINGDOM_WIN);
	    }else if(gameType == 10) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FISH_HIT_FISH);
	    }
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		
		String beginTime=date+" 00:00:00";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
		
		String endTime=date+" 23:59:59";
		boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
		
		if (channelId != null) {
		boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
		}		
		//剔除内部用户
		List<Long> internalUserIds=getInternalUserIds();
		boolQuery.mustNot(QueryBuilders.termsQuery("user_id",internalUserIds));
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}

	/**
	 * 查询内部用户
	 * @return
	 */
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }
    
}
