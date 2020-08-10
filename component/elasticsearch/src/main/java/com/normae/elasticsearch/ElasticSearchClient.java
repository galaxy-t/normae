package com.normae.elasticsearch;

import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.normae.elasticsearch.bo.DateQueryBo;
import com.normae.elasticsearch.bo.IndexBo;
import com.normae.elasticsearch.bo.QueryBo;
import com.normae.elasticsearch.bo.SortDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesRequest;
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lishanglei
 * @version v1.0.0
 * @date 2020/7/13
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/13              lishanglei      v1.0.0           Created
 */
@Slf4j
@Component
public class ElasticSearchClient {

    @Value("${es.nodes}")
    private String esNodes="192.168.8.128:9700";

    /**
     * Elasticsearch Rest 请求客户端
     */
    private RestHighLevelClient client;

    /**
     * 节点请求方案
     */
    private final String HTTP_HOST_SCHEME = "http";


    public ElasticSearchClient() {

        if (this.esNodes != null) {
            //若拆分结果不为空则开始构造 RestClient
            Optional<String[]> nodesHostArrO = Optional.ofNullable(this.esNodes.split(","));
            nodesHostArrO.ifPresent((nodesHostArr) -> {
                //将全部的节点的地址实例化
                HttpHost[] httpHostArr = new HttpHost[nodesHostArr.length];
                for (int i = 0; i < nodesHostArr.length; i++) {
                    String[] httpHost = nodesHostArr[i].split(":");
                    httpHostArr[i] = new HttpHost(httpHost[0], Integer.parseInt(httpHost[1]), HTTP_HOST_SCHEME);
                }

                //实例化客户端
                this.client = new RestHighLevelClient(RestClient.builder(httpHostArr));
            });
        }else{
            throw new GlobalException(GlobalExceptionCode.ERROR,"未读取到ES节点配置");
        }
    }

    /**
     * 获取 ES 客户端实例 , 开发人员可以直接自行执行某些操作
     *
     * @return
     */
    public RestHighLevelClient getClient() {
        return this.client;
    }

    /**
     * 创建索引库
     *
     * @param indexName 索引库名称
     * @param json      创建索引库的字段
     *
     * {
     * 	"properties": {
     * 		"acadId": {
     * 			"type": "keyword"	//字符串类型设置为 keyword 则不会被分词
     *                },
     * 		"baseInfo": {
     * 			"properties": {
     * 				"id": {
     * 					"type": "keyword"
     *                },
     * 				"realName": {
     * 					"type": "text",		//字符串设置为 text 则会被分词
     * 					"analyzer": "ik_smart",		//分词引擎
     * 					"fielddata": true,			//设置该字段可以被排序
     * 					"fields": {					//解决中文排序问题
     * 						"raw": {				//使用中文排序插件
     * 							"type": "icu_collation_keyword",
     * 							"language": "zh",
     * 							"country": "CN"
     *                        }
     *                    }
     *                },
     *            }
     *        }
     *    }
     * }
     *
     *
     * @param shards    分片数
     * @param replicas  备份数
     */
    public CreateIndexResponse createIndex(String indexName, String json, int shards, int replicas) {

        CreateIndexResponse response = null;
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(
                    Settings.builder()
                            //设置分片数
                            .put("index.number_of_shards", shards)
                            //设置备份数
                            .put("index.number_of_replicas", replicas)
            );
            request.mapping(json, XContentType.JSON);
            response = this.client.indices().create(request, RequestOptions.DEFAULT);
            log.info("创建索引库响应结果[{}]", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return response;
    }

    /**
     * 创建索引库
     *
     * @param indexName 索引库名称
     * @param json      创建索引库的字段
     */
    public CreateIndexResponse createIndex(String indexName, String json) {

        int shards = 1;
        //若节点不为空则进行拆分
        if (this.esNodes != null) {
            //若拆分结果不为空则开始构造 RestClient
            String[] nodes = this.esNodes.split(",");
            shards = nodes.length;
        } else {
            log.error("es节点配置错误");
        }
        return this.createIndex(indexName, json, shards, 2);
    }

    /**
     * 删除索引库
     *
     * @param indexName
     */
    public AcknowledgedResponse deleteIndex(String indexName) throws IOException {

        AcknowledgedResponse deleteIndexResponse = null;
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
            log.info("删除索引响应结果[{}]", deleteIndexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return deleteIndexResponse;
    }


    /**
     * 查看索引库是否存在
     *
     * @param indexName
     */
    public boolean indexExists(String indexName) throws IOException {

        boolean exists = false;
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
            log.info("查看索引库是否存在响应结果[{}]", exists);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return exists;
    }

    /**
     * 打开索引库,可以查看索引库已经执行的操作
     *
     * @param indexName
     */
    public OpenIndexResponse openIndex(String indexName) throws IOException {

        OpenIndexResponse openIndexResponse = null;
        try {
            OpenIndexRequest request = new OpenIndexRequest(indexName);
            openIndexResponse = client.indices().open(request, RequestOptions.DEFAULT);

            log.info("查看索引库响应结果[{}]", openIndexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return openIndexResponse;
    }

    /**
     * 打开索引库,可以查看索引库已经执行的操作
     *
     * @param indexName
     */
    public AcknowledgedResponse closeIndex(String indexName) throws IOException {

        AcknowledgedResponse closeIndexResponse = null;
        try {
            CloseIndexRequest request = new CloseIndexRequest("indexName");
            closeIndexResponse = client.indices().close(request, RequestOptions.DEFAULT);

            log.info("查看索引库响应结果[{}]", closeIndexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return closeIndexResponse;
    }

    /**
     * 创建索引库并插入一条文档
     *
     * @param indexName 索引库名称
     * @param json      创建索引库的字段
     */
    public IndexResponse createIndexAndAddDoc(String indexName, String json, String id) throws IOException {

        IndexResponse response = null;
        try {
            IndexRequest request = new IndexRequest(indexName);
            request.id(id);
            request.source(json, XContentType.JSON);
            request.timeout(TimeValue.timeValueSeconds(1));
            request.timeout("1s");
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            request.version(2);
            request.versionType(VersionType.EXTERNAL);
            request.opType(DocWriteRequest.OpType.CREATE);
            request.setPipeline("pipeline");
            response = client.index(request, RequestOptions.DEFAULT);
            log.info("创建索引库并插入一条文档响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 查询索引库中的文档
     *
     * @param indexName 索引库名称
     * @param id        文档id
     * @return
     * @throws IOException
     */
    public GetResponse getIndexDoc(String indexName, String id) {

        GetResponse response = null;

        try {
            GetRequest request = new GetRequest(indexName, id);
            response = client.get(request, RequestOptions.DEFAULT);
            log.info("查询索引库中的文档响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return response;
    }

    public void addDoc(String indexName, String json, String id) {

        // 准备请求对象
        IndexRequest indexRequest = new IndexRequest(indexName);
        // 将请求体封装到请求对象
        indexRequest.source(json, XContentType.JSON);
        indexRequest.id(id);
        // 发送请求
        try {
            IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("响应结果: " + index);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void addDoc(String indexName, String json) {

        // 准备请求对象
        IndexRequest indexRequest = new IndexRequest(indexName);
        // 将请求体封装到请求对象
        indexRequest.source(json, XContentType.JSON);
        // 发送请求
        try {
            IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("响应结果: " + index);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 获取source
     *
     * @param indexName
     * @param id
     * @param includes
     * @param excludes
     * @return
     * @throws IOException
     */
    public GetSourceResponse getSource(String indexName, String id, String[] includes, String[] excludes) throws IOException {

        GetSourceResponse response = null;
        try {
            GetSourceRequest request = new GetSourceRequest(indexName, id);
            request.fetchSourceContext(
                    new FetchSourceContext(true, includes, excludes)
            );

            response = client.getSource(request, RequestOptions.DEFAULT);
            log.info("全文检索响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 查询某条文档是否存在
     *
     * @return
     */
    public Boolean documentExits(String indexName, String id) throws IOException {

        boolean exists = false;
        try {
            GetRequest request = new GetRequest(indexName, id);
            request.fetchSourceContext(
                    new FetchSourceContext(false)
            );
            request.storedFields("_none_");
            exists = client.exists(request, RequestOptions.DEFAULT);
            log.info("查询某条文档是否存在响应结果");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return exists;
    }

    /**
     * 删除一条文档
     *
     * @param indexName
     * @param id
     * @return
     * @throws IOException
     */
    public DeleteResponse deleteDoc(String indexName, String id) throws IOException {

        DeleteResponse response = null;
        try {
            DeleteRequest request = new DeleteRequest(
                    indexName,
                    id);

            response = client.delete(
                    request, RequestOptions.DEFAULT);
            log.info("删除一条文档响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 更新文档
     *
     * @param indexName
     * @param id
     * @return
     * @throws IOException
     */
    public UpdateResponse updateDoc(String indexName, String id, String json) throws IOException {
        UpdateResponse response = null;
        try {
            UpdateRequest request = new UpdateRequest(
                    indexName,
                    id);
            request.doc(json, XContentType.JSON);
            log.info("更新文档数据[{}]",request);
            response = client.update(
                    request, RequestOptions.DEFAULT);

            log.info("更新文档响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 批量操作文档
     *
     * @param request
     * @return
     * @throws IOException
     */
    public BulkResponse bulkDoc(BulkRequest request) throws IOException {

        BulkResponse responses = null;
        try {
            responses = client.bulk(request, RequestOptions.DEFAULT);
            log.info("批量操作文档,响应结果[{}]", responses);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return responses;
    }


    /**
     * 批量获取文档
     *
     * @param indexBoList
     * @return
     */
    public MultiGetResponse multiGetDoc(List<IndexBo> indexBoList) throws IOException {

        MultiGetResponse response = null;
        try {
            MultiGetRequest request = new MultiGetRequest();
            if (indexBoList != null && indexBoList.size() > 0) {
                for (int i = 0; i < indexBoList.size(); i++) {
                    IndexBo indexBo = indexBoList.get(1);
                    request.add(
                            new MultiGetRequest.Item(
                                    indexBo.getIndexName(), indexBo.getId()
                            )
                    );
                }
            }

            response = client.mget(request, RequestOptions.DEFAULT);
            log.info("批量获取文档响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 单条件查询
     *
     * @param indexName 索引库名称,不指定索引库则会在所有索引库中查询
     * @return
     */
    public SearchResponse matchQuery(String indexName, String key, String value) {

        SearchResponse response = null;
        try {
            // 1.构建SearchRequest请求对象,指定索引库
            SearchRequest searchRequest = new SearchRequest(indexName);
            // 2.构建SearchSourceBuilder查询对象
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 3.构建QueryBuilder对象指定查询方式和查询条件
            QueryBuilder queryBuilder = QueryBuilders.matchQuery(key, value);
            // 4.将queryBuilder对象设置到SearchSourceBuilder中
            searchSourceBuilder.query(queryBuilder);
            // 5.将SearchSourceBuilder查询对象封装到请求对象SearchRequest中
            searchRequest.source(searchSourceBuilder);
            // 6.调用方法进行数据通信
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("单条件查询索引库结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 查询多个索引库中所有数据
     *
     * @return
     * @throws IOException
     */
    public SearchResponse searchIndex(String[] indexs,Integer pageIndex,Integer pageSize) {

        SearchResponse response = null;
        try {
            SearchRequest searchRequest = new SearchRequest(indexs);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            if (pageSize != 0 && pageSize != 0) {
                if (pageIndex > 1) {
                    searchSourceBuilder.from(pageIndex * (pageSize - 1));
                } else {
                    searchSourceBuilder.from(0);
                }
            }
            searchRequest.source(searchSourceBuilder);
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("查询索引响应结果[{}]", response);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }


    /**
     * 全文查询
     *
     * @param indexs  索引库
     * @param content 要搜索的内容
     * @return
     * @throws IOException
     */
    public SearchResponse fullSearch(String[] indexs, String content, int from, int size, List<SortDto> sortList, String aosId) throws IOException {

        SearchResponse response = null;
        try {

            SearchRequest searchRequest = new SearchRequest(indexs);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            if(!StringUtils.isEmpty(content)){
                searchSourceBuilder.query(new QueryStringQueryBuilder(content));
            }
            if (!StringUtils.isEmpty(aosId)) {
                QueryBuilder queryBuilder = QueryBuilders.matchQuery("baseInfo.aosId", aosId);
                searchSourceBuilder.query(queryBuilder);
            }
            if (sortList != null && sortList.size() > 0) {
                for (SortDto sortDto : sortList) {
                    searchSourceBuilder.sort(sortDto.getField(), SortOrder.fromString(sortDto.getSort()));
                }
            }
            if (size != 0 && size != 0) {
                if (from > 1) {
                    searchSourceBuilder.from(from * (size - 1));
                } else {
                    searchSourceBuilder.from(0);
                }
            }
            searchRequest.source(searchSourceBuilder);
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("查询索引响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 排序查询
     *
     * @param indexName
     * @param queryBos
     * @return
     */
    public SearchResponse orderSearch(String indexName, List<QueryBo> queryBos) {

        SearchResponse response = null;
        try {
            // 1.构建SearchRequest请求对象,指定索引库
            SearchRequest searchRequest = new SearchRequest(indexName);
            // 2.构建SearchSourceBuilder查询对象
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 3.构建QueryBuilder对象指定查询方式和查询条件
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            // 4.将queryBuilder对象设置到SearchSourceBuilder中
            sourceBuilder.query(queryBuilder);

            /**
             * 通过sort方法指定排序规则 args1: 排序字段 args2: 升序还是降序,默认升序
             *
             * <p>默认不能使用text字段排序( 设置"fielddata":true)
             */
            if (queryBos != null && queryBos.size() > 0) {
                for (QueryBo queryBo : queryBos) {
                    sourceBuilder.sort(queryBo.getKey(), SortOrder.fromString(queryBo.getValue()+""));
                }
            }

            // 5.将SearchSourceBuilder查询对象封装到请求对象SearchRequest中
            searchRequest.source(sourceBuilder);
            // 6.调用方法进行数据通信
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("排序查询响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 分页查询
     *
     * @param indexName
     * @param start     从第几页开始
     * @param size      每页几条
     * @return
     */
    public SearchResponse limitSearch(String indexName, int start, int size) {
        SearchResponse response = null;
        try {
            // 1.构建SearchRequest请求对象,指定索引库
            SearchRequest searchRequest = new SearchRequest(indexName);
            // 2.构建SearchSourceBuilder查询对象
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 3.构建QueryBuilder对象指定查询方式和查询条件
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            // 4.将queryBuilder对象设置到SearchSourceBuilder中
            sourceBuilder.query(queryBuilder);

            /**
             * 通过sort方法指定排序规则 args1: 排序字段 args2: 升序还是降序,默认升序
             *
             *
             * <p>默认不能使用text字段排序( 设置"fielddata":true)
             */
            // sourceBuilder.sort("id", SortOrder.DESC);
            sourceBuilder.sort("acadId");
            /** 分页 :查询第一页,每页两条 int start =(pageNum-1)*pageSize */
            //sourceBuilder.from(1);
            //sourceBuilder.size(2); // 分两页
            // 5.将SearchSourceBuilder查询对象封装到请求对象SearchRequest中
            searchRequest.source(sourceBuilder);
            // 6.调用方法进行数据通信
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("分页查询响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }


    /**
     * 高亮查询
     *
     * @param indexName
     * @param field
     * @param preTags
     * @param postTags
     * @return
     */
    public SearchResponse highSearch(String indexName, String field, String preTags, String postTags) {
        SearchResponse response = null;

        try {
            // 1.构建SearchRequest请求对象,指定索引库
            SearchRequest searchRequest = new SearchRequest(indexName);
            // 2.构建SearchSourceBuilder查询对象
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 3.构建QueryBuilder对象指定查询方式和查询条件  高亮使用matchquery,不支持matchquery
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "小米");
            // 4.将queryBuilder对象设置到SearchSourceBuilder中
            sourceBuilder.query(queryBuilder);
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags(preTags);
            highlightBuilder.postTags(postTags);
            highlightBuilder.field(field);

            sourceBuilder.highlighter(highlightBuilder);
            /**
             * 通过sort方法指定排序规则 args1: 排序字段 args2: 升序还是降序,默认升序
             *
             * <p>默认不能使用text字段排序( 设置"fielddata":true)
             */
            // sourceBuilder.sort("id", SortOrder.DESC);
            sourceBuilder.sort("price");
            /** 分页 :查询第一页,每页两条 int start =(pageNum-1)*pageSize */

            // sourceBuilder.from(1);
            // sourceBuilder.size(2);  //分两页
            // 5.将SearchSourceBuilder查询对象封装到请求对象SearchRequest中
            searchRequest.source(sourceBuilder);
            // 6.调用方法进行数据通信
            response = client.search(searchRequest, RequestOptions.DEFAULT);

            // 7.输出结果
            // System.out.println("响应结果: " + response);
            SearchHit[] hits = response.getHits().getHits();
            for (int i = 0; i < hits.length; i++) {
                String sourceAsString = hits[i].getSourceAsString();
                // System.out.println("查询结果:" + sourceAsString);

                Map<String, HighlightField> highlightFields = hits[i].getHighlightFields();
                HighlightField title = highlightFields.get("title");
                Text[] texts = title.getFragments();
                for (Text fragment : texts) {
                    System.out.println("field:" + fragment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 滚动查询
     *
     * @param indexName 索引库名称
     * @param key       条件查询字段
     * @param value     条件查询字段的值
     * @param size      查询的条数
     * @return
     * @throws IOException
     */
    public SearchResponse searchConditionDoc(String indexName, String key, String value, int size) throws IOException {

        SearchResponse response = null;
        try {
            SearchRequest request = new SearchRequest(indexName);
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchQuery(key, value));
            builder.size(size);
            request.source(builder);
            //查询间隔
            request.scroll(TimeValue.timeValueMinutes(1));
            response = client.search(request, RequestOptions.DEFAULT);
            //滚动id,后续的查询会用到
            String scrollId = response.getScrollId();
            //第一次查询的结果
            SearchHits hits = response.getHits();
            log.info("滚动搜索响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 单条件查询
     *
     * @param indexName
     * @param key
     * @param value
     * @return
     */
    public SearchResponse matchDoc(String indexName, String key, String value) {

        SearchResponse response = null;

        try {
            // 1.构建SearchRequest请求对象,指定索引库
            SearchRequest searchRequest = new SearchRequest(indexName);
            // 2.构建SearchSourceBuilder查询对象
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 3.构建QueryBuilder对象指定查询方式和查询条件
            QueryBuilder queryBuilder = QueryBuilders.matchQuery(key, value);
            // 4.将queryBuilder对象设置到SearchSourceBuilder中
            searchSourceBuilder.query(queryBuilder);
            // 5.将SearchSourceBuilder查询对象封装到请求对象SearchRequest中
            searchRequest.source(searchSourceBuilder);
            // 6.调用方法进行数据通信
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("单条件查询索引库结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 同时执行多次查询
     *
     * @param indexName
     * @param list
     * @return
     * @throws IOException
     */
    public MultiSearchResponse multiSearchDoc(String indexName, List<QueryBo> list) throws IOException {

        MultiSearchResponse response = null;
        try {
            MultiSearchRequest request = new MultiSearchRequest();
            if (list != null && list.size() > 0) {
                for (QueryBo queryBo : list) {
                    SearchRequest searchRequest;
                    if (!StringUtils.isEmpty(indexName)) {
                        searchRequest = new SearchRequest(indexName).source(new SearchSourceBuilder().query(QueryBuilders.matchQuery(queryBo.getKey(), queryBo.getValue())));
                    } else {
                        searchRequest = new SearchRequest().source(new SearchSourceBuilder().query(QueryBuilders.matchQuery(queryBo.getKey(), queryBo.getValue())));
                    }
                    request.add(searchRequest);
                }
            }
            response = client.msearch(request, RequestOptions.DEFAULT);
            log.info("多条件查询响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 在多个索引库中查询某字段的属性,如果索引库名为空,则在所有索引库中查询
     *
     * @param field   字段名
     * @param indices 索引库名
     * @return
     */
    public FieldCapabilitiesResponse fieldSearch(String field, String[] indices) throws IOException {

        FieldCapabilitiesResponse response = null;
        try {
            FieldCapabilitiesRequest request = new FieldCapabilitiesRequest()
                    .fields(field).indices(indices);
            response = client.fieldCaps(request, RequestOptions.DEFAULT);
            log.info(" 在多个索引库中查询某字段响应结果[{}]", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return response;
    }

    /**
     * 统计索引库中文档的数量
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public CountResponse countSearch(String indexName) throws IOException {

        CountResponse countResponse = null;
        try {
            CountRequest countRequest = new CountRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            countRequest.source(searchSourceBuilder);
            countResponse = client
                    .count(countRequest, RequestOptions.DEFAULT);
            log.info("统计查询响应结果[{}]", countResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return countResponse;
    }

    /**
     * 多条件查询
     *
     * @param indexName
     * @param queryBoList     非日期字段,可为null
     * @param dateQueryBoList 日期字段,可为null
     * @return
     * @throws IOException
     */
    public SearchResponse boolSearch(String indexName, List<QueryBo> queryBoList, List<DateQueryBo> dateQueryBoList) throws IOException {

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (queryBoList != null && queryBoList.size() > 0) {
            for (QueryBo queryBo : queryBoList) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(queryBo.getKey(), queryBo.getValue()));
            }
        }
        if (dateQueryBoList != null && dateQueryBoList.size() > 0) {
            for (DateQueryBo dateQueryBo : dateQueryBoList) {
                boolQueryBuilder.must(QueryBuilders
                        .rangeQuery(dateQueryBo.getField())
                        .format("epoch_second")
                        .gte(dateQueryBo.getStartTime())
                        .lte(dateQueryBo.getEndTime()));
            }
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = this.client.search(searchRequest, RequestOptions.DEFAULT);
        log.info("多条件[{}]查询结果[{}]", queryBoList, response);
        return response;
    }

}
