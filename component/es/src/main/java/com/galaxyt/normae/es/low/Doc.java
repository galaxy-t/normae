package com.galaxyt.normae.es.low;

import lombok.extern.slf4j.Slf4j;

/**
 * 文档
 * @author zhouqi
 * @date 2020/6/8 11:19
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/8 11:19     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
public class Doc {


    private ESClient esClient;

    public Doc(ESClient esClient) {
        this.esClient = esClient;
    }

    /**
     * 新增一个 doc
     * @param indexName     索引名称 , 若不存在则自动创建
     * @param type          文档类型
     * @param id            主键 ID , 必须指定 , 可使用 UUID 或者 任何其它的方式 , 需要自行转换成 String 类型
     * @param docBody       文档内容
     * @return
     * 新增成功 true
     * 新增失败 false
     */
    public boolean put(String indexName, String type, String id, String docBody) {
        String url = String.format("%s/%s/%s", indexName, type, id);
        return this.esClient.put(url, docBody, responseBody -> {
            if (responseBody != null) {
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * 创建一个文档 , 使用默认的类型[_doc]
     * @param indexName     索引名称 , 若不存在则自动创建
     * @param id            主键 ID , 必须指定 , 可使用 UUID 或者 任何其它的方式 , 需要自行转换成 String 类型
     * @param docBody       文档内容
     * @return
     * 新增成功 true
     * 新增失败 false
     */
    public boolean put(String indexName, String id, String docBody) {
        return this.put(indexName, "_doc", id, docBody);
    }


    public String get(String indexName, String type, String id) {
        String url = String.format("%s/%s/%s", indexName, type, id);
        return this.esClient.get(url, responseBody -> {
            if (responseBody == null) {
                return null;
            }

            return null;
        });
    }



}
