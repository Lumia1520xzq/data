package com.wf.data.service.elasticsearch;

import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.ChannelInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 某段时间的新用户(通用)
 * @author jianjian.huang
 * @date 2017年9月7日
 */

@Service
public class EsNewUserService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private ChannelInfoService channelService;
	
	//查询新增用户id列表
	public List<Long> getNewUserIdList(String beginDate,String endDate,Long parentId,Long channelId){
	List<Long> ids=new ArrayList<Long>();	
	List<UicUser> users = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getQuery(beginDate,endDate,parentId,channelId),0,100000,UicUser.class);
	if(CollectionUtils.isNotEmpty(users)){
		for(UicUser user:users){
			ids.add(user.getId());
		}
	}
	return ids;
	}
	
	//查询新用户的注册数
	public Integer getNewUserCount(String beginDate,String endDate,Long parentId,Long channelId){
	List<UicUser> list = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getQuery(beginDate,endDate,parentId,channelId),0,100000,UicUser.class);
	Integer count = CollectionUtils.isEmpty(list)?0:list.size();
	return count;
	}
	

	//新增用户查询条件
	private QueryBuilder getQuery(String beginDate,String endDate,Long parentId,Long channelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryBuilder query = null;
		map.put("delete_flag", 0);
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		if (beginDate!=null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginDate+" 00:00:00",DateUtils.DATE_TIME_PATTERN)));
		}
		if (endDate!=null) {
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(endDate+" 23:59:59",DateUtils.DATE_TIME_PATTERN)));
		}
		if (channelId != null) {
			boolQuery.must(QueryBuilders.termQuery("reg_channel_id", channelId));
		} else {
			if (parentId != null) {
				List<Long> channelIds = channelService.findSubChannelIds(parentId);
				channelIds.add(0, parentId);
				boolQuery.must(QueryBuilders.termsQuery("reg_channel_id", channelIds));
			}
		}
		query = boolQuery;
		logger.debug("query" + query);
		return query;
	}
	
}