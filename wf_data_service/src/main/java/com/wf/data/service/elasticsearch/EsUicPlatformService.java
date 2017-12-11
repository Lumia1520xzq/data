package com.wf.data.service.elasticsearch;

import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.NumberUtils;
import com.wf.data.common.constants.BuryingPointContents;
import com.wf.data.common.constants.EsContents;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.common.utils.elasticsearch.EsClientFactory;
import com.wf.data.common.utils.elasticsearch.EsQueryBuilders;
import com.wf.data.dao.uic.entity.UicUser;
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
 * @author jianjian.huang
 * 2017年12月8日
 */

@Service
public class EsUicPlatformService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EsClientFactory esClientFactory;


    public UicUser get(final Long id) {
        return esClientFactory.get(EsContents.UIC_USER ,EsContents.UIC_USER, id.toString(),UicUser.class);
    }

    /**
     * 当日新增用户ID的list<Long>
     */
    public List<Long> getNewUserList(String date) {
        List<Long> list = new ArrayList<>();
        List<UicUser> users = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getUicUserQuery(date), 0, 10000, UicUser.class);
        if (CollectionUtils.isNotEmpty(users)) {
            for (UicUser user : users) {
                list.add(user.getId());
            }
        }
        return list;
    }

    /**
     * 新增用户数量
     *
     */
    public Integer getNewUser(String date) {
        List<UicUser> users = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getUicUserQuery(date), 0, 10000, UicUser.class);
        return users.size();
    }


    public List<UicUser> getNewUserInfoList(String date, Long channelId) {
        return esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, userQuery(date, channelId), 0, 1000000, UicUser.class);
    }

    /**
     * 游戏活跃用户数
     *
     */
    public Integer getActiveUser(String date) {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date), 1);
        List<Long> activeUsers = getUserIds(aggsBuilder, null, date + " 00:00:00", DateUtils.formatDateTime(nextDate), BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
        return activeUsers.size();
    }

    /**
     * 累计用户数
     *
     */
    public Integer getSumUser() {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        List<Long> sumUser = getUserIds(aggsBuilder, null, null, null, BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
        return sumUser.size();
    }

    /**
     * 累计充值人数
     *
     */
    public Integer getSumRechargeUser() {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        List<Long> sumUser = getUserIds(aggsBuilder, null, null, null, BuryingPointContents.POINT_TYPE_RECHARGE_SUCCESS);
        return sumUser.size();
    }

    /**
     * 新用户中的投注人数
     */
    public Integer getNewBettingUser(String date) {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        //新增用户
        List<UicUser> newUserList = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getUicUserQuery(date), 0, 10000, UicUser.class);
        //当天投注人数
        Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date), 1);
        List<Long> dailyBetting = getUserIds(aggsBuilder, null, date + " 00:00:00", DateUtils.formatDateTime(nextDate), BuryingPointContents.POINT_TYPE_USER_BEATING);
        if (CollectionUtils.isEmpty(newUserList) || CollectionUtils.isEmpty(dailyBetting)) {
            return 0;
        }
        int count = 0;
        for (UicUser user : newUserList) {
            if (dailyBetting.contains(user.getId())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 新增次日留存
     *
     */
    public String getRemainRate(String date) {
        AggregationBuilder aggsBuilder = EsQueryBuilders.addAggregation("userCount", "user_id", 1000000);
        //新增用户
        List<UicUser> newUserList = esClientFactory.list(EsContents.UIC_USER, EsContents.UIC_USER, getUicUserQuery(date), 0, 10000, UicUser.class);
        //次日活跃用户
        Date nextDate = DateUtils.getNextDate(DateUtils.parseDate(date), 1);
        Date nextTwoDate = DateUtils.getNextDate(DateUtils.parseDate(date), 2);
        List<Long> nextDayActive = getUserIds(
                aggsBuilder, null, DateUtils.formatDateTime(nextDate), DateUtils.formatDateTime(nextTwoDate), BuryingPointContents.POINT_TYPE_GAME_MAIN_PAGE);
        if (CollectionUtils.isEmpty(newUserList) || CollectionUtils.isEmpty(nextDayActive)) {
            return "0%";
        }
        int count = 0;
        for (UicUser user : newUserList) {
            if (nextDayActive.contains(user.getId())) {
                count++;
            }
        }
        int newUserCount = newUserList.size();
        return NumberUtils.format(BigDecimalUtil.div(count, newUserCount, 4), "#.##%");
    }

    private List<Long> getUserIds(AggregationBuilder aggsBuilder, Integer gameType, String begin, String end, Integer buryingType) {
        List<Long> list = new ArrayList<>();
        Aggregations aggs = esClientFactory.getAggregation(
                EsContents.UIC_BURYING_POINT, EsContents.UIC_BURYING_POINT, aggsBuilder, getActiveQuery(gameType, begin, end, buryingType));
        LongTerms agg = (LongTerms) aggs.get("userCount");
        Iterator<Bucket> it = agg.getBuckets().iterator();
        while (it.hasNext()) {
            Bucket buck = it.next();
            list.add((Long) buck.getKey());
        }
        return list;
    }


    /**
     * 日活用户查询条件
     */
    private QueryBuilder getActiveQuery(Integer gameType, String beginTime, String endTime, Integer buryingType) {
        Map<String, Object> map = new HashMap<>();
        QueryBuilder query;
        if (gameType != null) {
            map.put("game_type", gameType);
        }
        map.put("delete_flag", 0);
        map.put("burying_type", buryingType);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if (beginTime != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(beginTime, DateUtils.DATE_TIME_PATTERN)));
        }
        if (endTime != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lt(DateUtils.formatUTCDate(endTime, DateUtils.DATE_TIME_PATTERN)));
        }
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }

    private QueryBuilder getUicUserQuery(String date) {
        Map<String, Object> map = new HashMap<>();
        QueryBuilder query;
        map.put("delete_flag", 0);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if (date != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(date + " 00:00:00", DateUtils.DATE_TIME_PATTERN)));
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(date + " 23:59:59", DateUtils.DATE_TIME_PATTERN)));
        }
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }

    private QueryBuilder userQuery(String date, Long channelId) {
        Map<String, Object> map = new HashMap<>();
        QueryBuilder query;
        map.put("delete_flag", 0);
        BoolQueryBuilder boolQuery = EsQueryBuilders.booleanQuery(map);
        if (date != null) {
            boolQuery.must(QueryBuilders.rangeQuery("create_time").gte(DateUtils.formatUTCDate(date + " 00:00:00", DateUtils.DATE_TIME_PATTERN)));
            boolQuery.must(QueryBuilders.rangeQuery("create_time").lte(DateUtils.formatUTCDate(date + " 23:59:59", DateUtils.DATE_TIME_PATTERN)));
        }
        if (channelId != null) {
            boolQuery.must(QueryBuilders.termQuery("reg_channel_id", channelId));
        }
        query = boolQuery;
        logger.debug("query" + query);
        return query;
    }

}
