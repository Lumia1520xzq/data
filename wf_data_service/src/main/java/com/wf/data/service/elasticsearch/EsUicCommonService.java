package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.constants.UserGroupContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.entity.dto.UicDto;
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
 * 
 * @author jianjian.huang
 * @date 2017年8月23日
 */

@Service
public class EsUicCommonService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private UicGroupService uicGroupService;
	
	/**
	 * 获取日活
	 * @param note
	 * @return
	 */
	public List<Long> getDauList(UicDto note){
		List<Long> list=new ArrayList<Long>();
		AggregationBuilder aggsBuilder= EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
		Aggregations aggs = esClientFactory.getAggregation(
		EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT,aggsBuilder,getQuery(note));
		LongTerms agg = (LongTerms)aggs.get("userCount");
		Iterator<Bucket> it = agg.getBuckets().iterator();
		while (it.hasNext()) {
			Bucket buck = it.next();
			list.add((Long) buck.getKey());
		}
		return list;
	}
	
	
	//投注用户查询条件
	private QueryBuilder getQuery(UicDto note) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		
		if (note.getGameType() != null && note.getGameType() != 0) {
			map.put("game_type", note.getGameType());
		}
		map.put("delete_flag", 0);
		map.put("burying_type",8);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		
		boolQuery.mustNot(QueryBuilders.termsQuery("user_id", getInternalUserIds()));

		if (note.getBeginDate() != null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(note.getBeginDate(),DateUtils.DATE_TIME_PATTERN)));
		}
		if (note.getEndDate() != null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lt(DateUtils.formatUTCDate(note.getEndDate(),DateUtils.DATE_TIME_PATTERN)));
		}
		
		if (note.getChannelId() != null) {
			boolQuery.must(QueryBuilders.termQuery("channel_id", note.getChannelId()));
		} else {
			if (note.getParentId() != null) {
				BoolQueryBuilder likeQuery = QueryBuilders.boolQuery();
				likeQuery.should(QueryBuilders.termQuery("channel_id", note.getParentId()));
				String channelIdStart = note.getParentId().toString() + "001";
				String channelIdEnd = note.getParentId().toString() + "100";
				likeQuery.should(QueryBuilders.rangeQuery("channel_id").gte(Long.parseLong(channelIdStart)).lte(Long.parseLong(channelIdEnd)));
				boolQuery.must(likeQuery);
			}
		}
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
