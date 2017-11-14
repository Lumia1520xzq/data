package com.wf.data.service.elasticsearch;

import com.wf.core.utils.type.StringUtils;
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
 * trans库common
 * @author chengsheng.liu
 * @date 2017年9月25日
 */
@Service
public class EsTransCommonService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private UicGroupService uicGroupService;

	//投注人数列表
	public List<Long> getBettingUserList(Map<String,Object> params) {
		List<Long> list = new ArrayList<Long>();
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE,aggsBuilder,getQuery(params));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		Iterator<Bucket> it = agg.getBuckets().iterator();
		while (it.hasNext()) {
			Bucket buck = it.next();
			list.add((Long) buck.getKey());
		}
		return list;
	}
	
	//投注条件
	private QueryBuilder getQuery(Map<String,Object> params) {
		QueryBuilder query = null;
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("delete_flag", 0));
		String beginTime = (String) params.get("beginTime");
		String endTime = (String) params.get("endTime");
		Long channelId = (Long)params.get("channelId");
		Long gameType = (Long)params.get("gameType");
		
		List<Integer> inBizs=new ArrayList<Integer>();
		if(gameType==null){
		          inBizs=Arrays.asList(
				  TransactionContents.BUSINESS_TYPE_BETTING_DART,TransactionContents.BUSINESS_TYPE_BETTING_CENCEL, //飞镖
				  TransactionContents.BUSINESS_TYPE_BETTING_BILLIARD,//桌球
				  TransactionContents.BUSINESS_TYPE_BETTING_WARS,//军团
				  TransactionContents.BUSINESS_TYPE_BETTING_ARROWS,//貂蝉保卫战 
				  TransactionContents.BUSINESS_TYPE_FOOTBALL_BETTING,//足球竞猜 
				  TransactionContents.BUSINESS_TYPE_MOTOR_BETTING,TransactionContents.BUSINESS_TYPE_MOTOR_BRTTING_CANCEL,//热血摩托 
				  TransactionContents.BUSINESS_TYPE_KINGDOM_BETTING,TransactionContents.BUSINESS_TYPE_KINGDOM_CANCEL, //多多三国
				  TransactionContents.BUSINESS_TYPE_FISH_FIRE_FISH //捕鱼
				  );
		}else if(gameType==1) {
				 inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_DART,TransactionContents.BUSINESS_TYPE_BETTING_CENCEL);
		}else if(gameType==2) {
			     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_BILLIARD);
		}
		else if(gameType==3) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_QUOITS);
	    }
		else if(gameType==4) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_WARS);
	    }else if(gameType==5) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_BETTING_ARROWS);
	    }else if(gameType==7) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FOOTBALL_BETTING);
	    }else if(gameType==8) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_MOTOR_BETTING,TransactionContents.BUSINESS_TYPE_MOTOR_BRTTING_CANCEL);
	    }else if(gameType==9) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_KINGDOM_BETTING,TransactionContents.BUSINESS_TYPE_KINGDOM_CANCEL);
	    }else if(gameType==10) {
		     inBizs=Arrays.asList(TransactionContents.BUSINESS_TYPE_FISH_FIRE_FISH);
	    }
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		if(StringUtils.isNotEmpty(beginTime))
		boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime, DateUtils.DATE_TIME_PATTERN)));
		if(StringUtils.isNotEmpty(endTime))
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
	
	
	//查询内部人员的Id
    private List<Long> getInternalUserIds(){
	List<Long> userIds = uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
	return userIds;
    }
    
}
