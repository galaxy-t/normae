package com.galaxyt.normae.es.low;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

/**
 * ES 操作客户端
 */
@Slf4j
/*@Component*/
public class ESClient {

    /*@Value("${es.nodes}")*/
    private String esNodes;


    /**
     * Elasticsearch Rest 请求客户端
     */
    private RestClient client;

    /**
     * 节点请求方案
     */
    private final String HTTP_HOST_SCHEME = "http";


    public static final String HTTP_REQUEST_METHOD_GET = "GET";
    public static final String HTTP_REQUEST_METHOD_POST = "POST";
    public static final String HTTP_REQUEST_METHOD_PUT = "PUT";


    /**
     * 监控
     */
    private Monitor monitor = new Monitor(this);

    /**
     * 索引操作
     */
    private Index index = new Index(this);


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
                this.client = RestClient.builder(httpHostArr).build();
            });
        }

    }

    /**
     * 监控
     * @return
     */
    public Monitor monitor() {
        return this.monitor;
    }

    /**
     * 索引
     * @return
     */
    public Index index() {
        return this.index;
    }



    /**
     * 发起一个 get 请求
     * 若返回值为 null 则代表其请求失败 , 否则请求必定会有返回结果并进入 function 中
     * @param url           要请求的地址
     * @param function      结果返回处理函数
     * @param <R>
     * @return
     */
    public <R> R get(String url, Function<String, R> function) {
        return this.request(url, HTTP_REQUEST_METHOD_GET, null, function);
    }

    /**
     * 发起一个 post 请求
     * 若返回值为 null 则代表其请求失败 , 否则请求必定会有返回结果并进入 function 中
     * @param url           要请求的地址
     * @param jsonBody      请求体     若不需要请求体则设置为 null , 仅接受 JSON 格式的请求体
     * @param function      结果返回处理函数
     * @param <R>
     * @return
     */
    public <R> R post(String url, String jsonBody, Function<String, R> function) {
        return this.request(url, HTTP_REQUEST_METHOD_POST, jsonBody, function);
    }

    /**
     * 发起一个 put 请求
     * 若返回值为 null 则代表其请求失败 , 否则请求必定会有返回结果并进入 function 中
     * @param url           要请求的地址
     * @param jsonBody      请求体     若不需要请求体则设置为 null , 仅接受 JSON 格式的请求体
     * @param function      结果返回处理函数
     * @param <R>
     * @return
     */
    public <R> R put(String url, String jsonBody, Function<String, R> function) {
        return this.request(url, HTTP_REQUEST_METHOD_PUT, jsonBody, function);
    }


    /**
     * 发起一个请求
     * 若返回值为 null 则代表其请求失败 , 否则请求必定会有返回结果并进入 function 中
     * @param url           要请求的地址
     * @param method        请求方法    HTTP_REQUEST_METHOD_GET,HTTP_REQUEST_METHOD_POST,HTTP_REQUEST_METHOD_PUT
     * @param jsonBody      请求体     若不需要请求体则设置为 null , 仅接受 JSON 格式的请求体
     * @param function      结果返回处理函数
     * @param <R>
     * @return
     */
    public <R> R request(String url, String method, String jsonBody, Function<String, R> function) {

        log.info("Elasticsearch client request: [{}] [{}] [{}]", method, url, jsonBody);

        //设置请求参数
        //若部位 GET 请求则设置请求体 , 仅支持 JSON 格式字符串
        Request request = new Request(method, url);
        request.setJsonEntity(jsonBody);

        try {
            Response response = this.client.performRequest(request);

            //若状态码是 200 或 201
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                String responseBody = EntityUtils.toString(response.getEntity());
                return function.apply(responseBody);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Elasticsearch client request failure: [{}]", e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {
        //实例化客户端
        RestClient client = RestClient.builder(new HttpHost("192.168.8.177", 9200, "http")).build();

        Request request = new Request("GET", "/test_index3/_doc/444");

        String body = "{\"name\":111}";

        request.setJsonEntity(null);
        try {
            Response response = client.performRequest(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("===========================");
                System.out.println(responseBody);
            } else {
                System.out.println(response.getStatusLine().getStatusCode());
                System.out.println(response.getStatusLine().getReasonPhrase());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
