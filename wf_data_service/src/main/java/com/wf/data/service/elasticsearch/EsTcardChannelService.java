package com.wf.data.service.elasticsearch;

import com.google.common.collect.Lists;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.constants.TransactionContents;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.service.ChannelInfoService;
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

import javax.annotation.Resource;
import java.util.*;


/** 
 * 乐赢三张每日各渠道数据
 * @author jianjian.huang
 * 2017年12月12日
 */

@Service
public class EsTcardChannelService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Resource
	private ChannelInfoService channelInfoService;

	/**
	 * 投注用户(三张)
	 */
	public List<Long> getTcardBettingUsers (String date,Long parentId,Long channelId,List<Long> userIds) {
		List<Long> list = new ArrayList<>();
		AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userIds", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getTcardQuery(date,parentId,channelId,userIds,true));
		LongTerms agg = (LongTerms) aggs.get("userIds");
		List<Terms.Bucket> buckets = agg.getBuckets();
		for(Terms.Bucket buck:buckets) {
			list.add((Long)buck.getKey());
		}
		return list;
	}

	/**
	 * 投注流水
	 */
	public long getBettingAmt (String date,Long parentId,Long channelId,List<Long> userIds) {
		AggregationBuilder aggsBuilder = EsQueryBuilders.sumAggregation("businessAmount","change_money");
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getTcardQuery(date,parentId,channelId,userIds,true));
		Map<String, Aggregation> aggMap = aggs.asMap();
		Sum sum = (Sum) aggMap.get("businessAmount");
		return (long)sum.getValue();
	}

	/**
	 * 返奖流水
	 */
	public long getAwardAmt(String date,Long parentId,Long channelId,List<Long> userIds) {
		AggregationBuilder aggsBuilder = EsQueryBuilders.sumAggregation("businessAmount","change_money");
		Aggregations aggs = esClientFactory.getAggregation(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, aggsBuilder, getTcardQuery(date,parentId,channelId,userIds,false));
		Map<String, Aggregation> aggMap = aggs.asMap();
		Sum sum = (Sum) aggMap.get("businessAmount");
		return (long)sum.getValue();
	}

	/**
	 * 三张投注 & 返奖条件
	 */
	private QueryBuilder getTcardQuery(String date,Long parentId,Long channelId,List<Long> userIds,boolean betting) {
		Map<String, Object> map = new HashMap<>(4);
		QueryBuilder query;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		List<Integer> inBizs;
		if(betting) {
			inBizs = Arrays.asList(
					TransactionContents.BUSSINESS_TYPE_TCARD_SERVICE_MONEY, TransactionContents.BUSSINESS_TYPE_TCARD_BASE_RATE,
					TransactionContents.BUSSINESS_TYPE_TCARD_CALL, TransactionContents.BUSSINESS_TYPE_TCARD_FIGHT
			);
		} else {
			inBizs = Arrays.asList(TransactionContents.BUSSINESS_TYPE_TCARD_WIN);
		}
		boolQuery.must(QueryBuilders.termsQuery("business_type",inBizs));
		if(StringUtils.isNotEmpty(date)) {
			String beginDate = date + " 00:00:00";
			String endDate = date + " 23:59:59";
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(com.wf.data.common.utils.DateUtils.formatUTCDate(beginDate, com.wf.data.common.utils.DateUtils.DATE_TIME_PATTERN)));
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(com.wf.data.common.utils.DateUtils.formatUTCDate(endDate, com.wf.data.common.utils.DateUtils.DATE_TIME_PATTERN)));
		}
		if(channelId != null){
			boolQuery.must(QueryBuilders.termQuery("channel_id",channelId));
		}else {
			if (parentId != null) {
				List<ChannelInfo> dtoList = channelInfoService.findSubChannel(parentId);
				List<Long> channelIds = Lists.newArrayList();
				for(ChannelInfo dto : dtoList){
					channelIds.add(dto.getId());
				}
				channelIds.add(0, parentId);
				boolQuery.must(QueryBuilders.termsQuery("channel_id", channelIds));
			}
		}
		boolQuery.mustNot(QueryBuilders.termsQuery("user_id",userIds));
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}

}
