package com.wf.data.common.utils.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.core.persistence.Page;
import com.wf.data.common.utils.JsonHelper;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
    private GenericObjectPool<EsClient> pool;

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
        EsClient esClient = null;
        try {
            esClient = pool.borrowObject();
            return esClient;
        } catch (Exception e) {
            throw new RuntimeException("线程池异常", e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);// 整个池最大值
        config.setMaxIdle(8);// 最大空闲
        config.setMinIdle(0);// 最小空闲
        config.setMaxWaitMillis(2000);// 最大等待时间，-1表示一直等
        config.setBlockWhenExhausted(true);// 当对象池没有空闲对象时，新的获取对象的请求是否阻塞。true阻塞。默认值是true
        config.setTestOnBorrow(false);// 在从对象池获取对象时是否检测对象有效，true是；默认值是false
        config.setTestOnReturn(false);// 在向对象池中归还对象时是否检测对象有效，true是，默认值是false
        config.setTestWhileIdle(false);// 在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。true是，默认值是false
        config.setMinEvictableIdleTimeMillis(3000); // 可发呆的时间
        config.setTestWhileIdle(false); // 发呆过长移除的时候是否test一下先

        pool = new GenericObjectPool<EsClient>(this.poolFactory,config);


        logger.info("连接池已经初始化");
    }
}
