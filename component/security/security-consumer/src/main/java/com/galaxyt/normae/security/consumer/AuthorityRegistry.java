package com.galaxyt.normae.security.consumer;

import com.galaxyt.normae.core.util.json.GsonUtil;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 微服务权限注册中心
 * 用于管理当前 gateway 所能够代理的全部微服务中的权限
 * 单例
 * @author zhouqi
 * @date 2020/7/8 10:18
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/7/8 10:18     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@ToString
public enum AuthorityRegistry {

    INSTANCE;

    /**
     * 一个线程安全的注册器
     * 结构
     * app  object
     *      method:url     authority
     *
     */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, AuthorityWrapper>> registry = new ConcurrentHashMap<>(16, 0.75f, 4);

    /**
     * 无论何时均会替换到旧的将新的放入注册器中
     * TODO 总感觉怪怪的
     * @param appId
     * @param authoritys
     */
    private void put(String appId, ConcurrentHashMap<String, AuthorityWrapper> authoritys) {
        registry.put(appId, authoritys);
        log.info("服务[{}]的权限[{}]缓存已更新", appId, GsonUtil.getJson(authoritys));
    }

    /**
     *
     * @param appId
     * @param authoritys
     */
    public void put(String appId, AuthorityWrapper[] authoritys) {
        ConcurrentHashMap<String, AuthorityWrapper> authorityWrapperMap = new ConcurrentHashMap<>(16, 0.75f, 4);
        for (AuthorityWrapper wrapper : authoritys) {
            authorityWrapperMap.put(String.format("%s:%s", wrapper.getMethod(), wrapper.getUrl()), wrapper);
        }

        this.put(appId, authorityWrapperMap);
    }


    /**
     * 根据客户端
     * @param appId         微服务标识
     * @param methodType    请求类型 , 如 : GET , POST
     * @param path          请求路径 , 注意该路径应为不带微服务标识的路径 , 如在浏览器中呈现的为 http://***:80/user/login , 那么此处参数应为 /login
     * @return  若存在则返回当前请求所对应的权限对象 , 否则返回 null
     */
    public AuthorityWrapper get(String appId, String methodType, String path) {
        if (registry.containsKey(appId)) {
            return registry.get(appId).get(String.format("%s:%s", methodType, path));
        }
        return null;
    }


}
