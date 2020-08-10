package com.galaxyt.normae.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.galaxyt"})
public class GatewayApplication {



    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("============================ Gateway * System startup completed ===================================================");
    }

}
