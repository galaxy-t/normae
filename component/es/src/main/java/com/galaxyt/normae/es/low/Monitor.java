package com.galaxyt.normae.es.low;

/**
 * 对集群或节点的监控
 * @author zhouqi
 * @date 2020/6/5 16:32
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/5 16:32     zhouqi          v1.0.0           Created
 *
 */
public class Monitor {

    /**
     * 健康检查
     */
    public static final String ES_ENDPOINT_HEALTH = "_cat/health?v&pretty";

    /**
     * 查看全部节点
     */
    public static final String ES_ENDPOINT_NODES = "_cat/nodes?v&pretty";

    /**
     * 查看全部 Index
     */
    public static final String ES_ENDPOINT_INDICES = "_cat/indices?v&pretty";


    private ESClient esClient;

    public Monitor(ESClient esClient) {
        this.esClient = esClient;
    }

    /**
     * 集群健康检查
     * @return
     * epoch      timestamp cluster status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
     * 1591582344 02:12:24  my-es   yellow          1         1     10  10    0    0       10             0                  -                 50.0%
     */
    public String health() {
        return this.esClient.get(ES_ENDPOINT_HEALTH, responseBody -> responseBody);
    }

    /**
     * 查看全部节点
     * @return
     * ip            heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
     * 192.168.8.177           17          33   0    0.00    0.01     0.05 mdi       *      node-1
     */
    public String nodes() {
        return this.esClient.get(ES_ENDPOINT_NODES, responseBody -> responseBody);
    }

    /**
     * 查看全部 Index
     * @return
     * health status index         uuid                   pri rep docs.count docs.deleted store.size pri.store.size
     * yellow open   test_index    wImqsC8QQdCdKywp8CCY0A   5   1          5            0     18.6kb         18.6kb
     * yellow open   article_index HeHNyIsmRaik08HzMLfwgw   5   1          2            0     10.3kb         10.3kb
     */
    public String indices() {
        return this.esClient.get(ES_ENDPOINT_INDICES, responseBody -> responseBody);
    }

}
