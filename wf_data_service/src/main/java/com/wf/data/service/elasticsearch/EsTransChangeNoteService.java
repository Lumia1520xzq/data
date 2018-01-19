package com.wf.data.service.elasticsearch;

import com.google.common.collect.Lists;
import com.wf.core.persistence.Page;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.ChannelInfoService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
 * 投注流水
 * @author jianjian.huang
 * 2018年1月19日
 */

@Service
public class EsTransChangeNoteService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EsClientFactory esClientFactory;
	@Autowired
	private ChannelInfoService channelInfoService;

	public Page<TransChangeNote> findPage(Integer start, Integer length, TransChangeNote note) {
		return esClientFactory.findPage(EsContents.TRANS_CHANGE_NOTE, EsContents.TRANS_CHANGE_NOTE, getQuery(note), start, length,
				TransChangeNote.class);
	}

	private QueryBuilder getQuery(TransChangeNote note){
		Map<String, Object> map = new HashMap<>(10);
		QueryBuilder query;
		if (note.getUserId() != null) {
			map.put("user_id", note.getUserId());
		}
		if (note.getChangeType() != null) {
			map.put("change_type", note.getChangeType());
		}

		if (note.getBusinessType() != null) {
			map.put("business_type", note.getBusinessType());
		}
		BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
		if (!"".equals(note.getBeginDate())) {
			String beginTime = DateUtils.formatUTCDate(note.getBeginDate(), DateUtils.DATE_T_PATTERN);
			boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(beginTime));
		}
		if (!"".equals(note.getEndDate())) {
			String endTime = DateUtils.formatUTCDate(note.getEndDate(), DateUtils.DATE_T_PATTERN);
			boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(endTime));
		}
		if (note.getChangeMoneyLow() != null) {
			boolQuery.must(QueryBuilders.rangeQuery("change_money").gte(note.getChangeMoneyLow()));
		}
		if (note.getChangeMoneyHigh() != null) {
			boolQuery.must(QueryBuilders.rangeQuery("change_money").lte(note.getChangeMoneyHigh()));
		}
		if (note.getChannelId() != null) {
			boolQuery.must(QueryBuilders.termQuery("channel_id", note.getChannelId()));
		} else {
			Long parentId = note.getParentId();
			if (parentId != null){
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
		logger.debug("query"+query);
		return query;
	}
}
