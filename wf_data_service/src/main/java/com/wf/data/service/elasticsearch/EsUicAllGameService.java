package com.wf.data.service.elasticsearch;

import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
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
 * 
 * @author jianjian.huang
 * @date 2017年8月22日
 */

@Service
public class EsUicAllGameService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;

	// 1、新增用户
	public Integer getNewUser(Integer gameType,String date){
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		List<Long> oldUsers=getUserIds(aggsBuilder,gameType,null,date+" 00:00:00", BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> newUsers=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		int count=getNewUserCount(oldUsers, newUsers);
		return count;
	}
	
	// 2、活跃用户(游戏)
	public Integer getActiveUser(Integer gameType,String date){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> activeUsers=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		return activeUsers.size();
	}
	
	// 3、平台日活(游戏)
	public Integer getDailyActive(String date){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> dailyActive=getUserIds(aggsBuilder,null,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		return dailyActive.size();
	}
	
	// 5、累计用户
	public Integer getSumUser(Integer gameType){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		List<Long> sumUser=getUserIds(aggsBuilder,gameType,null,null,BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		return sumUser.size();
	}
	
	// 新用户中的投注人数
	public Integer getNewBettingUser(Integer gameType,String date){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		List<Long> oldUsers=getUserIds(aggsBuilder,gameType,null,date+" 00:00:00",BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> newUsers=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		//新增用户
		List<Long> newUserList=getNewUserList(oldUsers, newUsers);
		//当天投注人数
		List<Long> dailyBetting=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_USER_BEATING);
		if(CollectionUtils.isEmpty(newUserList)||CollectionUtils.isEmpty(dailyBetting)){
			return 0;
		}
		int count=0;
		for(Long user:newUserList){
			if(dailyBetting.contains(user)){
				count++;
			}
		}
		return count;
	}
	
	// 新增次日留存
	public String getRemainRate(Integer gameType,String date){
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		List<Long> oldUsers=getUserIds(aggsBuilder,gameType,null,date+" 00:00:00",BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date),1);
		List<Long> newUsers=getUserIds(aggsBuilder,gameType,date+" 00:00:00",DateUtils.formatDateTime(nextDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		//新增用户
		List<Long> newUserList=getNewUserList(oldUsers, newUsers);
		//次日活跃用户
		Date nextTwoDate = DateUtils.getNextDate(DateUtils.parseDate(date),2);
		List<Long> nextDayActive=getUserIds(
		aggsBuilder,gameType,DateUtils.formatDateTime(nextDate),DateUtils.formatDateTime(nextTwoDate),BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
		if(CollectionUtils.isEmpty(newUserList)||CollectionUtils.isEmpty(nextDayActive)){
			return "0%";
		}
		int count=0;
		for(Long user:newUserList){
			if(nextDayActive.contains(user)){
				count++;
			}
		}
		int newUserCount=newUserList.size();
		return NumberUtils.format(BigDecimalUtil.div(count,newUserCount,4),"#.##%");
	}
	
	
	private List<Long> getUserIds(AggregationBuilder aggsBuilder,Integer gameType,String begin,String end,Integer buryingType){
		List<Long> list=new ArrayList<Long>();
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,aggsBuilder,getActiveQuery(gameType,begin,end,buryingType));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		Iterator<Bucket> it=agg.getBuckets().iterator();
		while(it.hasNext()) {
			Bucket buck=it.next();
			list.add((Long)buck.getKey());
		}
		return list;
	}
	
	//计算新增用户
	private int getNewUserCount(List<Long> oldUsers, List<Long> newUsers) {
		if(CollectionUtils.isEmpty(newUsers)){
			return 0;
		}
		if(CollectionUtils.isEmpty(oldUsers)) {
			return newUsers.size();
		}
		int count=0;
		for(Long user:newUsers) {
			 if(!oldUsers.contains(user)) {
				 count++;
			 }
		}
		return count;
	}
	
	//新增用户list
	private List<Long> getNewUserList(List<Long> oldUsers, List<Long> newUsers) {
		List<Long> list=new ArrayList<Long>();
		if(CollectionUtils.isEmpty(newUsers)){
			return list;
		}
		if(CollectionUtils.isEmpty(oldUsers)) {
			return list;
		}
		for(Long user:newUsers) {
			 if(!oldUsers.contains(user)) {
				 list.add(user);
			 }
		}
		return list;
	}

	// 日活用户查询条件
	private QueryBuilder getActiveQuery(Integer gameType,String beginTime,String endTime,Integer buryingType) {
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
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}


}
