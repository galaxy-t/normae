package com.galaxyt.normae.security.core;

/**
 * 常量
 * @author zhouqi
 * @date 2020/7/8 10:42
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/7/8 10:42     zhouqi          v1.0.0           Created
 *
 */
public class Constants {

    /**
     * gateway 注册到当前 eureka 上的前缀
     */
    public static final String GATEWAY_IN_EUREKA_PREFIX = "GATEWAY";


    /**
     * 微服务用于获取全部权限的接口路径
     */
    public static final String AUTHORITY_PROVIDER_API = "http://%s:%s/endpoint/authority";

    /**
     * 网关用于接收权限的接口路径
     */
    public static final String AUTHORITY_CONSUMER_API = "http://%s:%s/endpoint/authority/registry?appId=%s";


}
