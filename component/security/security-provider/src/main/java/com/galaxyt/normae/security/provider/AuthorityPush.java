package com.galaxyt.normae.security.provider;

import com.galaxyt.normae.core.util.HttpUtil;
import com.galaxyt.normae.core.util.json.GsonUtil;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import com.galaxyt.normae.security.core.Constants;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取全部需要通过网关向外暴露的接口 , 并推送到 gateway
 * 该类默认会尽可能晚的被执行
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/7/8 10:02
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/8 10:02     zhouqi          v1.0.0           Created
 */
@Slf4j
@Component
public class AuthorityPush implements CommandLineRunner {


    @Autowired
    private RequestMappingProcessor requestMappingProcessor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("eurekaRegistration")
    private Registration registration;

    /**
     * Eureka 客户端
     */
    private EurekaClient eurekaClient;

    @Override
    public void run(String... args) throws Exception {

        log.info("权限推送程序开始...");

        //获取 eureka 客户端
        this.eurekaClient = this.applicationContext.getBean(DiscoveryClient.class);

        //获取当前服务全部权限集合
        List<AuthorityWrapper> authoritys = this.requestMappingProcessor.findAll();

        log.info("获取到当前服务的权限集合[{}]", authoritys);

        this.push(authoritys);
    }

    /**
     * 推送方法
     * 会找到全部符合 gateway 命名规范的实例进行推送
     * @param authoritys
     */
    private void push(List<AuthorityWrapper> authoritys) {

        log.info("即将开始向网关推送权限集合");

        //获取当前服务注册的 eureka 上的全部实例
        Applications applications = this.eurekaClient.getApplications();
        if (applications == null || applications.size() <= 0) {
            log.info("未能从注册中心找到其它服务");
            return;
        }

        //若实例不为空则获取全部实例的集合
        List<Application> applicationList = applications.getRegisteredApplications();

        for (Application application : applicationList) {

            //若当前服务名称符合网关命名标准
            if (application.getName().startsWith(Constants.GATEWAY_IN_EUREKA_PREFIX)) {

                log.info("找到网关服务[{}]", application.getName());

                //得到全部的实例列表
                List<InstanceInfo> gatewayInstanceList = application.getInstancesAsIsFromEureka();
                //循环推送
                for (InstanceInfo instanceInfo : gatewayInstanceList) {

                    //拼接请求 URL
                    String authorityConsumerEndpoint = String.format(Constants.AUTHORITY_CONSUMER_API, instanceInfo.getIPAddr(), instanceInfo.getPort(), this.registration.getServiceId());

                    //推送到网关服务上
                    String authorityPushResponse = HttpUtil.INSTANCE.post(authorityConsumerEndpoint, GsonUtil.getJson(authoritys), response -> response);

                    log.info("权限推送[{}][{}]", authorityPushResponse, authorityConsumerEndpoint);

                }

            }
        }

        log.info("权限推送完成");
    }



}
