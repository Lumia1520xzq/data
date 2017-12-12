package com.wf.data.common.utils.elasticsearch;

import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.Map;

public class EsQueryBuilders {
	/**
	 * match query 单个匹配 
	 * @param field
	 * @param value
	 * @return
	 */
    public static MatchQueryBuilder matchQuery(String field, Object value) {
        return QueryBuilders.matchQuery(field, value);
    } 
    /**
     * 多条件查询
     * @param map
     * @return
     */
	public static BoolQueryBuilder booleanQuery(Map<String, Object> map) {
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			builder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
		}
		return builder;
	}

	public static MatchAllQueryBuilder matchAllQuery() {
		
        return QueryBuilders.matchAllQuery();
    } 
	
	/**
	 * term 查询
	 * @param field
	 * @param value
	 * @return
	 */
	public static TermQueryBuilder termQuery(String field, Object value){
		return QueryBuilders.termQuery(field, value);
	}
	
	/**
	 * terms多条件查询
	 * @param field
	 * @param value
	 * @return
	 */
	public static TermsQueryBuilder termsQuery(String field, Object... value){
		return QueryBuilders.termsQuery(field, value);
	}
	
	/**
	 * 匹配字段值大于或等于
	 * @param field
	 * @param value
	 * @return
	 */
	public static RangeQueryBuilder gteQuery(String field, Object value){
		return QueryBuilders.rangeQuery(field).gte(value);
	}
	
	/**
	 * 范围查询将匹配字段值大于此参数值
	 * @param field
	 * @param value
	 * @return
	 */
	public static RangeQueryBuilder gtQuery(String field, Object value){
		return QueryBuilders.rangeQuery(field).gt(value);
	}
	
	/**
	 * 范围查询将匹配字段值小于或等于此参数值
	 * @param field
	 * @param value
	 * @return
	 */
	public static RangeQueryBuilder lteQuery(String field, Object value){
		return QueryBuilders.rangeQuery(field).lte(value);
	}
	
	/**
	 * 范围查询将匹配字段值小于此参数值
	 * @param field
	 * @param value
	 * @return
	 */
	public static RangeQueryBuilder ltQuery(String field, Object value){
		return QueryBuilders.rangeQuery(field).lt(value);
	}
	/**
	 * group聚合
	 * @param name 返回值名称
	 * @param field group字段
	 * @param size 条数
	 * @return
	 */
	public static AggregationBuilder addAggregation(String name, String field, Integer size){
		if(size == null) {size = 10;}
		return AggregationBuilders.terms(name).field(field).size(size);
	}

	public static AggregationBuilder addAggregationTermAsc(String name, String field, Integer size,boolean asc){
		if(size == null) {size = 10;}
		return AggregationBuilders.terms(name).field(field).size(size).order(Terms.Order.term(asc));
	}
	
	
	public static AggregationBuilder sumAggregation(String name, String field){
		return AggregationBuilders.sum(name).field(field);
	}

	public static AggregationBuilder minAggregation(String name, String field){
		return AggregationBuilders.min(name).field(field);
	}

	public static AggregationBuilder scriptedMetric(String name, Script mapScript){
		Script initScript=new Script("params._agg.transactions=[]");
		Script combineScript=new Script("double total = 0; for (t in params._agg.transactions) { total += t } return total");
		Script reduceScript=new Script("double total = 0; for (a in params._aggs) { total += a } return total");
		return AggregationBuilders.scriptedMetric(name).initScript(initScript).mapScript(mapScript)
			   .combineScript(combineScript).reduceScript(reduceScript);
	}

}