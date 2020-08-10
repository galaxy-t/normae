package com.galaxyt.normae.web.config;

import com.galaxyt.normae.core.util.seurity.JWTUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign 配置
 *
 * @author zhouqi
 * @date 2020/5/18 11:31
 * @version v1.0.0
 * @Description
 * 1: 配置请求头传递 TOKEN , 解决内部 feign 调用无 token 问题
 *    要求服务的熔断配置开启信号量模式 , 否则无法传递 TOKEN
 *    配置: hystrix.command.default.execution.isolation.strategy: SEMAPHORE
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 11:31     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
/*@Configuration*/
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                requestTemplate.header(JWTUtil.USER_ID, request.getHeader(JWTUtil.USER_ID));
                requestTemplate.header(JWTUtil.USERNAME, request.getHeader(JWTUtil.USERNAME));
            }
        } catch (Exception e) {
            //忽略这个异常
        }
    }




}
