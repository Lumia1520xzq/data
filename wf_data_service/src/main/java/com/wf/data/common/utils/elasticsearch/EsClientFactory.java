package com.wf.data.common.utils.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.core.persistence.Page;
import com.wf.data.common.utils.JsonHelper;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengsheng.liu
 * @date 2017年6月20日
 */
@Component
public class EsClientFactory implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EsClientPoolFactory poolFactory;
    private GenericObjectPool pool;

    public <T> T get(String index, String type, String id, Class<T> clazz) {
        EsClient esClient = getESClient();
        T t = null;
        try {
            GetResponse getResponse = esClient.client.prepareGet().setIndex(index).setType(type).setId(id).get();
            String str = getResponse.getSourceAsString();
            JSONObject jsStr = JSONObject.parseObject(str);
            t = JSONObject.toJavaObject(jsStr, clazz);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("获取对象失败：", e);
        } finally {
            try {
                pool.returnObject(esClient);
            } catch (Exception e) {
            }
        }

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String index, String type, String id) {
        EsClient esClient = getESClient();
        GetResponse getResponse = esClient.client.prepareGet().setIndex(index).setType(type).setId(id).get();
        String str = getResponse.getSourceAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsStr;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            jsStr = mapper.readTree(str);
            map = JsonHelper.fromJson(jsStr, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pool.returnObject(esClient);
            } catch (Exception e) {
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMap(String index, String type, QueryBuilder query) {
        EsClient esClient = getESClient();
        List<Map<String, Object>> out = new ArrayList<>();
        SearchResponse response = esClient.client.prepareSearch(index).setTypes(type).setQuery(query).execute().actionGet();
        SearchHits resultHits = response.getHits();
        for (int i = 0; i < resultHits.getHits().length; i++) {
            String jsonStr = resultHits.getHits()[i].getSourceAsString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsStr;
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                jsStr = mapper.readTree(jsonStr);
                map = JsonHelper.fromJson(jsStr, Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            out.add(map);
        }
        try {
            pool.returnObject(esClient);
        } catch (Exception e) {
        }
        return out;
    }

    public <T> List<T> list(String index, String type, QueryBuilder query, int from, int size, Class<T> clazz) {
        EsClient esClient = getESClient();
        List<T> out = new ArrayList<>();
        try {
            SearchResponse response = esClient.client.prepareSearch(index).setTypes(type).setQuery(query).setFrom(from)
                    .setSize(size).execute().actionGet();
            SearchHits resultHits = response.getHits();
            for (int i = 0; i < resultHits.getHits().length; i++) {
                String jsonStr = resultHits.getHits()[i].getSourceAsString();
                JSONObject jsStr = JSONObject.parseObject(jsonStr);
                T t = JSONObject.toJavaObject(jsStr, clazz);
                out.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取list失败：", e);
        } finally {
            try {
                pool.returnObject(esClient);
            } catch (Exception e) {
            }
        }

        return out;
    }

    public <T> Page<T> findPage(String index, String type, QueryBuilder query, int from, int size, Class<T> clazz) {
        Page<T> page = new Page<>();
        EsClient esClient = getESClient();
        List<T> data = new ArrayList<>();
        long count = 0;
        try {
            SortBuilder<?> sortBuilder = SortBuilders.fieldSort("create_time").order(SortOrder.DESC);
            SearchResponse response = esClient.client.prepareSearch(index).setTypes(type).setQuery(query).addSort(sortBuilder)
                    .setFrom(from).setSize(size).execute().actionGet();
            SearchHits resultHits = response.getHits();
            // 获取总条数
            count = resultHits.getTotalHits();
            for (int i = 0; i < resultHits.getHits().length; i++) {
                String jsonStr = resultHits.getHits()[i].getSourceAsString();
                JSONObject jsStr = JSONObject.parseObject(jsonStr);
                T t = JSONObject.toJavaObject(jsStr, clazz);
                data.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取page失败：", e);
        } finally {
            try {
                pool.returnObject(esClient);
            } catch (Exception e) {
            }
        }
        page.setData(data);
        page.setCount(count);
        return page;
    }

    public Aggregations getAggregation(String index, String type, AggregationBuilder aggregation, QueryBuilder query) {
        EsClient esClient = getESClient();
        Aggregations aggs = null;
        try {
            SearchResponse response = esClient.client.prepareSearch(index).setTypes(type).setQuery(query)
                    .addAggregation(aggregation).execute().actionGet();
            logger.debug("query:" + query);
            logger.debug("aggregation:" + aggregation);
            aggs = response.getAggregations();
        } catch (Exception e) {
            throw new RuntimeException("获取Aggregation失败：", e);
        } finally {
            try {
                pool.returnObject(esClient);
            } catch (Exception e) {
            }
        }
        return aggs;
    }

    private EsClient getESClient() {
        try {
            return ((EsClient) pool.borrowObject());
        } catch (Exception e) {
            throw new RuntimeException("线程池异常", e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new GenericObjectPool(this.poolFactory);
        pool.setMaxActive(20); // 能从池中借出的对象的最大数目
        pool.setMaxIdle(20); // 池中可以空闲对象的最大数目
        pool.setMaxWait(100); //对象池空时调用borrowObject方法，最多等待多少毫秒
        pool.setTimeBetweenEvictionRunsMillis(600000);// 间隔每过多少毫秒进行一次后台对象清理的行动
        pool.setNumTestsPerEvictionRun(-1);// －1表示清理时检查所有线程
        pool.setMinEvictableIdleTimeMillis(3000);// 设定在进行后台对象清理时，休眠时间超过了3000毫秒的对象为过期
        logger.info("连接池已经初始化");
    }
}
