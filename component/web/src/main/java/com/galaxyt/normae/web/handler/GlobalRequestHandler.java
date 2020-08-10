package com.galaxyt.normae.web.handler;

import com.galaxyt.normae.core.thread.CurrentUser;
import com.galaxyt.normae.core.util.seurity.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class GlobalRequestHandler implements HandlerInterceptor {

    /**
     * 请求前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*
        此处进入到此处则认定网关已经进行过权限认证
         */
        Optional<String> userIdO = Optional.ofNullable(request.getHeader(JWTUtil.USER_ID));

        //此处只要 userId 不为 null 则表示 username 也是合法的 , 若 username 可为 null 也可以
        userIdO.ifPresent(userId -> CurrentUser.init(Long.parseLong(userId), request.getHeader(JWTUtil.USERNAME)));

        return Boolean.TRUE;
    }


    /**
     * 请求后置拦截
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //当前请求结束需要销毁线程中存储的内容，否则线程池的作用会导致这些缓存的数据无法被虚拟机销毁
        CurrentUser.destroy();
    }

}
