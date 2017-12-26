package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.constants.TransactionContents;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.UicGroupService;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/** 
 * 乐赢三张每小时邮件
 * @author jianjian.huang
 * 2017年12月12日
 */

@Service
public class EsTcardDailyService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private UicGroupService uicGroupService;

	/**
     * 投注用户ID
	 */
	public List<Long> getBettingUserIds (Map<String,String> params){
		List<Long> list = new ArrayList<>();
		AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userIds", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getBettingQuery(params));
		LongTerms agg = (LongTerms) aggs.get("userIds");
		Iterator<Terms.Bucket> it = agg.getBuckets().iterator();
		while (it.hasNext()) {
			Terms.Bucket buck = it.next();
			list.add((Long) buck.getKey());
		}
		return list;
	}

	/**
	 * 投注笔数
	 */
	public Integer getBettingCount(Map<String,String> params) {
		List<TransChangeNote> note = esClientFactory.list(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, getBettingQuery(params), 0, 5000000, TransChangeNote.class);
		return note.size();
	}

    /**
	 * 投注流水
	 */
	public Double getBettingAmt(Map<String,String> params) {
		AggregationBuilder aggsBuilder = EsQueryBuilders.sumAggregation("businessAmount","change_money");
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getBettingQuery(params));
		Map<String, Aggregation> aggMap = aggs.asMap();
		Sum sum = (Sum) aggMap.get("businessAmount");
		return sum.getValue();
	}

    /**
	 * 返奖流水
	 */
	public Double getAwardAmt(Map<String,String> params) {
		AggregationBuilder aggsBuilder = EsQueryBuilders.sumAggregation("businessAmount","change_money");
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getAwardQuery(params));
		Map<String, Aggregation> aggMap = aggs.asMap();
		Sum sum = (Sum) aggMap.get("businessAmount");  
		return sum.getValue();
	}

    /**
     * 三张投注条件
	 */
	private QueryBuilder getBettingQuery(Map<String,String> params) {
		Map<String, Object> map = new HashMap<>(4);
		QueryBuilder query;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		List<Integer> inBizs = Arrays.asList(
		  TransactionContents.BUSSINESS_TYPE_TCARD_SERVICE_MONEY, TransactionContents.BUSSINESS_TYPE_TCARD_BASE_RATE,
		  TransactionContents.BUSSINESS_TYPE_TCARD_CALL, TransactionContents.BUSSINESS_TYPE_TCARD_FIGHT
		  );
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		if(MapUtils.isNotEmpty(params)){
			String beginDate = params.get("beginDate");
			String endDate = params.get("endDate");
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginDate,DateUtils.DATE_TIME_PATTERN)));
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endDate,DateUtils.DATE_TIME_PATTERN)));
		}
		//剔除内部用户
		List<Long> internalUserIds = getInternalUserIds();
		boolQuery.mustNot(QueryBuilders.termsQuery("user_id",internalUserIds));
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}

	/**
	 * 三张返奖条件
	 */
	private QueryBuilder getAwardQuery(Map<String,String> params) {
		Map<String, Object> map = new HashMap<>();
		QueryBuilder query;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		List<Integer> inBizs = Arrays.asList(TransactionContents.BUSSINESS_TYPE_TCARD_WIN);
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		if(MapUtils.isNotEmpty(params)){
			String beginDate = params.get("beginDate");
			String endDate = params.get("endDate");
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginDate,DateUtils.DATE_TIME_PATTERN)));
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endDate,DateUtils.DATE_TIME_PATTERN)));
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
     */
    private List<Long> getInternalUserIds(){
		return uicGroupService.findGroupUsers(String.valueOf(UserGroupContents.INTERNAL_LIST_GROUP));
    }
    
}
