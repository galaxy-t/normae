package com.galaxyt.normae.security.provider;

import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用于获取全部需要通过网关向外暴露的接口
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/6/9 17:54
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/6/9 17:54     zhouqi          v1.0.0           Created
 */
@Slf4j
@Component
public class RequestMappingProcessor {

    @Autowired
    private WebApplicationContext webApplicationContext;


    public List<AuthorityWrapper> findAll() {

        //未避免与 Swagger2 中的一个类型冲突 , 所以使用 beanName 的方式获取
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) this.webApplicationContext.getBean("requestMappingHandlerMapping");
        //获取全部方法
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        //用于装载解析出来的全部权限
        List<AuthorityWrapper> authoritys = new ArrayList<>();

        //循环
        for (RequestMappingInfo info : map.keySet()) {

            //获取提供该接口的方法
            HandlerMethod method = map.get(info);

            /*
            获取该方法上标注的 Authority 注解
            仅当该接口方法上存在 Authority 注解时才会认为该接口会通过网关进行暴露 , 否则程序将不再对该接口进行处理 , 网关也不会允许访问该接口
             */
            Optional<Authority> authorityO = Optional.ofNullable(method.getMethodAnnotation(Authority.class));
            authorityO.ifPresent(authority -> {

                //获取 url 地址
                String url = (String) info.getPatternsCondition().getPatterns().toArray()[0];
                //获取请求类型
                String requestMethod = info.getMethodsCondition().getMethods().toArray()[0].toString();

                AuthorityWrapper wrapper = new AuthorityWrapper();
                wrapper.setUrl(url);
                wrapper.setMethod(requestMethod);
                wrapper.setRole(authority.role());
                wrapper.setAuthority(authority.authority());
                wrapper.setDescription(authority.description());
                wrapper.setLogin(authority.isLogin());
                wrapper.setHaveAuthority(authority.isHaveAuthority());

                authoritys.add(wrapper);
                log.debug(wrapper.toString());
            });

        }

        return authoritys;
    }


}
