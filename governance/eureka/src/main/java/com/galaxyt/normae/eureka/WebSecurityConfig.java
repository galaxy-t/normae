package com.galaxyt.normae.eureka;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Eureka开启安全验证之后SpringSecurity会默认开启csrf检验，需要将其关闭，否则客户端注册不上
 * @author zhouqi
 * @date 2020/8/25 9:45
 * @version v1.0.0
 * @Description
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        super.configure(http);
    }

}
