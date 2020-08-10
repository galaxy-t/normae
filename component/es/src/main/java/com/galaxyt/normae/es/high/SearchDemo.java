package com.galaxyt.normae.es.high;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ES 搜索 示例
 * @author zhouqi
 * @date 2020/6/8 17:08
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/8 17:08     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Component
public class SearchDemo {


    @Autowired
    private ESClient esClient;

    /**
     * 一些简单的搜索可以直接使用这种方式
     */
    public void search1() {

        /*
        构造参数不是必须的
        索引名称用来约束检索范围
        若不提供索引名称则检索全部索引
         */
        SearchRequest searchRequest = new SearchRequest("index1","index2");

        SearchSourceBuilder builder = new SearchSourceBuilder();    //检索条件构造器
        builder.query(QueryBuilders.matchAllQuery());               //设置检索全部 , 即返回全部的 Document

        //分页设置
        builder.from(0);    //从第几条开始查询 , 默认为 0 , 参考 MYSQL 的 LIMIT
        builder.size(5);    //一共查询多少条 , 默认为 10 , 参考 MYSQL 的 LIMIT

        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));   //设置一个默认的超时时间

        //builder.query(QueryBuilders.matchQuery("fieldName", "饭 诗"));       //模糊搜索 , 关键字以空格分隔 , 或的意思
        //builder.query(QueryBuilders.matchPhrasePrefixQuery("fieldName", "饭 诗")); //模糊检索 , 要求搜索 "饭 诗" 这个短语 , 而不是以空格分隔

        //也可以将 DSL 语句的 query 子语句直接写到 builder 中
        //此种形式建议直接使用模板查询
        //String queryStr = "{\"match\":{\"name\":\"周 李\"}}";
        //builder.query(QueryBuilders.wrapperQuery(queryStr));

        //设置某些字段高亮
        /*HighlightBuilder highlightBuilder = new HighlightBuilder();

        HighlightBuilder.Field highlightTitle1 = new HighlightBuilder.Field("fieldName1");
        highlightTitle1.highlighterType("unified");
        highlightBuilder.field(highlightTitle1);

        HighlightBuilder.Field highlightTitle2 = new HighlightBuilder.Field("fieldName2");
        highlightTitle2.highlighterType("unified");
        highlightBuilder.field(highlightTitle2);

        builder.highlighter(highlightBuilder);*/


        try {
            SearchResponse response = this.esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
            log.debug(response.toString());
            this.responseHandler(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 如果希望直接写 DSL 可以使用这种方式 , 简单粗暴\
     * 里面的代码也比较容易理解
     */
    public void search2() {

        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setRequest(new SearchRequest("indexName1", "indexName2"));      //设置 Index 的名称用来约束检索范围 , 若不设置则默认查询全局

        request.setScriptType(ScriptType.INLINE);
        String script = "{\"query\": {\"match\":{\"{{field}}\":\"{{value}}\"}},\"size\":\"{{size}}}\"}";
        request.setScript(script);

        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("field", "title");
        scriptParams.put("value", "elasticsearch");
        scriptParams.put("size", 5);
        request.setScriptParams(scriptParams);

        try {
            SearchTemplateResponse response = this.esClient.getClient().searchTemplate(request, RequestOptions.DEFAULT);
            this.responseHandler(response.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 预设一些模板 , 官方提供的文档说只能依靠 low rest client 中的 Request 来进行
     */
    public void registered() {

        //设置请求类型和模板名称 , 注意  _script/  是需要固定写死的
        Request request = new Request("POST", "_script/title_search");

        String script = "{\"script\":{\"lang\":\"mustache\",\"source\":{\"query\":{\"match\":{\"{{field}}\":\"{{value}}\"}},\"size\":\"{{size}}\"}}}";
        try {
            Response response = this.esClient.getClient().getLowLevelClient().performRequest(request);
            log.debug(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void search3() {

        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setRequest(new SearchRequest("indexName1", "indexName2"));      //设置 Index 的名称用来约束检索范围 , 若不设置则默认查询全局
        request.setScriptType(ScriptType.STORED);
        request.setScript("title_search");      //设置预设模板名称

        //设置参数
        Map<String, Object> params = new HashMap<>();
        params.put("field", "title");
        params.put("value", "elasticsearch");
        params.put("size", 5);
        request.setScriptParams(params);

        try {
            SearchTemplateResponse response = this.esClient.getClient().searchTemplate(request, RequestOptions.DEFAULT);
            this.responseHandler(response.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void responseHandler(SearchResponse response) {
        RestStatus status = response.status();  //响应状态
        TimeValue took = response.getTook();    //搜索一共花费了多长时间
        Boolean terminatedEarly = response.isTerminatedEarly();     //搜索操作是否由于到达终止条件而提前终止
        boolean timedOut = response.isTimedOut();   //是否超时

        //若请求成功则处理结果
        if (status == RestStatus.OK && !timedOut) {

            SearchHits hits = response.getHits();

            long totalHits = hits.getTotalHits();   //检索到的总条数
            float maxScore = hits.getMaxScore();    //最大相似度

            for (SearchHit hit : hits.getHits()) {

                //Document 的一些信息
                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();

                //Document 的 _source
                String sourceAsString = hit.getSourceAsString();

                //获取高亮
                Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
                HighlightField highlightField = highlightFieldMap.get("key");
                Text[] framgents = highlightField.getFragments();
                String fragmentString = framgents[0].string();
            }


        }
    }





}
