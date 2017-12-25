package com.wf.data.service.elasticsearch;

import com.google.common.collect.Lists;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.data.common.constants.BuryingPointContents;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.uic.entity.UicUser;
import com.wf.data.service.ChannelInfoService;
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

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * @author jianjian.huang
 * @date 2017年9月4日
 */

@Service
public class EsUicChannelService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;
	@Resource
	private ChannelInfoService channelInfoService;

	// 1、新增用户
	public Integer getNewUser(String date,Long parentId,Long channelId){
		List<UicUser> users = esClientFactory.list(EsContents.UIC_USER,EsContents.UIC_USER,getUicUserQuery(date,parentId,channelId),0, 10000,UicUser.class);
		return users.size();
	}

	// 2、活跃用户(游戏)
	public Integer getActiveUser(String date,Long parentId,Long channelId){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> activeUsers=getUserIds(aggsBuilder,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE,parentId,channelId);
		return activeUsers.size();
	}

	// 新用户中的投注人数
	public Integer getNewBettingUser(String date,Long parentId,Long channelId){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		//新增用户
		List<UicUser> newUserList=esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getUicUserQuery(date,parentId,channelId),0, 10000,UicUser.class);
		//当天投注人数
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> dailyBetting=getUserIds
				(aggsBuilder,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_USER_BEATING,parentId,channelId);
		if(CollectionUtils.isEmpty(newUserList)||CollectionUtils.isEmpty(dailyBetting)){
			return 0;
		}
		int count=0;
		for(UicUser user:newUserList){
			if(dailyBetting.contains(user.getId())){
				count++;
			}
		}
		return count;
	}

	// 新增次日留存
	public String getRemainRate(String date,Long parentId,Long channelId){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		//新增用户
		List<UicUser> newUserList=esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER,getUicUserQuery(date,parentId,channelId),0, 10000,UicUser.class);
		//次日活跃用户
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		Date nextTwoDate = DateUtils.getNextDate(DateUtils.parseDate(date),2);
		List<Long> nextDayActive=getUserIds(
				aggsBuilder,DateUtils.formatDateTime(nextDate),DateUtils.formatDateTime(nextTwoDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE,parentId,channelId);
		if(CollectionUtils.isEmpty(newUserList)||CollectionUtils.isEmpty(nextDayActive)){
			return "0%";
		}
		int count=0;
		for(UicUser user:newUserList){
			if(nextDayActive.contains(user.getId())){
				count++;
			}
		}
		int newUserCount=newUserList.size();
		return NumberUtils.format(BigDecimalUtil.div(count,newUserCount,4),"#.##%");
	}


	private List<Long> getUserIds(AggregationBuilder aggsBuilder,String begin,String end,Integer buryingType,Long parentId,Long channelId){
		List<Long> list=new ArrayList<Long>();
		Aggregations aggs = esClientFactory.getAggregation(
				EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,aggsBuilder,getActiveQuery(begin,end,buryingType,parentId,channelId));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		Iterator<Bucket> it=agg.getBuckets().iterator();
		while(it.hasNext()) {
			Bucket buck=it.next();
			list.add((Long)buck.getKey());
		}
		return list;
	}

	// 日活用户查询条件
	private QueryBuilder getActiveQuery(String beginTime,String endTime,Integer buryingType,Long parentId,Long channelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		map.put("delete_flag", 0);
		map.put("burying_type",buryingType);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		if(beginTime!=null){
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
		}
		if(endTime!=null){
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lt(DateUtils.formatUTCDate(endTime,DateUtils.DATE_TIME_PATTERN)));
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
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}

	private QueryBuilder getUicUserQuery(String date,Long parentId,Long channelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		if (date!=null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(date+" 00:00:00",DateUtils.DATE_TIME_PATTERN)));
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(date+" 23:59:59",DateUtils.DATE_TIME_PATTERN)));
		}
		if (channelId != null) {
			boolQuery.must(QueryBuilders.termQuery("reg_channel_id", channelId));
		}else {
			if (parentId != null) {
				List<ChannelInfo> dtoList = channelInfoService.findSubChannel(parentId);
				List<Long> channelIds = Lists.newArrayList();
				for(ChannelInfo dto : dtoList){
					channelIds.add(dto.getId());
				}
				channelIds.add(0, parentId);
				boolQuery.must(QueryBuilders.termsQuery("reg_channel_id", channelIds));
			}
		}
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}
}

