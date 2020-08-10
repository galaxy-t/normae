package com.galaxyt.normae.es.low;

import com.galaxyt.normae.core.util.json.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *
 * 索引的操作
 * @author zhouqi
 * @date 2020/6/5 16:30
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/5 16:30     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
public class Index {


    /**
     * 创建索引
     */
    public static final String ES_ENDPOINT_INDEX_CREATE = "/%s?pretty";


    private ESClient esClient;

    public Index(ESClient esClient) {
        this.esClient = esClient;
    }

    /**
     * 创建一个索引
     * 默认分片飞 5 个主分片 , 每个主分片有 1 个副分片
     *
     * @param indexName
     * @return 若返回 null 则代表创建失败 , 否则返回创建的索引的名称
     */
    public String create(final String indexName) {
        return this.esClient.put(String.format(ES_ENDPOINT_INDEX_CREATE, indexName), null, responseBody -> {

            log.info("Elasticsearch 创建索引[{}]结果为[{}]", indexName, responseBody);

            //若返回值为 null 则直接返回 null
            if (responseBody == null) {
                return null;
            }

            //以下是正常的返回值 但是此处只要返回值为 200 则直接返回
            /*Map<String, Object> map = (Map<String, Object>) GsonUtil.getObject(responseBody, Map.class);
            boolean acknowledged = (boolean) map.get("acknowledged");
            boolean shardsAcknowledged = (boolean) map.get("shards_acknowledged");
            String index = (String) map.get("index");*/

            return indexName;
        });
    }

    /**
     * 查询一个 Index 的详情
     * @param indexName 要查询的索引的名称
     * @return
     */
    public String detail(final String indexName) {
        return this.esClient.get(String.format("/%s",indexName),responseBody -> {
            return responseBody;
        });
    }


    public static void main(String[] args) {
        String s = "{\n" +
                "\"_index\": \"test_index3\",\n" +
                "\t\"_type\": \"_doc\",\n" +
                "\t\"_id\": \"222\",\n" +
                "\t\"_version\": 1,\n" +
                "\t\"_seq_no\": 0,\n" +
                "\t\"_primary_term\": 1,\n" +
                "\t\"found\": true,\n" +
                "\t\"_source\": {\n" +
                "\t\t\"name\": 222\n" +
                "\t}\n" +
                "}";

        Map<String, String> map = (Map<String, String>) GsonUtil.getObject(s, Map.class);

        System.out.println(map.get("_source"));

    }


}
