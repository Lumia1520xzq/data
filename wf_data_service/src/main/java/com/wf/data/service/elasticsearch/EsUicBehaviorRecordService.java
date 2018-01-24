package com.wf.data.service.elasticsearch;


import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.controller.request.BehaviorRecordReq;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.BehaviorRecordResp;
import com.wf.data.service.BehaviorTypeService;
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

import java.util.*;


/**
 * 
 * @author huangjianjian
 * @date 2017年9月6日
 */

@Service
public class EsUicBehaviorRecordService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private BehaviorTypeService behaviorTypeService;
	@Autowired
	private ChannelInfoService channelInfoService;
	@Autowired
	private EsNewUserService newUserService;

	public List<BehaviorRecordResp> getBehaviorList(BehaviorRecordReq note){
		List<BehaviorRecordResp> list = new ArrayList<BehaviorRecordResp>();
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("channel","channel_id", 1000000)
		.subAggregation(EsQueryBuilders.addAggregation("event","behavior_event_id", 1000000)
		.subAggregation(EsQueryBuilders.addAggregation("user","user_id", 1000000)));
		logger.debug("aggs:"+aggsBuilder);

		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BEHAVIOR_RECORD, EsContents.UIC_BEHAVIOR_RECORD,aggsBuilder,getQuery(note));

		LongTerms agg_channel = (LongTerms)aggs.get("channel");
		Iterator<Bucket> it_channel= agg_channel.getBuckets().iterator();
		while(it_channel.hasNext()) {
			Bucket buck_channel = it_channel.next();
			Long channelId =(Long)buck_channel.getKey();
			LongTerms agg_event=(LongTerms)buck_channel.getAggregations().get("event");
			Iterator<Bucket> it_event= agg_event.getBuckets().iterator();
			while (it_event.hasNext()){				
				Bucket buck_event = it_event.next();
				Long eventId =(Long)buck_event.getKey();
				//触发数量
				Long behaviorCount= buck_event.getDocCount();
				LongTerms agg_user=(LongTerms)buck_event.getAggregations().get("user");
				//触发用户数 
				int behaviorUserCount = agg_user.getBuckets().size();
				BehaviorRecordResp resp=new BehaviorRecordResp();
				//1、事件名称
				String behaviorName=behaviorTypeService.findEventNameById(eventId);
				resp.setBehaviorName(behaviorName);
				//2、事件ID
				resp.setBehaviorEventId(eventId);
				//3、触发数量(日均)
				resp.setBehaviorCount(behaviorCount);
				//4、触发用户数(日均)
				resp.setBehaviorUserCount(behaviorUserCount);
				//5、所属渠道
				ChannelInfo info = channelInfoService.get(channelId);
				if (info == null) {info = new ChannelInfo();}
				resp.setChannelName(info.getName());
				list.add(resp);
			}
		}
		return list;
	}

	
	//新版埋点查询条件
	private QueryBuilder getQuery(BehaviorRecordReq note) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		String beginDate=note.getBeginDate();
		String endDate=note.getEndDate();
		Long parentId=note.getParentId();
		Long channelId=note.getChannelId();
		if (beginDate!=null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginDate+" 00:00:00", DateUtils.DATE_TIME_PATTERN)));
		}
		if (endDate!=null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endDate+" 23:59:59",DateUtils.DATE_TIME_PATTERN)));
		}
		if (channelId != null) {
			boolQuery.must(QueryBuilders.termQuery("channel_id", channelId));
		} 
		if (parentId != null) {
			boolQuery.must(QueryBuilders.termQuery("parent_channel_id",parentId));
		} 
		Long parentEventId=note.getParentEventId();
		Long eventId = note.getEventId();
		if(eventId!=null){
			boolQuery.must(QueryBuilders.termQuery("behavior_event_id",eventId));
		}
		else {
		if(parentEventId!=null){
			List<Long> eventIds=behaviorTypeService.findEventIds(parentEventId);
			boolQuery.must(QueryBuilders.termsQuery("behavior_event_id",eventIds));
		}
		}
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}
	
	//新增用户埋点
	public List<BehaviorRecordResp> getNewBehaviorList(BehaviorRecordReq note){
		String beginDate=note.getBeginDate();
		String endDate=note.getEndDate();
		Long parentId=note.getParentId();
		List<Long> newUserIds = newUserService.getNewUserIdList(beginDate,endDate,parentId,note.getChannelId());
		List<BehaviorRecordResp> list = new ArrayList<BehaviorRecordResp>();
		if(CollectionUtils.isEmpty(newUserIds)) {
			return list;
		}
// 		int days=DateUtils.getDateInterval(beginDate,endDate)+1;
		AggregationBuilder aggsBuilder=EsQueryBuilders.addAggregation("channel","channel_id", 1000000)
		.subAggregation(EsQueryBuilders.addAggregation("event","behavior_event_id", 1000000)
		.subAggregation(EsQueryBuilders.addAggregation("user","user_id", 1000000)));
		logger.debug("aggs:"+aggsBuilder);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BEHAVIOR_RECORD, EsContents.UIC_BEHAVIOR_RECORD,aggsBuilder,getQuery(note));
		LongTerms agg_channel = (LongTerms)aggs.get("channel");
		Iterator<Bucket> it_channel= agg_channel.getBuckets().iterator();
		while(it_channel.hasNext()) {
			Bucket buck_channel = it_channel.next();
			Long channelId =(Long)buck_channel.getKey();
			LongTerms agg_event=(LongTerms)buck_channel.getAggregations().get("event");
			Iterator<Bucket> it_event= agg_event.getBuckets().iterator();
			while (it_event.hasNext()){				
				Bucket buck_event = it_event.next();
				Long eventId =(Long)buck_event.getKey();
				LongTerms agg_user=(LongTerms)buck_event.getAggregations().get("user");
				Iterator<Bucket> it_user= agg_user.getBuckets().iterator();
				Long behaviorCount=0L;
				int behaviorUserCount=0;
				while(it_user.hasNext()){
					Bucket buck_user=it_user.next();
					Long userId = (Long)buck_user.getKey();
 					if(newUserIds.contains(userId)){
 						//触发数量
 						behaviorCount+=buck_user.getDocCount();
 						//触发用户数 
 						behaviorUserCount++; 
					}
				}
				BehaviorRecordResp resp=new BehaviorRecordResp();
				//1、事件名称
				String behaviorName=behaviorTypeService.findEventNameById(eventId);
				resp.setBehaviorName(behaviorName);
				//2、事件ID
				resp.setBehaviorEventId(eventId);
				//3、触发数量(日均)
//				resp.setBehaviorCount((long)Math.ceil(BigDecimalUtil.div(behaviorCount,days)));
				resp.setBehaviorCount(behaviorCount);
				//4、触发用户数(日均)
//				resp.setBehaviorUserCount((int)Math.ceil(BigDecimalUtil.div(behaviorUserCount,days)));
				resp.setBehaviorUserCount(behaviorUserCount);
				//5、所属渠道
				ChannelInfo info = channelInfoService.get(channelId);
				resp.setChannelName(info.getName());
				list.add(resp);
			}
		}
		return list;
	}

}
