package com.galaxyt.normae.web.config;

import com.galaxyt.normae.web.handler.GlobalRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/18 11:44
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/18 11:44     zhouqi          v1.0.0           Created
 */
@Slf4j
@Configuration
public class WebAppConfig implements WebMvcConfigurer {


    @Autowired
    private GlobalRequestHandler globalRequestHandler;

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(this.globalRequestHandler)  //注册改拦截器
                .addPathPatterns("/**")     //表示拦截所有的请求，
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");  //排除 swagger 拦截
    }
}
