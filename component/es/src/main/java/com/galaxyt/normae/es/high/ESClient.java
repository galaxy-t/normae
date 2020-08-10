package com.galaxyt.normae.es.high;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * ES 操作客户端
 * 本客户端仅建议使用简单的增删改查 , 复杂的操作希望开发人员能够使用  getClient 方法获取到 ES Rest 进行操作
 * 并自行对结果进行处理
 * @author zhouqi
 * @date 2020/6/8 16:49
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/8 16:49     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Component
public class ESClient {

    @Value("${es.nodes}")
    private String esNodes;

    /**
     * Elasticsearch Rest 请求客户端
     */
    private RestHighLevelClient client;

    /**
     * 节点请求方案
     */
    private final String HTTP_HOST_SCHEME = "http";

    /**
     * 全局默认类型
     */
    public static final String DEFAULT_TYPE = "_doc";

    public ESClient() {

        //若节点不为空则进行拆分
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
        }

    }


    /**
     * 获取 ES 客户端实例 , 开发人员可以直接自行执行某些操作
     * @return
     */
    public RestHighLevelClient getClient() {
        return this.client;
    }



    /**
     * 新增文档
     * @param index         Index 名称
     * @param type          类型
     * @param id            主键 ID
     * @param content       内容
     * @return
     * true  新增成功
     * false 新增失败
     */
    public boolean add(String index, String type, String id, String content) {
        log.debug("Document add : index=[{}] type=[{}] id=[{}] content=[{}]", index, type, id, content);
        IndexRequest request = new IndexRequest(index, type, id);
        request.source(content, XContentType.JSON);
        try {
            IndexResponse response = this.client.index(request, RequestOptions.DEFAULT);
            log.debug("Document success[{}] : index=[{}] type=[{}] id=[{}] content=[{}]", response, index, type, id, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Document add failure[{}] : index=[{}] type=[{}] id=[{}] content=[{}]", e.getMessage(), index, type, id, content);
            return false;
        }
    }

    /**
     * 新增文档 , 类型默认为 "_doc"
     * @param index         Index 名称
     * @param id            主键 ID
     * @param content       内容
     * @return
     * true  新增成功
     * false 新增失败
     */
    public boolean add(String index, String id, String content) {
        return this.add(index, DEFAULT_TYPE, id, content);
    }

    /**
     * 查询操作
     * 根据 ID 查询一个 Document
     * @param index     Index 名称
     * @param type      类型
     * @param id        主键
     * @return
     */
    public String get(String index, String type, String id) {
        log.debug("Document get : index=[{}] type=[{}] id=[{}]", index, type, id);

        GetRequest request = new GetRequest(index, type, id);
        try {
            GetResponse response = this.client.get(request, RequestOptions.DEFAULT);
            log.debug("Document get success[{}] : index=[{}] type=[{}] id=[{}]", response, index, type, id);
            return response.getSourceAsString();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Document get failure[{}] : index=[{}] type=[{}] id=[{}]", e.getMessage(), index, type, id);
            return null;
        }
    }

    /**
     * 查询操作  , 类型默认为 "_doc"
     * 根据 ID 查询一个 Document
     * @param index     Index 名称
     * @param id        主键
     * @return
     */
    public String get(String index, String id) {
        return this.get(index, DEFAULT_TYPE, id);
    }

    /**
     * 判断一个 Document 是否存在
     * @param index     Index 名称
     * @param type      类型
     * @param id        主键
     * @return
     * 存在 true
     * 不存在 false
     */
    public boolean exists(String index, String type, String id) {
        log.debug("Document exists : index=[{}] type=[{}] id=[{}]", index, type, id);

        GetRequest request = new GetRequest(index, type, id);
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        try {
            boolean bool = this.client.exists(request, RequestOptions.DEFAULT);
            log.debug("Document exists[{}] : index=[{}] type=[{}] id=[{}]", bool, index, type, id);
            return bool;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Document exists failure[{}] : index=[{}] type=[{}] id=[{}]", e.getMessage(), index, type, id);
            return false;
        }
    }

    /**
     * 判断一个 Document 是否存在  , 类型默认为 "_doc"
     * @param index     Index 名称
     * @param id        主键
     * @return
     * 存在 true
     * 不存在 false
     */
    public boolean exists(String index, String id) {
        return this.exists(index, DEFAULT_TYPE, id);
    }

    /**
     * 删除一个文档
     * @param index     Index 名称
     * @param type      类型
     * @param id        主键
     * @return
     */
    public boolean delete(String index, String type, String id) {
        log.debug("Document delete : index=[{}] type=[{}] id=[{}]", index, type, id);
        DeleteRequest request = new DeleteRequest(index, type, id);
        try {
            DeleteResponse response = this.client.delete(request, RequestOptions.DEFAULT);
            log.debug("Document delete success[{}]: index=[{}] type=[{}] id=[{}]", response, index, type, id);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Document delete failure[{}]: index=[{}] type=[{}] id=[{}]", e.getMessage(), index, type, id);
            return false;
        }
    }

    /**
     * 删除一个文档  , 类型默认为 "_doc"
     * @param index     Index 名称
     * @param id        主键
     * @return
     */
    public boolean delete(String index, String id) {
        return this.delete(index, DEFAULT_TYPE, id);
    }


    /**
     *  修改一个 Document
     *  注 : 不支持脚本
     * @param index     Index 名称
     * @param type      类型
     * @param id        id
     * @param content   修改的主体内容 JSON 格式
     * @return
     * 修改失败或文档不存在返回 false , 如果是 saveOrUpdate 操作 , 建议使用 add 操作
     * 成功返回 true
     */
    public boolean update(String index, String type, String id, String content) {
        log.debug("Document update : index=[{}] type=[{}] id=[{}] content=[{}]", index, type, id, content);
        UpdateRequest request = new UpdateRequest(index, type, id);
        request.doc(content, XContentType.JSON);

        try {
            UpdateResponse response = this.client.update(request, RequestOptions.DEFAULT);
            log.debug("Document update success[{}] : index=[{}] type=[{}] id=[{}] content=[{}]", response, index, type, id, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("Document update failure[{}]: index=[{}] type=[{}] id=[{}] content=[{}]", e.getMessage(), index, type, id, content);
            return false;
        }
    }

    /**
     *  修改一个 Document , 类型默认为 "_doc"
     *  注 : 不支持脚本
     * @param index     Index 名称
     * @param id        id
     * @param content   修改的主体内容 JSON 格式
     * @return
     * 修改失败或文档不存在返回 false , 如果是 saveOrUpdate 操作 , 建议使用 add 操作
     * 成功返回 true
     */
    public boolean update(String index, String id, String content) {
        return this.update(index, DEFAULT_TYPE, id, content);
    }

}
