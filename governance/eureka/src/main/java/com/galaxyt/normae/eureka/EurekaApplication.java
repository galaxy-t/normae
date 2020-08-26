package com.galaxyt.normae.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 启动类
 * @author zhouqi
 * @date 2020/8/25 9:45
 * @version v1.0.0
 * @Description
 *
 */
@Slf4j
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
        log.info("============================ Eureka * System startup completed ===================================================");
    }
}
