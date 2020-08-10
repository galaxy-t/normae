package com.galaxyt.normae.sms;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SMS 启动类
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/21 10:48
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/21 10:48     zhouqi          v1.0.0           Created
 */
@Slf4j
@EnableFeignClients(basePackages = "com.galaxyt")
@SpringBootApplication(scanBasePackages = "com.galaxyt")
@MapperScan(basePackages = "com.galaxyt.normae.sms.dao")
public class SmsApplication{

    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
        log.info("============================ SMS * System startup completed ===================================================");
    }

}
