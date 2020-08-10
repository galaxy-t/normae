package com.galaxyt.normae.security.consumer;

import com.galaxyt.normae.core.thread.GlobalThreadFactory;
import com.galaxyt.normae.core.util.HttpUtil;
import com.galaxyt.normae.core.util.json.GsonUtil;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import com.galaxyt.normae.security.core.Constants;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 权限拉取
 * @author zhouqi
 * @date 2020/6/10 11:30
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/10 11:30     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Component
public class AuthorityPull  implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GatewayProperties gatewayProperties;

    /**
     * Eureka 客户端
     */
    private EurekaClient eurekaClient;

    /**
     * 自动执行
     * @param args
     */
    @Override
    public void run(String... args) {

        //获取 eureka 客户端
        this.eurekaClient = this.applicationContext.getBean(DiscoveryClient.class);

        this.initialize();
    }

    /**
     * 初始化方法
     */
    public void initialize() {

        /*
        客户端 ID 集合
        获取本网关所代理的全部客户端的 ID
         */
        Set<String> appIdList = this.gatewayProperties.getRoutes().stream().map(RouteDefinition::getId).collect(Collectors.toSet());

        /*
        单线程执行拉取
         */
        Executors.newSingleThreadExecutor(GlobalThreadFactory.create("AuthorityPull", true)).submit(() -> {

            //循环每个客户端 ID
            for (String appId : appIdList) {
                this.pull(appId);
            }

            log.info("全部节点权限已完成拉取并缓存[{}]",AuthorityRegistry.INSTANCE.toString());

        });

    }


    /**
     * 拉取方法
     * @param appId 要拉取的微服务标识
     * @return
     * 会将该微服务标识注册到 eureka 中的全部实例进行迭代拉取 , 若成功拉取到其中一个则将终止循环
     */
    private void pull(String appId) {

        //从 eureka 中得到该服务的全部实例 , 有可能该实例还未注册到 eureka 中 , 所以可能会返回 null
        Optional<Application> applicationO = Optional.ofNullable(this.eurekaClient.getApplication(appId));
        applicationO.ifPresent(application -> {

            //得到全部的实例列表
            List<InstanceInfo> clientInstanceList = application.getInstancesAsIsFromEureka();
            //循环拉取
            for (InstanceInfo instanceInfo : clientInstanceList) {
                //拼接请求 URL
                String authorityProviderEndpoint = String.format(Constants.AUTHORITY_PROVIDER_API, instanceInfo.getIPAddr(), instanceInfo.getPort());

                try {
                    //查询并添加缓存
                    AuthorityWrapper[] authoritys = HttpUtil.INSTANCE.get(authorityProviderEndpoint, response -> GsonUtil.gson.fromJson(response, AuthorityWrapper[].class));
                    AuthorityRegistry.INSTANCE.put(appId, authoritys);

                    log.info("拉取到服务[{}]的权限[{}]并已缓存", appId, GsonUtil.getJson(authoritys));

                } catch (Exception e) { //若拉取失败则进行下一次循环 , 直到全部节点全部拉取过
                    continue;
                }
            }

        });
    }


}
